package com.example.diseasetrackerfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class exposureCheck extends AppCompatActivity {

    private NetworkCalls networkCalls = new NetworkCalls();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exposure_check);
        Bundle extras = getIntent().getExtras();
        String user_id = extras.getString("id");
        TextView t = findViewById(R.id.results);
        TextView x = findViewById(R.id.count);
        networkCalls.exposureCheck(user_id, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseBody.trim();
                            String counter = "Based on my data the amount of infected people you might have been exposed to is "+ responseBody;
                            String risk = risk(responseBody);
                            String counterAndRisk = counter + risk;
                            type(counterAndRisk,x);


                        }
                    });
                }else{
                    String responseBody = response.message();
                    Toast.makeText(getApplicationContext(),responseBody,Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void type(String word, TextView t){

        long delayInMillis = 20;

        final int[] charIndex = {0};
        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (charIndex[0] < word.length()) {
                    t.setText(word.substring(0, charIndex[0] + 1));
                    charIndex[0]++;
                    handler.postDelayed(this, delayInMillis);
                }
            }
        };

        handler.postDelayed(runnable, delayInMillis);
    }

    public String risk(String t){
        String risk = null;
        int x = Integer.parseInt(t);

        if(x == 0){
            risk = "Your estimated risk is less than 0";
        }else if(x < 10 && x > 0){
            risk = "Your estimated risk is low";
        } else if (x > 10 && x < 15) {
            risk = "Your estimated risk is medium";

        }else{
            risk = "Your estimated risk is high";
        }
        return risk;
    }

    public void returnToHomepage(View view) {
        finish();
    }

    public void nextSteps(View view) {
        Intent intent = new Intent(exposureCheck.this, nextSteps.class);
        startActivity(intent);
    }
}