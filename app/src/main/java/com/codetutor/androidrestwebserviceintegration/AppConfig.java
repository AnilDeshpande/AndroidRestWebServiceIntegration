package com.codetutor.androidrestwebserviceintegration;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.google.gson.GsonBuilder;

/**
 * Created by anildeshpande on 5/2/18.
 */

public class AppConfig extends Application {

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public static API_ENDPOINTS selectedEndPoint;
    public boolean isEmulator;

    static Context context;

    public static enum API_ENDPOINTS{
        localhost, remote
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("appprefrences.xml",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isEmulator = Util.isEmulator();
        context = getApplicationContext();

    }

    public static void saveUserName(String username){
        editor.putString("username",username);
        editor.commit();
    }

    public static void saveSuccessfulLoginUser(String jsonString){
        editor.putString("user",jsonString);
        editor.commit();
    }

    public static Author getSavedSuccessfulAuthor(){
        Author author = new GsonBuilder().create().fromJson(sharedPreferences.getString("user",null), Author.class);
        return author;
    }

    public static void saveSessionTokenValue(String token){
        editor.putString("token",token);
        editor.commit();
    }

    public static String getSessionTokenValue(String token){
        return sharedPreferences.getString("token",null);
    }

    public static  String getSavedUserName(){
        return sharedPreferences.getString("username",null);
    }

    public static void savePassword(String password){
        editor.putString("password",password);
        editor.commit();
    }

    public static String getSavedPassword(){
        return sharedPreferences.getString("password",null);
    }


    public static String getSessionId(){
        return sharedPreferences.getString("sessionId",null);
    }

    public static void saveSessionId(String sessionId){
        editor.putString("sessionId",sessionId);
        editor.commit();
    }

    public static void setServerEndPointPreference(boolean endPoint){
        editor.putBoolean("endpoint",endPoint);
        editor.commit();
    }

    public static boolean getSeverEndPointPreference(){
        return sharedPreferences.getBoolean("endpoint", true);
    }

    public static Context getContext(){
        return context;
    }

}
