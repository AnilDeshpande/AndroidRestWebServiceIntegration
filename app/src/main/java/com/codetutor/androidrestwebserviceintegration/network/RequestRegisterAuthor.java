package com.codetutor.androidrestwebserviceintegration.network;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

class RequestRegisterAuthor extends AppNetworkRequest{

    String url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.registerAuthor;

    OkHttpClient okHttpClient;
    Request request;

    RequestRegisterAuthor(APICallListener apiCallListener, Object requestBody){


        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(5000, TimeUnit.MILLISECONDS);

        request =  new Request.Builder().url(RestAPIs.getBaseUrl()+ToDoAppRestAPI.registerAuthor)
                .addHeader("Content-Type","application/json")
                .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()))
                .build();
    }

    @Override
    void execute() {


    }

    @Override
    public void run() {
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, final IOException e) {
                ((Activity)apiCallListener).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        apiCallListener.onCallBackFailure(e.getMessage());
                    }
                });

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                ((Activity)apiCallListener).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        apiCallListener.onCallBackSuccess(response.toString());
                    }
                });
            }
        });
    }
}
