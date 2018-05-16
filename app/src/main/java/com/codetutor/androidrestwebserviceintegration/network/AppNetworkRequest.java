package com.codetutor.androidrestwebserviceintegration.network;

import com.codetutor.androidrestwebserviceintegration.restbean.Author;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class AppNetworkRequest implements Runnable{

    APICallListener apiCallListener;

    public static enum REQUEST_TYPE{
        REQUEST_REGISTER_AUTHOR,
        REQUEST_LOGIN_AUTHOR,
        REQUEST_LOGOUT_AUTHOR,
        REQUEST_ADD_TODO_ITEM,
        REQUEST_GET_TODOS,
        REQUEST_DELETE_TODO,
        REQUEST_MODIFY_TODO

    }

    public static final int REQUEST_REGISTER_AUTHOR = 1;
    public static final int REQUEST_LOGIN_AUTHOR = 2;
    public static final int REQUEST_LOGOUT_AUTHOR = 3;
    public static final int REQUEST_ADD_TODO_ITEM = 4;
    public static final int REQUEST_GET_TODOS = 5;
    public static final int REQUEST_DELETE_TODO = 6;
    public static final int REQUEST_MODIFY_TODO = 7;


    public static final String CONTENT_TYPE = "Content-Type";
    public static final String JSON_CONTENT_TYPE  = "application/json";

    String url;
    Map<String,String> header = new HashMap<>();
    String requestBody;

    public static AppNetworkRequest getReqestInstance(REQUEST_TYPE requestType, APICallListener apiCallListener,Object request){

        AppNetworkRequest appNetworkRequest=null;

        try {
            switch (requestType){
                case REQUEST_REGISTER_AUTHOR:
                    Author author = (Author)request;
                    JSONObject authorJsonObject = new JSONObject();

                    authorJsonObject.put("authorEmailId",author.getAuthorEmailId());
                    authorJsonObject.put("authorName",author.getAuthorName());
                    authorJsonObject.put("authorPassword",author.getAuthorPassword());
                    appNetworkRequest = new RequestRegisterAuthor(apiCallListener, authorJsonObject);
                    break;

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return appNetworkRequest;

    }

    abstract void execute();

}
