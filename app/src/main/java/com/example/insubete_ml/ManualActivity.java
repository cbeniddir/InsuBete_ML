package com.example.insubete_ml;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ManualActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    List<String> names;
    List<String> gis;


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
                Log.d("data", "titles -> "+"add names ok");
                gis.add(row[1].getContents());
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new Adapter(this, names, gis);
            recyclerView.setAdapter(adapter);

            editText = findViewById(R.id.edit_text);
            textView = findViewById(R.id.text_view);
            textView2= findViewById(R.id.label);


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
        String results = performInference(input);

        textView.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        textView.setText(results);
    }

}
