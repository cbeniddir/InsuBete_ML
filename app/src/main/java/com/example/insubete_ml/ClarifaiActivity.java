package com.example.insubete_ml;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

public class ClarifaiActivity extends AppCompatActivity {

    String api_key = "2cb3a797413b46df82292964d33d4398";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String photoPath = null;
    Bitmap bitmap;
    ImageView imageView;
    ListView listView;
    //In case we would like remove doubles :
    // ArrayList<String> ArrayLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clarifai);

    }

    private class ClarifaiTask extends AsyncTask<File, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(File... images) {

            //In case we would like remove doubles :
            //HashSet<String> Labels = new HashSet<>();
            ArrayList<String> Labels = new ArrayList<String>();

            ClarifaiClient client = new ClarifaiBuilder(api_key).buildSync();
            List<ClarifaiOutput<Concept>> predictionResults;

            //Make prediction for each image in parameters
            for (File image : images) {
                bitmap = BitmapFactory.decodeFile(image.getPath());
                Model<Concept> generalModel = client.getDefaultModels().generalModel();
                PredictRequest<Concept> request = generalModel.predict().withInputs(
                        ClarifaiInput.forImage(image)
                );
                List<ClarifaiOutput<Concept>> result = request.executeSync().get();

                //Get Labels of each object and put it in an ArrayList<String>
                for(int i = 0; i < result.size(); i++){
                    for(int j = 0; j < result.get(i).data().size(); j++){
                        String labelResult = result.get(i).data().get(j).name();
                        Labels.add(labelResult);
                    }
                }
                //In case we would like to remove doubles :
                // ArrayLabels = new ArrayList<>(Labels);
            }

            return Labels;
        }

        protected void onPostExecute(ArrayList<String> ObjectLabels) {
            //Displaying predicted labels
            listView = (ListView) findViewById(R.id.labels);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ClarifaiActivity.this,android.R.layout.simple_list_item_1, ObjectLabels);
            listView.setAdapter(arrayAdapter);

            //Displaying the picture which has been taken
            imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(bitmap);

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        // If we've taken a photo, send it off to Clarifai to check
        if (photoPath != null) {
            new ClarifaiTask().execute(new File(photoPath));
        }
    }


    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                File storageDir = getFilesDir();
                photoFile = File.createTempFile(
                        "SNAPSHOT",
                        ".jpg",
                        storageDir
                );

                photoPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                return;
            }

            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.insubete_ml.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }


}
