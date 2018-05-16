package com.codetutor.androidrestwebserviceintegration.network;

import android.os.Handler;

import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;


public abstract class AppNetworkRequest implements Runnable{

    Handler handler;



    APICallListener apiCallListener;

    OkHttpClient okHttpClient;

    Object responseObject;

    public static enum REQUEST_TYPE{
        REQUEST_REGISTER_AUTHOR,
        REQUEST_LOGIN_AUTHOR,
        REQUEST_LOGOUT_AUTHOR,
        REQUEST_ADD_TODO_ITEM,
        REQUEST_GET_TODOS,
        REQUEST_DELETE_TODO,
        REQUEST_MODIFY_TODO

    }

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String JSON_CONTENT_TYPE  = "application/json";

    public static final int CONNECT_TIMEOUT=5000;
    public static final int READ_TIMEOUT=5000;

    AppNetworkRequest(APICallListener apiCallListener){
        handler = new Handler(AppConfig.getContext().getMainLooper());
        this.apiCallListener=apiCallListener;
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public static AppNetworkRequest getReqestInstance(REQUEST_TYPE requestType, APICallListener apiCallListener,Object requestBody){

        AppNetworkRequest appNetworkRequest=null;


        switch (requestType){
            case REQUEST_REGISTER_AUTHOR:
                appNetworkRequest = getRegisterAuthorRequest(REQUEST_TYPE.REQUEST_LOGIN_AUTHOR, apiCallListener, requestBody);
                break;
            case REQUEST_LOGIN_AUTHOR: break;
            case REQUEST_LOGOUT_AUTHOR: break;
            case REQUEST_ADD_TODO_ITEM: break;
            case REQUEST_GET_TODOS: break;
            case REQUEST_DELETE_TODO: break;
            case REQUEST_MODIFY_TODO: break;

        }
        return appNetworkRequest;
    }

    private static AppNetworkRequest getRegisterAuthorRequest(REQUEST_TYPE requestType, APICallListener apiCallListener, Object requestBody){
        Author author = (Author)requestBody;
        return new RequestRegisterAuthor(apiCallListener, new GsonBuilder().create().toJson(author));
    }



    public abstract void makeBackEndRequest();

}
