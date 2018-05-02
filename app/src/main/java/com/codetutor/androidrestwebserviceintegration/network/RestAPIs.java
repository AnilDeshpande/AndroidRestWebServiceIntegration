package com.codetutor.androidrestwebserviceintegration.network;

import com.codetutor.androidrestwebserviceintegration.AppConfig;

/**
 * Created by anildeshpande on 5/2/18.
 */

public class RestAPIs {
    public static String getBaseUrl(){
        if(AppConfig.selectedEndPoint== AppConfig.API_ENDPOINTS.localhost){
            return ToDoAppRestAPI.baseLocalHostUrl;
        }else if (AppConfig.selectedEndPoint== AppConfig.API_ENDPOINTS.remote){
            return ToDoAppRestAPI.baseRemoteUrl;
        }else {
            return ToDoAppRestAPI.baseRemoteUrl;
        }
    }
}
