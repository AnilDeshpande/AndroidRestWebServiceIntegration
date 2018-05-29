package com.codetutor.androidrestwebserviceintegration.network;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class RequestGetToDos extends  AppNetworkRequest{
    public static final String TAG = RequestRegisterAuthor.class.getSimpleName();

    String url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.getToDoItem;

    Request request;

    RequestGetToDos(APICallListener apiCallListener, Object requestBody){

        super(apiCallListener);
        request =  new Request.Builder().url(url)
                .addHeader(AppNetworkRequest.CONTENT_TYPE,AppNetworkRequest.JSON_CONTENT_TYPE)
                .get()
                .build();
    }

    @Override
    public void makeBackEndRequest() {
        new Thread(this).start();
    }
    @Override
    public void run() {
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallListener.onCallBackFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    responseObject = new Gson().fromJson(response.body().string(), List.class);
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallListener.onCallBackSuccess(responseObject);
                    }
                });

            }
        });
    }
}