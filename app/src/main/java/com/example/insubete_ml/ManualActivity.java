package com.example.insubete_ml;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ManualActivity extends AppCompatActivity {
    Workbook workbook;
    AsyncHttpClient asyncHttpClient;
    RecyclerView recyclerView;
    ListView menuView;

    Adapter adapter;
    List<String> names;
    List<String> gis;
    ProgressBar progressBar;
    TextView wait;
    List<String> ingredients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        recyclerView = findViewById(R.id.view);
        menuView = findViewById(R.id.menu);

        progressBar = findViewById(R.id.progressBar);
        wait = findViewById(R.id.wait);

        names = new ArrayList<>();
        gis = new ArrayList<>();
        ingredients = new ArrayList<>();


//        // checking if the excel file has new content
//
//        try {
//            URL mURL = new URL(apiURL);
//            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) mURL.openConnection();
//            InputStream inputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
//            // getting network os exception error
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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

                        for(int i = 1;i< rows-1;i++){
                            Cell[] row = sheet.getRow(i);
                            names.add(row[0].getContents());
                            Log.d("data", "titles -> "+"add names ok");
                            gis.add(row[1].getContents());
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        adapter = new Adapter(this, names, gis);
                        recyclerView.setAdapter(adapter);


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                }
}
