package com.example.diseasetrackerfinal;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkCalls {
    private static final MediaType MEDIA_TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded");
    private OkHttpClient client;
    public NetworkCalls(){
        client = new OkHttpClient();
    }

    public void loginValidate(String email, String password, Callback callback){
        email.trim();
        RequestBody requestBody = new FormBody.Builder()
                .add("email",email)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2552770/loginV3.php")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void signUp(String name, String surname, String ID, String Dob,String email, String password,Callback callback){
        email.trim();
        RequestBody requestBody = new FormBody.Builder()
                .add("Name",name)
                .add("Surname",surname)
                .add("ID",ID)
                .add("DoB",Dob)
                .add("email",email)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2552770/signUpV2.php")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void forgotPassword(String email, String password, String retypePassword, Callback callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .add("password",password)
                .add("retypePassword",retypePassword)
                .build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2552770/forgotPassword.php")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void checkIn(String name, double latitude, double longitude, int userId, String date,Callback callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("Name", name)
                .add("latitude", Double.toString(latitude))
                .add("longitude", Double.toString(longitude))
                .add("user_id", Integer.toString(userId))
                .add("date", date)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2552770/mapV2.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void diagnoses(String userId,String date,String test_result,Callback callback){
        RequestBody formBody = new FormBody.Builder()
                .add("userid", userId)
                .add("date",date)
                .add("test_result",test_result.toString())
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2552770/covidCases.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void infectedLocations(double latitude, double longitude,Callback callback){
        RequestBody formBody = new FormBody.Builder()
                .add("latitude",Double.toString(latitude))
                .add("longitude",Double.toString(longitude))
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2552770/infectedLocationsV2.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void exposureCheck(String userid,Callback callback){
        RequestBody formBody = new FormBody.Builder()
                .add("userId",userid)
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2552770/exposureCheck.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }



}
