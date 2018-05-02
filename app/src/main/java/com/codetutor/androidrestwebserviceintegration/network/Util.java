package com.codetutor.androidrestwebserviceintegration.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by anildeshpande on 2/21/18.
 */

public class Util {

    public static boolean isAppOnLine(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }
    }


}
