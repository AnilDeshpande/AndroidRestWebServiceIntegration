package com.codetutor.androidrestwebserviceintegration.network;

import android.os.Handler;

import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public abstract class AppNetworkRequest implements Runnable{

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
    public static final String TOKEN = "token";
    public static final String JSON_CONTENT_TYPE  = "application/json";

    public static final int CONNECT_TIMEOUT=5000;
    public static final int READ_TIMEOUT=5000;

    Handler handler;



    APICallListener apiCallListener;

    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .build();;

    Object responseObject;



    AppNetworkRequest(APICallListener apiCallListener){
        handler = new Handler(AppConfig.getContext().getMainLooper());
        this.apiCallListener=apiCallListener;

    }

    public static AppNetworkRequest getReqestInstance(REQUEST_TYPE requestType, APICallListener apiCallListener,Object requestBody){

        AppNetworkRequest appNetworkRequest=null;


        switch (requestType){
            case REQUEST_REGISTER_AUTHOR:
                appNetworkRequest = getRegisterAuthorRequest(REQUEST_TYPE.REQUEST_LOGIN_AUTHOR, apiCallListener, requestBody);
                break;
            case REQUEST_LOGIN_AUTHOR:
                appNetworkRequest = getLoginAuthorRequest(REQUEST_TYPE.REQUEST_LOGIN_AUTHOR, apiCallListener, requestBody);
                break;
            case REQUEST_LOGOUT_AUTHOR:
                appNetworkRequest = getSingoutRequest(REQUEST_TYPE.REQUEST_LOGOUT_AUTHOR, apiCallListener, requestBody);
                break;
            case REQUEST_ADD_TODO_ITEM: break;
            case REQUEST_GET_TODOS:
                appNetworkRequest = getToDosRequest(REQUEST_TYPE.REQUEST_GET_TODOS,apiCallListener,null);
                break;
            case REQUEST_DELETE_TODO: break;
            case REQUEST_MODIFY_TODO: break;

        }
        return appNetworkRequest;
    }

    private static AppNetworkRequest getRegisterAuthorRequest(REQUEST_TYPE requestType, APICallListener apiCallListener, Object requestBody){
        Author author = (Author)requestBody;
        return new RequestRegisterAuthor(apiCallListener, new GsonBuilder().create().toJson(author));
    }

    private static AppNetworkRequest getLoginAuthorRequest(REQUEST_TYPE requestType, APICallListener apiCallListener, Object requestObject){
        Author author = (Author)requestObject;
        return new RequestAuthorLogin(apiCallListener, new GsonBuilder().create().toJson(author));
    }

    private static AppNetworkRequest getSingoutRequest(REQUEST_TYPE requestType, APICallListener apiCallListener, Object requestObject){
        Author author = (Author) requestObject;
        return  new RequestSignOut(apiCallListener,new GsonBuilder().create().toJson(author));
    }

    private static AppNetworkRequest getToDosRequest(REQUEST_TYPE requestType, APICallListener apiCallListener, Object requestObject){
        return new RequestGetToDos(apiCallListener,null);
    }



    public abstract void makeBackEndRequest();

}
