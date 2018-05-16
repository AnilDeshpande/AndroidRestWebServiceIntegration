package com.codetutor.androidrestwebserviceintegration.network;

import android.util.Log;

import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

class RequestRegisterAuthor extends AppNetworkRequest{

    public static final String TAG = RequestRegisterAuthor.class.getSimpleName();

    String url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.registerAuthor;

    Request request;

    RequestRegisterAuthor(APICallListener apiCallListener, Object requestBody){

        super(apiCallListener);
        request =  new Request.Builder().url(RestAPIs.getBaseUrl()+ToDoAppRestAPI.registerAuthor)
                .addHeader(AppNetworkRequest.CONTENT_TYPE,AppNetworkRequest.JSON_CONTENT_TYPE)
                .post(RequestBody.create(MediaType.parse(AppNetworkRequest.JSON_CONTENT_TYPE), requestBody.toString()))
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
            public void onFailure(Request request, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallListener.onCallBackFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                try{
                    responseObject = new GsonBuilder().create().fromJson(response.body().string(), Author.class);
                }catch (IOException e){
                    Log.d(TAG,e.getMessage());
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
