package com.example.sunscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class results2 extends AppCompatActivity {

    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
    String timeStamp = dateFormat.format(date);

    TextView resultText, listingText;
    BottomNavigationView bottomNavigationView;


    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results2);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        listingText = findViewById(R.id.listing);
        resultText = findViewById(R.id.resultText);

        bottomNavigationView = findViewById(R.id.nav_results2);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:{
                        Intent intent = new Intent(results2.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.navigation_check:{
                        Intent intent = new Intent(results2.this, cameraActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.navigation_facts:{
                        Intent intent = new Intent(results2.this, facts.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });


        try {
            File suncheck1 = new File(results2.this.getFilesDir(), "suncheck/" + timeStamp + ".txt");
            if (!suncheck1.exists()){
                resultText.setText("We did not detect any words in your image");
            }
            else{
                checkIngredients();
            }
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkIngredients() throws IOException {
        int flag = 0;

        File suncheck = new File(results2.this.getFilesDir(), "suncheck/" + timeStamp + ".txt");

        File finalListing = new File(results2.this.getFilesDir(), "suncheck/" + "1.txt");
        FileWriter fw = new FileWriter(finalListing, true);
        PrintWriter printWriter = new PrintWriter(fw);

        BufferedReader reader;
        reader = new BufferedReader(new FileReader(suncheck));
        String line = reader.readLine();
        printWriter.println(" ");

        while (line != null){

            if (line.equals("Oxybenzone")){
                printWriter.println("Oxybenzone");
                flag = 1;
            }
            if (line.equals("octinoxate")){
                printWriter.println("Octinoxate");
                flag = 1;
            }
            if (line.equals("Octocrylene")){
                printWriter.println("Octocrylene");
                flag = 1;
            }
            if (line.equals("Benzophenone-1")){
                printWriter.println("Benzophenone-1");
                flag = 1;
            }
            if (line.equals("Benzophenone-8")){
                printWriter.println("Benzophenone-8");
                flag = 1;
            }
            if (line.equals("OD-PABA")){
                printWriter.println("OD-PABA");
                flag = 1;
            }
            if (line.equals("4-methylbenzylidene camphor")){
                printWriter.println("4-methylbenzylidene camphor");
                flag = 1;
            }
            if (line.equals("3-benzylidene camphor")){
                printWriter.println("3-benzylidene camphor");
                flag = 1;
            }
            if (line.equals("Titanium dioxide") || (line.equals("Nano-titanium dioxide"))){
                printWriter.println("Nano-titanium dioxide");
                flag = 1;
            }
            if (line.equals("Nano-zinc oxide")){
                printWriter.println("Nano-zinc oxide");
                flag = 1;
            }
            line = reader.readLine();

        }

        fw.flush();
        fw.close();

        BufferedReader reader2;
        reader2 = new BufferedReader(new FileReader(finalListing));

        if (flag == 0)
            resultText.setText("Awesome! Your product is Ocean-friendly\n\nHappy swimming!");

        else{
            String line2 = reader2.readLine();

            while (line2 != null){
                listingText.append(line2);
                listingText.append("\n");
                line2 = reader2.readLine();
            }
            resultText.setText("Looks like your product contains harmful ingredients:");
        }
        suncheck.delete();
        finalListing.delete();
    }
}
