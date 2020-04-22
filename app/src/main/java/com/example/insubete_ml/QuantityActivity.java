package com.example.insubete_ml;
import android.content.Intent;
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

                //computeGlycemicIndex();
                //getItemViews();
                //final ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                //new QuantityAdapter(getApplicationContext(), ingredients).getQuantities()
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
            list.add(editModel);
        }

        return list;
    }

    public float computeGlycemicIndex(){
        //First, we have to compute glycemic index for each aliment of the meal, for the quantity eaten


        //After this, we'll sum every GI of the aliments that are composing the meal in order to compute the total GI

        //return the GI of the entire meal
        return 0.0f;
    }



}
