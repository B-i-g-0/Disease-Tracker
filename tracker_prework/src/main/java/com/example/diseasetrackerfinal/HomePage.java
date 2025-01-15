package com.example.diseasetrackerfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;

public class HomePage extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Bundle extras = getIntent().getExtras();
        TextView t = findViewById(R.id.header_title);
        String name = extras.getString("Name");
        String header = "welcome to Disease Tracker ,"+name+" your personal Covid-19 contact tracing app.";
        type(header,t);

        Button checkInButton = findViewById(R.id.checkInButton);
        Button diagnosisbutton = findViewById(R.id.diagnosisButton);
        Button exposurebutton = findViewById(R.id.exposureButton);
        Button infectedLocations = findViewById(R.id.infectedLocalButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        slidAnim(checkInButton);
        slidAnim(diagnosisbutton);
        slidAnim(exposurebutton);
        slidAnim(infectedLocations);
        slidAnim(logoutButton);

    }

    public void slidAnim(Button b){
        Animation slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        b.setVisibility(View.VISIBLE);
        b.startAnimation(slideInAnimation);
    }




    public void type(String word,TextView t){

        long delayInMillis = 20;

        final int[] charIndex = {0};
        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (charIndex[0] < word.length()) {
                    t.setText("Hi! " + word.substring(0, charIndex[0] + 1));
                    charIndex[0]++;
                    handler.postDelayed(this, delayInMillis);
                }
            }
        };

        handler.postDelayed(runnable, delayInMillis);
    }

    public void checkInButton(View view) {
        if (ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Bundle extras = getIntent().getExtras();
            String id = extras.getString("id");
            Intent intent = new Intent(HomePage.this,MapActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(HomePage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    public void diagnosisButton(View view) {
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        Intent intent = new Intent(HomePage.this,ReportDiagnosis.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void infectedLocations(View view) {
        if (ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Bundle extras = getIntent().getExtras();
            String id = extras.getString("id");
            Intent intent = new Intent(HomePage.this, infectedLocations.class);
            intent.putExtra("id",id);
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(HomePage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    public void exposureCheck(View view) {
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        Intent intent = new Intent(HomePage.this,exposureCheck.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void logOutButton(View view) {
        finish();
    }
}