package com.example.insubete_ml;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jxl.Cell;


public class QuantityActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ListView menuView;
    ArrayList<String> ingredients;
    QuantityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);

        recyclerView = findViewById(R.id.view);
        ingredients = new ArrayList<String>();
        ingredients = getIntent().getStringArrayListExtra("INGREDIENTS_LIST");

    }
    private void showData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuantityAdapter(this, ingredients);
        recyclerView.setAdapter(adapter);
    }
}
