package com.example.insubete_ml;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PredictActivity extends AppCompatActivity {

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
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);

        editText = findViewById(R.id.edit_text);
        textView = findViewById(R.id.text_view);

        tensorFlowInferenceInterface = new TensorFlowInferenceInterface(getAssets(), MODEL_NAME);
    }

    public void pressButton(View view) {
        float input = Float.parseFloat(editText.getText().toString());
        String results = performInference(input);
        textView.setText(results);
    }

    private String performInference(float input) {
        float[] floatArray = {input};

            tensorFlowInferenceInterface.feed(INPUT_NODE, floatArray, INPUT_SHAPE);
            tensorFlowInferenceInterface.run(new String[] {OUTPUT_NODE});

            float[] results = {0.0f};
            tensorFlowInferenceInterface.fetch(OUTPUT_NODE, results);

            return String.valueOf(Math.round(results[0]));

    }

}