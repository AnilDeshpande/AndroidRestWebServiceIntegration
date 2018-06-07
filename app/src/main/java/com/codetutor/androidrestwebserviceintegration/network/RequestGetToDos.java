package com.codetutor.androidrestwebserviceintegration.network;

import android.util.Log;

import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.restbean.Error;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoItem;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoListResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RequestGetToDos extends  AppNetworkRequest{
    public static final String TAG = RequestGetToDos.class.getSimpleName();

    String url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.getToDoItem+ AppConfig.getSavedSuccessfulAuthor().getAuthorEmailId();

    Request request;

    RequestGetToDos(APICallListener apiCallListener, Object jsonRequestBody){

        super(apiCallListener);
        request =  new Request.Builder().url(url)
                .addHeader(AppNetworkRequest.TOKEN,AppConfig.getSessionTokenValue())
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
                    if(response.code()==200){
                        responseObject = new Gson().fromJson(response.body().string(), ToDoListResponse.class);
                    }else{
                        responseObject = new Error(response.code(),response.message());
                    }
                } catch (IOException e) {
                    responseObject = new Error(101,e.getMessage());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallListener.onCallBackSuccess(REQUEST_TYPE.REQUEST_GET_TODOS, responseObject);
                    }
                });

            }
        });
    }
}
