package com.example.insubete_ml;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

    ManualAdapter manualAdapter;
    List<String> names;
    List<String> gis;
    ProgressBar progressBar;
    TextView wait;
    ArrayList<String> ingredients;
    Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        recyclerView = findViewById(R.id.view);
        menuView = findViewById(R.id.menu);

        progressBar = findViewById(R.id.progressBar);
        wait = findViewById(R.id.wait);
        next = (Button)findViewById(R.id.validate);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManualActivity.this, QuantityActivity.class);
                intent.putStringArrayListExtra("INGREDIENTS_LIST", ingredients);
                startActivity(intent);
            }
        });

        String URL = "https://bikashthapa01.github.io/excel-reader-android-app/story.xls";

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

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(URL, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(ManualActivity.this, "Error in Downloading Excel File", Toast.LENGTH_SHORT).show();
                wait.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                WorkbookSettings ws = new WorkbookSettings();
                ws.setGCDisabled(true);
                if(file != null){
                    //text.setText("Success, DO something with the file.");
                    //wait.setVisibility(View.GONE);
                    //progressBar.setVisibility(View.GONE);

                    try {
                        workbook = Workbook.getWorkbook(file);
                        Sheet sheet = workbook.getSheet(0);
                        //Cell[] row = sheet.getRow(1);
                        //text.setText(row[0].getContents());
                        for(int i = 0;i< sheet.getRows();i++){
                            Cell[] row = sheet.getRow(i);
                            names.add(row[0].getContents());
                            gis.add(row[1].getContents());
                        }

                        showData();
                        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // TODO if item is already in the list
                                ingredients.add(ObjectLabels.get(position));
                                view.setBackgroundColor(0xB5F50057);
                                view.setEnabled(false);

                                for(int i = 0; i < ingredients.size(); i++) {
                                    System.out.println(ingredients.get(i));
                                }
                                if(ingredients.isEmpty()){
                                    validateButton.setEnabled(false);
                                    validateButton.setBackgroundColor(0xB8B8D1);
                                }
                                Toast.makeText(ClarifaiActivity.this, ObjectLabels.get(position), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
        private void showData() {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            manualAdapter = new ManualAdapter(this, names, gis);
            recyclerView.setAdapter(manualAdapter);
        }
}
