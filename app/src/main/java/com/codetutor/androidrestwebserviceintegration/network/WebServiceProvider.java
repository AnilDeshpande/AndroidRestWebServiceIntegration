package com.codetutor.androidrestwebserviceintegration.network;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

public class WebServiceProvider {

    private RequestQueue requestQueue;

    public static WebServiceProvider instance;

    private WebServiceProvider(Context context){
        //requestQueue = Volley.newRequestQueue(context);

        Cache cache = new DiskBasedCache(context.getCacheDir(),1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache,network);
        requestQueue.start();
    }

    public static WebServiceProvider getInstance(Context context) {
        if(instance==null){
            instance = new WebServiceProvider(context);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
