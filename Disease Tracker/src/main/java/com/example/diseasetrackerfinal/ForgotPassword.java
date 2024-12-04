package com.example.diseasetrackerfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    public void Return(View view) {
        NetworkCalls networkCalls = new NetworkCalls();
        String email = ((EditText)findViewById(R.id.emailTxt)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordTxt)).getText().toString();
        String retypePassword = ((EditText)findViewById(R.id.retypePasswordTxt)).getText().toString();
        TextView message = findViewById(R.id.messagetxt);

        networkCalls.forgotPassword(email, password, retypePassword, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseBody = response.body().string().trim();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (responseBody.trim().equals("success")){
                                Intent intent = new Intent(ForgotPassword.this, LoginPage.class);
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ForgotPassword.this).toBundle());


                            }
                            else{
                                message.setText(responseBody);
                                message.setBackground(getDrawable(R.drawable.error));
                                message.setTextSize(18);

                            }



                        }
                    });
                }
                else{

                    String errorMessage = response.message();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            message.setText(errorMessage);
                        }
                    });

                }

            }
        });
        }


    }
