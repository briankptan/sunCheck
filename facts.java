package com.example.sunscreen;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class facts extends AppCompatActivity {

    BottomNavigationView navView;
    TextView textView;

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        textView = findViewById(R.id.textView2);
        textView.append("EFFECTS OF OXYBENZONE AND OTHER CHEMICALS FOUND IN SUNSCREEN\n\nEach year, up to" +
        "14,000 tons of sunscreen is washed off into the ocean. Wearing sunscreen is allegedly a healthy habit," +
        "however, it can have devastating effects on the environment, especially our oceans. Research has shown the" +
        "exponential effects of sunscreen on marine life. In one study, conducted by Danovaro et al. (2008), it was" +
        "found that even in small doses, sunscreen can be deadly. The researcher applied the recommended amount" +
        "of sunscreen to volunteers' hands and had the volunteers immerse their hands into 2L containers containing" +
        "various species of coral. Within 96 hours, all of the coral exposed to the sunscreen were completely bleached.\n\n" +
        "Oxybenzone (BP-3) and octinoxate are two of the popular of many harmful chemicals found in 3,500+ skin care" +
        "products worldwide. These chemicals are highly toxic to juvenile corals and other marine life and can enter" +
        "the environment through both wastewater and directly from swimmers wearing sunscreen. Research has shown" +
        "four major toxic effects in coral: increased susceptibility to bleaching, DNA damage, abnormal skeleton growth," +
        "and gross deformities of baby coral.\n\nIn addition to damaging coral, sunscreen chemicals can have devastating" +
        "effects on algae, fish, and even dolphins." +
        "\n\nTo learn even more about the effects of sunscreen on marine life, visit\n\n" +
        "oceanservice.noaa.gov/news/sunscreen-corals.html\n\n" +
        "marinesafe.org/blog/2016/03/18/sunscreen-pollution/\n\n" +
        "seagoinggreen.org/blog/2018/2/12/how-is-sunscreen-actually-affecting-the-marine-environment\n\n" +
        "icriforum.org/sites/default/files/ICRI_Sunscreen_0.pdf\n\n\n\n");

        navView = findViewById(R.id.nav_facts);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_check: {
                        Intent intent = new Intent(facts.this, cameraActivity.class);
                        startActivity(intent);
                        return true;
                    }
                    case R.id.navigation_home: {
                        Intent intent = new Intent(facts.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });


    }
}
