package com.example.sunscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class impact extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    EditText numPpl, numHrs;
    Button calculate, clear;
    String ppl, hrs;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impact);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        bottomNavigationView = findViewById(R.id.nav_impactView);
        numPpl = findViewById(R.id.pplNumTF);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_check: {
                        Intent intent = new Intent(impact.this, cameraActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.navigation_facts: {
                        Intent intent = new Intent(impact.this, facts.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.navigation_home: {
                        Intent intent = new Intent(impact.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });

        numPpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ppl = numPpl.getText().toString();
            }
        });





    }
}

