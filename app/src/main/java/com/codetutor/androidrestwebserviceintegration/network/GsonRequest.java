package com.codetutor.androidrestwebserviceintegration.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends JsonRequest<T> {

    private static final String TAG = GsonRequest.class.getSimpleName();

    public enum REQ_TYPE{
        REGISTER,
        LOGIN,
        ADD_TODO,
        GET_TODOS,
        MODIFY_TODOS,
        DELETE_TODO,
        LOGOUT
    }

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Listener<T> listener;


    private static REQ_TYPE previousRequest = null;
    private REQ_TYPE currentRequest;

    public void setCurrentRequest(REQ_TYPE currentRequest){
        this.currentRequest=currentRequest;
    }

    private GsonRequest(int requestType, String url,String requestBody, Class<T> clazz, Map<String, String> headers, Listener<T> listener, Response.ErrorListener errorListener) {
        super(requestType,url,requestBody,listener,errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    public static <T> GsonRequest<T> getGsonRequest(REQ_TYPE requestType,String requestBody, Class<T> clazz, Listener<T> listener, Response.ErrorListener errorListener){

        int httpRequestType=0;
        String url=null;
        boolean shouldCache=false;

        Map<String, String> headers =new HashMap<String, String>();
        headers.put("Content-Type","application/json");

        switch (requestType){
            case GET_TODOS: httpRequestType = Method.GET;
                            if(previousRequest!=null && previousRequest.equals(REQ_TYPE.GET_TODOS)){
                                shouldCache=true;
                            }else{
                                shouldCache=false;
                            }
                            url = RestAPIs.getBaseUrl()+ ToDoAppRestAPI.getToDoItem+"/"+ AppConfig.getSavedSuccessfulAuthor().getAuthorEmailId();
                            headers.put("token",AppConfig.getSessionTokenValue());
                            break;
            case MODIFY_TODOS: httpRequestType = Method.PUT;
                               url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.modifyToDoUrl;
                               headers.put("token",AppConfig.getSessionTokenValue());
                               break;
            case DELETE_TODO: httpRequestType = Method.DELETE;
                              shouldCache=false;
                              url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.deleteToDo+"/"+AppConfig.getToBeDeletedToDoId();
                              headers.put("token",AppConfig.getSessionTokenValue());
                              break;
            case REGISTER: httpRequestType = Method.POST;
                            shouldCache=false;
                            url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.registerAuthor;
                            break;
            case LOGIN: httpRequestType = Method.POST;
                        shouldCache=false;
                        url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.login; break;
            case ADD_TODO: httpRequestType = Method.POST;
                           shouldCache=false;
                           url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.addToDoItem;
                           headers.put("token",AppConfig.getSessionTokenValue());
                           break;
            case LOGOUT: httpRequestType = Method.POST;
                         shouldCache=false;
                         url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.logout;
                         break;
        }

        GsonRequest<T> gsonRequest = new GsonRequest(httpRequestType, url,requestBody, clazz, headers, listener, errorListener);
        gsonRequest.setCurrentRequest(requestType);
        gsonRequest.setShouldCache(shouldCache);
        previousRequest=requestType;
        return gsonRequest;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public void addMarker(String tag) {
        Log.i(TAG,currentRequest.name()+ " Logging the tag: "+tag);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
        if (cacheEntry == null) {
            cacheEntry = new Cache.Entry();
        }
        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
        long now = System.currentTimeMillis();
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;
        cacheEntry.data = response.data;
        cacheEntry.softTtl = softExpire;
        cacheEntry.ttl = ttl;
        String headerValue;
        headerValue = response.headers.get("Date");
        if (headerValue != null) {
            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        headerValue = response.headers.get("Last-Modified");
        if (headerValue != null) {
            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        cacheEntry.responseHeaders = response.headers;

        try {
            String json = new String(cacheEntry.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, clazz), cacheEntry);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }
}
