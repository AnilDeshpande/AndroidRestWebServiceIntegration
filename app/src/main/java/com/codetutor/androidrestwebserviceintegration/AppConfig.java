package com.codetutor.androidrestwebserviceintegration;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by anildeshpande on 5/2/18.
 */

public class AppConfig extends Application {

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public static API_ENDPOINTS selectedEndPoint;

    public static enum API_ENDPOINTS{
        localhost, remote
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("appprefrences.xml",MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public static void saveUserName(String username){
        editor.putString("username",username);
        editor.commit();
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

}
