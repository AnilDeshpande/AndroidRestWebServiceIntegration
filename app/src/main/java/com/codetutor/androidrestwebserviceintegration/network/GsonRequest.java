package com.codetutor.androidrestwebserviceintegration.network;

import com.android.volley.AuthFailureError;
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

    public GsonRequest<T> getGsonRequest(REQ_TYPE requestType,String requestBody, Class<T> clazz, Listener<T> listener, Response.ErrorListener errorListener){
        int httpRequestType=0;
        String url=null;

        Map<String, String> headers =new HashMap<String, String>();
        headers.put("Content-Type","application/json");

        switch (requestType){
            case GET_TODOS: httpRequestType = Method.GET;
                            url = RestAPIs.getBaseUrl()+ ToDoAppRestAPI.getToDoItem+"/"+ AppConfig.getSavedSuccessfulAuthor().getAuthorEmailId();
                            headers.put("token",AppConfig.getSessionTokenValue());
                            break;
            case MODIFY_TODOS: httpRequestType = Method.PUT;
                               url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.login;
                               break;
            case DELETE_TODO: httpRequestType = Method.DELETE;
                              url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.deleteToDo; break;
            case REGISTER: httpRequestType = Method.POST;
                           url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.registerAuthor; break;
            case LOGIN: httpRequestType = Method.POST;
                        url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.login; break;
            case ADD_TODO: httpRequestType = Method.POST;
                           url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.addToDoItem;
                           break;
            case LOGOUT: httpRequestType = Method.POST;
                         url = RestAPIs.getBaseUrl()+ToDoAppRestAPI.logout;
                         break;
        }

        GsonRequest<T> gsonRequest = new GsonRequest(httpRequestType, url,requestBody, clazz, headers, listener, errorListener);

        return gsonRequest;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
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
