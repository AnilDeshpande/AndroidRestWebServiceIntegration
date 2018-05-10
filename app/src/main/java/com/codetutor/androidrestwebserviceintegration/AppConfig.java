package com.codetutor.androidrestwebserviceintegration;

import android.app.Application;
import android.content.SharedPreferences;

import com.codetutor.androidrestwebserviceintegration.network.Util;

/**
 * Created by anildeshpande on 5/2/18.
 */

public class AppConfig extends Application {

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public static API_ENDPOINTS selectedEndPoint;
    public boolean isEmulator;

    public static enum API_ENDPOINTS{
        localhost, remote
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("appprefrences.xml",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isEmulator = Util.isEmulator();

    }

    public static void saveUserName(String username){
        editor.putString("username",username);
        editor.commit();
    }

    public static  String getSavedUserName(){
        return sharedPreferences.getString("username",null);
    }


    public static String getSessionId(){
        return sharedPreferences.getString(getSavedUserName(),null);
    }

    public static void saveSessionId(String sessionId){
        editor.putString(getSavedUserName(),sessionId);
        editor.commit();
    }

    public static void setServerEndPointPreference(boolean endPoint){
        editor.putBoolean("endpoint",endPoint);
        editor.commit();
    }

    public static boolean getSeverEndPointPreference(){
        return sharedPreferences.getBoolean("endpoint", true);
    }

}
