package com.example.diseasetrackerfinal;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);



    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing App")
                .setMessage("Are you sure you want to close this app? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        finishAffinity();
                        System.exit(0);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void Login(View view) {
        NetworkCalls networkCalls = new NetworkCalls();
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        TextView welcome = findViewById(R.id.welcomeText);

        networkCalls.loginValidate(email, password, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!responseBody.trim().equals("Invalid Password") && !responseBody.trim().equals("Email not found,please sign up")){
                                String name = responseBody;
                                String[] array = name.split(",");
                                String Name = array[0].trim();
                                String id = array[1].trim();

                                System.out.println(array);
                                SharedPreferences sharedPreferences = getSharedPreferences("SessionData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("id",id);
                                editor.putString("Name", Name);
                                editor.apply();

                                Intent intent = new Intent(LoginPage.this, HomePage.class);
                                intent.putExtra("Name",Name);
                                intent.putExtra("id",id);
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginPage.this).toBundle());


                            }
                            else{
                                welcome.setText(responseBody);
                                welcome.setBackground(getDrawable(R.drawable.error));
                                welcome.setTextSize(18);

                            }



                        }
                    });
                }
                else{
                    String errorMessage = response.message();
                }

            }
        });


    }

    public void takeUserToSignUp(View view) {
        Intent intent = new Intent(LoginPage.this, SignUpPage.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginPage.this).toBundle());
    }

    public void forgotPassword(View view) {
        Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginPage.this).toBundle());
    }
}


