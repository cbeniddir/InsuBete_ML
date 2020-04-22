package com.example.insubete_ml;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class QuantityActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ListView menuView;
    ArrayList<String> ingredients;
    QuantityAdapter adapter;
    Button validateButton;
    public ArrayList<IngredientModel> editModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);

        recyclerView = findViewById(R.id.view);
        validateButton = (Button) findViewById(R.id.validate);

        ingredients = getIntent().getStringArrayListExtra("INGREDIENTS_LIST");
        editModelArrayList = populateList(ingredients);
        System.out.println("getting ingredients in on create");
        for(int i = 0; i < ingredients.size(); i++) {
            System.out.println(ingredients.get(i));
        }
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuantityAdapter(this, editModelArrayList);
        recyclerView.setAdapter(adapter);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("data", "titles -> "+ Arrays.toString(editModelArrayList.toArray()));
                getIngredientModelValues(editModelArrayList);

                for(int i = 0; i < editModelArrayList.size(); i++){
                    System.out.println("CHO "+ editModelArrayList.get(i).getChoQuantity());
                    System.out.println("GI" + editModelArrayList.get(i).getGi());
                    System.out.println("Serving Size" + editModelArrayList.get(i).getServingSize());
                }

                float finalGI = computeGlycemicIndex(editModelArrayList);
                System.out.println("final GI" + finalGI);

                Intent intent = new Intent(getBaseContext(), PredictActivity.class);
                intent.putExtra("FINAL_IG",finalGI);
                startActivity(intent);

            }
        });

    }

    private ArrayList<IngredientModel> populateList(ArrayList<String> ingredients){
        ArrayList<IngredientModel> list = new ArrayList<>();
        //Get all selected ingredients and place them in an Ingredient Model
        for(String item : ingredients){
            IngredientModel editModel = new IngredientModel();
            editModel.setName((String.valueOf(item)));
            editModel.setQuantity("0");
            editModel.setServingSize("0");
            editModel.setGi("0");
            editModel.setChoQuantity("0");
            editModel.setGiByQuantity("0");
            list.add(editModel);
        }
        return list;
    }

    //To compute the GI of the entire meal
    public float computeGlycemicIndex(ArrayList<IngredientModel>list_ingredients){
        //First, we have to compute glycemic index for each aliment of the meal, for the quantity eaten
        float GITotal = 0.0f;

        for(IngredientModel ingredient : list_ingredients){
            //Parse en float des éléments pour calculer la bonne quantité de cho
            float quantity = Float.parseFloat(ingredient.getQuantity());
            float quantityCHO = Float.parseFloat(ingredient.getChoQuantity());
            float quantityServing = Float.parseFloat(ingredient.getServingSize());

            //Calcul de la bonne quantité de Cho
            float quantityFinaleCHO = (quantity * quantityCHO)/quantityServing;
            float giIngredientStandard = Float.parseFloat(ingredient.getGi());

            if(giIngredientStandard != 0) {
                float giIngredientByQuantity = (quantityFinaleCHO / quantity) * giIngredientStandard;
                GITotal += giIngredientByQuantity;
            }

        }

        //return the GI of the entire meal
        return GITotal;
    }

    public ArrayList<IngredientModel> getIngredientModelValues(ArrayList<IngredientModel> list_ingredient) {

        try{
            AssetManager am = getAssets();
            InputStream ls=am.open("basicfood.xls");

            Workbook wb = Workbook.getWorkbook(ls);
            //workbook = Workbook.getWorkbook(file);
            Sheet sheet = wb.getSheet(0);
            int rows = sheet.getRows();
            int cols = sheet.getColumns();

            //On parcourt la liste des ingrédients sélectionnés
            for(int j = 0; j < list_ingredient.size(); j++) {
                //On parcourt les lignes jusqu'à retrouver chaque ingrédient de la liste dans l'excel
                for (int i = 1; i < rows - 1; i++) {
                    Cell[] row = sheet.getRow(i);
                    if (row[0].getContents().equals(list_ingredient.get(j).getName())) {

                            list_ingredient.get(j).setGi(row[1].getContents());
                            list_ingredient.get(j).setServingSize(row[2].getContents());
                            list_ingredient.get(j).setChoQuantity(row[3].getContents());

                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
                return list_ingredient;
    }

}
