package com.codetutor.androidrestwebserviceintegration;

import android.app.Application;
import android.content.SharedPreferences;

import com.codetutor.androidrestwebserviceintegration.network.APIServiceProvider;
import com.codetutor.androidrestwebserviceintegration.network.RestAPIs;
import com.codetutor.androidrestwebserviceintegration.network.Util;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by anildeshpande on 5/2/18.
 */

public class AppConfig extends Application {

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public static API_ENDPOINTS selectedEndPoint;
    public boolean isEmulator;

    private static APIServiceProvider apiServiceProvider;

    public static enum API_ENDPOINTS{
        localhost, remote
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("appprefrences.xml",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isEmulator = Util.isEmulator();
        apiServiceProvider = APIServiceProvider.getApiServiceProvider(RestAPIs.getBaseUrl(),5000,5000, HttpLoggingInterceptor.Level.BODY);

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

    public static void setServerEndPointPreference(boolean endPoint){
        editor.putBoolean("endpoint",endPoint);
        editor.commit();
    }

    public static boolean getSeverEndPointPreference(){
        return sharedPreferences.getBoolean("endpoint", true);
    }

    public static APIServiceProvider getApiServiceProvider(){
        return apiServiceProvider;
    }


}
