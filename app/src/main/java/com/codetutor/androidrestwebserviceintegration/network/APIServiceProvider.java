package com.codetutor.androidrestwebserviceintegration.network;

import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.codetutor.androidrestwebserviceintegration.restbean.LoginToken;
import com.codetutor.androidrestwebserviceintegration.restbean.ModifyToDoPayloadBean;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoItem;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServiceProvider {

    private static APIServiceProvider apiServiceProvider;

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    APIInterface apiInterface;

    private APIServiceProvider(String baseUrl, long readTimeout, long connectTimeout, HttpLoggingInterceptor.Level logLevel){
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(logLevel);

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(RestAPIs.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        apiInterface = retrofit.create(APIInterface.class);
    }

    public static APIServiceProvider getApiServiceProvider(String baseUrl, long readTimeout, long connectTimeout, HttpLoggingInterceptor.Level logLevel){
        if(apiServiceProvider==null){
            apiServiceProvider=new APIServiceProvider(baseUrl,readTimeout,connectTimeout,logLevel);
        }
        return  apiServiceProvider;
    }

    public Call<Author> getRegisterAuthorApi(Author author){
        return apiInterface.registerAuthor(author);
    }

    public Call<LoginToken> loginAuthor(Author author){
        return apiInterface.loginAuthor(author);
    }

    public Call<ResponseBody> logoutAuthor(Author author){
        return apiInterface.logoutAuthor(author);
    }

    public Call<ToDoItem> addToDoItem(ToDoItem toDoItem){
        return apiInterface.addToDoItem(AppConfig.getSessionTokenValue(),toDoItem);
    }

    public Call<List<ToDoItem>> getToDoList(){
        return apiInterface.getToDoList(AppConfig.getSavedSuccessfulAuthor().getAuthorEmailId(),AppConfig.getSessionTokenValue());
    }

    public Call<ResponseBody> deleteToDo(ToDoItem toDoItem){
        return apiInterface.deleteToDo(AppConfig.getSessionTokenValue(),toDoItem);
    }

    public Call<ResponseBody> modifyToDoItem(ModifyToDoPayloadBean modifyToDoPayloadBean){
        return apiInterface.modifyToDoItem(AppConfig.getSessionTokenValue(),modifyToDoPayloadBean);
    }


}
