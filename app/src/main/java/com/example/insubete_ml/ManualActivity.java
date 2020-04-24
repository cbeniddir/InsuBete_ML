package com.example.insubete_ml;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ManualActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    List<String> names;
    List<String> gis;
    TextView result,label_result;


    //Path of our model
    private static final String MODEL_NAME = "file:///android_asset/frozen_linear_regression.pb";
    //Name of our input node from the graph
    private static final String INPUT_NODE = "x";
    //Name of our output node from the graph
    private static final String OUTPUT_NODE = "y_output";
    //The shape of our input
    private static final long[] INPUT_SHAPE = {1L, 1L};

    private static TensorFlowInferenceInterface tensorFlowInferenceInterface;
    private EditText editText;
    private TextView textView, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        recyclerView = findViewById(R.id.view);


        names = new ArrayList<>();
        gis = new ArrayList<>();

        try {
            AssetManager am = getAssets();
            InputStream ls=am.open("basicfood.xls");

            Workbook wb = Workbook.getWorkbook(ls);
            //workbook = Workbook.getWorkbook(file);
            Sheet sheet = wb.getSheet(0);
            //Cell[] row = sheet.getRow(1);
            //text.setText(row[0].getContents());
            int rows = sheet.getRows();
            int cols = sheet.getColumns();

            for(int i = 1;i< rows;i++){
                Cell[] row = sheet.getRow(i);
                names.add(row[0].getContents());
                gis.add(row[1].getContents());
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new Adapter(this, names, gis);
            recyclerView.setAdapter(adapter);

            editText = findViewById(R.id.input);


            tensorFlowInferenceInterface = new TensorFlowInferenceInterface(getAssets(), MODEL_NAME);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    private String performInference(float input) {
        float[] floatArray = {input};

        tensorFlowInferenceInterface.feed(INPUT_NODE, floatArray, INPUT_SHAPE);
        tensorFlowInferenceInterface.run(new String[] {OUTPUT_NODE});

        float[] results = {0.0f};
        tensorFlowInferenceInterface.fetch(OUTPUT_NODE, results);

        return String.valueOf(Math.round(results[0]));

    }

     public void pressButton(View view) {
        float input = Float.parseFloat(editText.getText().toString());

         result = findViewById(R.id.result);
         label_result= findViewById(R.id.label_result);
        label_result.setVisibility(View.VISIBLE);
        result.setVisibility(View.VISIBLE);

         String results = performInference(input);

         result.setText(results);
    }

}
