package com.codetutor.androidrestwebserviceintegration.network;

import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.codetutor.androidrestwebserviceintegration.restbean.LoginToken;
import com.codetutor.androidrestwebserviceintegration.restbean.ModifyToDoPayloadBean;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIInterface {

    @POST(ToDoAppRestAPI.registerAuthor)
    Call<Author> registerAuthor(@Body Author author);

    @POST(ToDoAppRestAPI.login)
    Call<LoginToken> loginAuthor(@Body Author author);

    @POST(ToDoAppRestAPI.logout)
    Call<ResponseBody> logoutAuthor(@Body Author author);

    @POST(ToDoAppRestAPI.addToDoItem)
    Call<ToDoItem> addToDoItem(@Header(value = "token") String token, @Body ToDoItem toDoItem);

    @GET(ToDoAppRestAPI.getToDoItem+"{authorEmailId}/")
    Call<List<ToDoItem>> getToDoList(@Path(value = "authorEmailId") String authorEmailId, @Header(value = "token") String token);

    @HTTP(method = "DELETE", path = ToDoAppRestAPI.deleteToDo, hasBody = true)
    Call<ResponseBody> deleteToDo(@Header(value = "token") String token, @Body ToDoItem toDoItem);

    @PUT(ToDoAppRestAPI.modifyToDoUrl)
    Call<ToDoItem> modifyToDoItem(@Header(value = "token") String token, @Body ModifyToDoPayloadBean modifyToDoPayloadBean);

}
