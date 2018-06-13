package com.codetutor.androidrestwebserviceintegration.network;

import com.codetutor.androidrestwebserviceintegration.restbean.Author;

import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {
    @FormUrlEncoded
    @POST(ToDoAppRestAPI.registerAuthor)
    Author registerAuthor(@Header("Content-Type") String contentType, @Body Author author);

}
