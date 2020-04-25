package com.example.sunscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.CAMERA;


public class cameraActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 3;
    public static final int CAMERA_PERM_CODE = 2;
    public static final int GALLERY_REQUEST_CODE = 12;


    Button cameraBtn, galleryBtn;
    String currentPhotoPath;

    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
    String timeStamp = dateFormat.format(date);


    BottomNavigationView bottomNavigationView;

    int flag1 = 0;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        int flag1 = 0;


        bottomNavigationView = findViewById(R.id.nav_viewCam);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:{
                        Intent intent = new Intent(cameraActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.navigation_facts:{
                        Intent intent = new Intent(cameraActivity.this, facts.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });

        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCamPermission();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });
    }

    private void askCamPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode== Activity.RESULT_OK){

                File f = new File (currentPhotoPath);
                //selectedImage.setImageURI(Uri.fromFile(f));
                Log.d("Tag", "Absolute Url of image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);

                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                Context ctx = this;


                //upload to Firebase
                //uploadToFirebase(f.getName(), contentUri);

                //start ML local analyzer
                try {
                    runTextRecognition(ctx, contentUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("Text Recognition Failed");
                }
                if (contentUri == null){
                    Intent restart = getIntent();
                    finish();
                    startActivity(restart);
                }
                else{
                    Intent intent = new Intent(cameraActivity.this, splashScreen2.class);
                    intent.putExtra("flag", Integer.toString(flag1));
                    startActivity(intent);
                }

            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode==Activity.RESULT_OK){
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("Tag", "Gallery Image Uri: " + imageFileName);
                //selectedImage.setImageURI(contentUri);

                //upload to Firebase
                //uploadToFirebase(imageFileName, contentUri);

                Context ctx = this;
                //start ML local analyzer
                try {
                    runTextRecognition(ctx, contentUri);
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("FAILED");
                }

                if (contentUri == null){
                    Intent restart = getIntent();
                    finish();
                    startActivity(restart);
                }
                else{

                    Intent intent = new Intent(cameraActivity.this, splashScreen2.class);
                    intent.putExtra("flag", Integer.toString(flag1));
                    startActivity(intent);
                }
            }
        }
    }

    private void flag(){
        flag1 = 1;
    }


    private void runTextRecognition(Context d, Uri x) throws IOException {
        FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(d, x);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processTextRecognitionResult(firebaseVisionText);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        showToast("Processing");
        String result = texts.getText();
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0){
            showToast("No text found");
            flag();

        }
        else{
            //showToast("Text Detected");
            for (FirebaseVisionText.TextBlock block : texts.getTextBlocks()){
                String blockText = block.getText();
                //SaveToFile(blockText);
                for (FirebaseVisionText.Line line : block.getLines()){
                    String lineText = line.getText();
                    //SaveToFile(lineText);
                    for (FirebaseVisionText.Element element : line.getElements()){
                        String elementText = element.getText();
                        SaveToFile(elementText);
                    }
                }
            }

            //showToast("Text Saved to txt file");
            Uri txtFile = Uri.fromFile(new File("/data/data/com.example.camera_gallery/files/suncheck/" + timeStamp + ".txt"));
            //TextFileUploadToFirebase("NutriText", txtFile);
            //createDateButton();


            //DeleteFile();


        /*for (int i = 0; i < blocks.size(); i++){
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();

            for (int j = 0; j < lines.size(); j++){
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();

         */
        }
    }


    private void SaveToFile(String myLine){
        File textFile = new File(cameraActivity.this.getFilesDir(), "suncheck");
        if (!textFile.exists()){
            textFile.mkdir();
        }
        try{

            File testFile = new File(textFile, timeStamp + ".txt");
            FileWriter fw = new FileWriter(testFile, true);
            PrintWriter printWriter = new PrintWriter(fw);
            printWriter.println(myLine);
            fw.flush();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider1", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
}
