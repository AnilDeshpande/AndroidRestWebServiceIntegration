package com.codetutor.androidrestwebserviceintegration.network;

import android.util.Log;

import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.restbean.Error;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoItem;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestModifyToDo extends AppNetworkRequest{

    public static final String TAG = RequestModifyToDo.class.getSimpleName();

    String url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.modifyToDoUrl;

    Request request;

    RequestModifyToDo(APICallListener apiCallListener, Object jsonRequestBody){

        super(apiCallListener);
        request =  new Request.Builder().url(url)
                .addHeader(AppNetworkRequest.CONTENT_TYPE,AppNetworkRequest.JSON_CONTENT_TYPE)
                .addHeader(AppNetworkRequest.TOKEN, AppConfig.getSessionTokenValue())
                .put(RequestBody.create(MediaType.parse(AppNetworkRequest.JSON_CONTENT_TYPE), jsonRequestBody.toString()))
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
            public void onResponse(Call call, final Response response)  {
                try{
                    if(response.code()==202) {
                        responseObject = new Gson().fromJson(response.body().string(), ToDoItem.class);
                    } else {
                        responseObject = new Error(response.code(),response.message());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    responseObject = new Error(101,e.getMessage());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallListener.onCallBackSuccess(REQUEST_TYPE.REQUEST_MODIFY_TODO, responseObject);
                    }
                });

            }
        });
    }

}
