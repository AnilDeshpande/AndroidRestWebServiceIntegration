package com.codetutor.androidrestwebserviceintegration.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestDeleteToDo extends AppNetworkRequest{
    public static final String TAG = RequestDeleteToDo.class.getSimpleName();

    String url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.registerAuthor;

    Request request;

    RequestDeleteToDo(APICallListener apiCallListener, Object requestBody){

        super(apiCallListener);
        request =  new Request.Builder().url(url)
                .addHeader(AppNetworkRequest.CONTENT_TYPE,AppNetworkRequest.JSON_CONTENT_TYPE)
                .delete(RequestBody.create(MediaType.parse(AppNetworkRequest.JSON_CONTENT_TYPE), requestBody.toString()))
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
            public void onResponse(Call call,final Response response) throws IOException {
                try{
                    if(response.code()==204){
                        responseObject = null;
                    }else{
                        throw new IOException("Couldn't delete");
                    }

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