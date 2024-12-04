package com.example.diseasetrackerfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignUpPage extends AppCompatActivity {
    private String DoB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        Button Dob = findViewById(R.id.Dob);

        Dob.setOnClickListener(v -> showDatePickerDialog());

        EditText id = findViewById(R.id.identificationNumber);
        id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new AlertDialog.Builder(SignUpPage.this)
                            .setTitle("Privacy Notice")
                            .setMessage("Due to POPI regulations, ID number is not collected or shared")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            }
        });



    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                (view, year, month, dayOfMonth) -> DoB = year + "-" + (month + 1) + "-" + dayOfMonth,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    public void SignUp(View view) {
        NetworkCalls networkCalls = new NetworkCalls();
        String name = ((EditText) findViewById(R.id.Name)).getText().toString();
        String surname = ((EditText) findViewById(R.id.Surname)).getText().toString();
        String ID = ((EditText) findViewById(R.id.identificationNumber)).getText().toString();
        String email = ((EditText) findViewById(R.id.emailEdit)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEdit)).getText().toString();
        TextView signup = findViewById(R.id.signText);

        networkCalls.signUp(name,surname,ID,DoB,email, password, new Callback() {
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
                            if(responseBody.trim().equals("SignUp successful")){
                                Intent intent = new Intent(SignUpPage.this,LoginPage.class);
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignUpPage.this).toBundle());
                            }
                            else{
                                signup.setText(responseBody);
                                signup.setBackground(getDrawable(R.drawable.error));
                                signup.setTextSize(18);
                            }




                        }
                    });
                }
                else{
                    String errorMessage = response.message();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            signup.setText(errorMessage);
                            signup.setBackground(getDrawable(R.drawable.error));
                            signup.setTextSize(18);
                        }
                    });


                }

            }
        });


    }
}

