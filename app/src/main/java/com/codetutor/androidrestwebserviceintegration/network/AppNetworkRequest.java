package com.codetutor.androidrestwebserviceintegration.network;

import android.os.Handler;

import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.codetutor.androidrestwebserviceintegration.restbean.ModifyToDoPayloadBean;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoItem;
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

    Object responseObject;

    public static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .build();

    AppNetworkRequest(APICallListener apiCallListener){
        handler = new Handler(AppConfig.getContext().getMainLooper());
        this.apiCallListener=apiCallListener;

    }

    public static AppNetworkRequest getRequestInstance(REQUEST_TYPE requestType, APICallListener apiCallListener, Object requestBodyObject){

        AppNetworkRequest appNetworkRequest=null;

        switch (requestType){
            case REQUEST_REGISTER_AUTHOR:
                appNetworkRequest = getRegisterAuthorRequest(apiCallListener, requestBodyObject);
                break;
            case REQUEST_LOGIN_AUTHOR:
                appNetworkRequest = getLoginAuthorRequest(apiCallListener, requestBodyObject);
                break;
            case REQUEST_LOGOUT_AUTHOR:
                appNetworkRequest = getSingoutRequest(apiCallListener, requestBodyObject);
                break;
            case REQUEST_ADD_TODO_ITEM:
                appNetworkRequest = getAddToDoRequest(apiCallListener,requestBodyObject);
                break;
            case REQUEST_GET_TODOS:
                appNetworkRequest = getToDosRequest(apiCallListener,null);
                break;
            case REQUEST_DELETE_TODO:
                appNetworkRequest = getDeleteToDoRequest(apiCallListener,requestBodyObject);
                break;
            case REQUEST_MODIFY_TODO:
                appNetworkRequest = getModifyToDoRequest(apiCallListener,requestBodyObject);
                break;
        }
        return appNetworkRequest;
    }

    private static AppNetworkRequest getRegisterAuthorRequest(APICallListener apiCallListener, Object requestBodyObject){
        Author author = (Author)requestBodyObject;
        return new RequestRegisterAuthor(apiCallListener, new GsonBuilder().create().toJson(author));
    }

    private static AppNetworkRequest getLoginAuthorRequest(APICallListener apiCallListener, Object requestBodyObject){
        Author author = (Author)requestBodyObject;
        return new RequestAuthorLogin(apiCallListener, new GsonBuilder().create().toJson(author));
    }

    private static AppNetworkRequest getSingoutRequest(APICallListener apiCallListener, Object requestBodyObject){
        Author author = (Author) requestBodyObject;
        return  new RequestSignOut(apiCallListener,new GsonBuilder().create().toJson(author));
    }

    private static AppNetworkRequest getToDosRequest(APICallListener apiCallListener, Object requestBodyObject){
        return new RequestGetToDos(apiCallListener,null);
    }

    private static AppNetworkRequest getAddToDoRequest(APICallListener apiCallListener, Object requestBodyObject){
        ToDoItem item = (ToDoItem)requestBodyObject;
        return new RequestAddToDoItem(apiCallListener,new GsonBuilder().create().toJson(item));
    }

    private static AppNetworkRequest getDeleteToDoRequest(APICallListener apiCallListener, Object requestBodyObject){
        ToDoItem item = (ToDoItem)requestBodyObject;
        return new RequestDeleteToDo(apiCallListener,new GsonBuilder().create().toJson(item));
    }

    private static AppNetworkRequest getModifyToDoRequest(APICallListener apiCallListener, Object requestBodyObject){
        ModifyToDoPayloadBean modifyToDoPayloadBean = (ModifyToDoPayloadBean)requestBodyObject;
        return new RequestModifyToDo(apiCallListener,new GsonBuilder().create().toJson(modifyToDoPayloadBean));

    }

    public abstract void makeBackEndRequest();

}
