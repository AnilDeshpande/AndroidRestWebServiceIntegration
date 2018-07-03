package com.codetutor.androidrestwebserviceintegration.network;

import com.codetutor.androidrestwebserviceintegration.restbean.Author;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
}
