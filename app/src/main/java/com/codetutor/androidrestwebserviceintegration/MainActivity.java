package com.codetutor.androidrestwebserviceintegration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.codetutor.androidrestwebserviceintegration.network.ToDoAppRestAPI;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private  static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Util.isAppOnLine(getApplicationContext())){
            Thread thread=new Thread(registerAuthor);
            thread.start();
        }

    }

    Runnable registerAuthor = new Runnable() {

        @Override
        public void run() {
            HttpURLConnection httpURLConnection=null;
            try{
                String response=null;
                URL url = new URL(ToDoAppRestAPI.baseUrl+ToDoAppRestAPI.registerAuthor);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(2000);
                httpURLConnection.setConnectTimeout(4000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type","application/json");

                Author author=new Author(0,"Anil","anildesh82@gmail.com","anil");
                JSONObject authorJsonObject = new JSONObject();
                authorJsonObject.put("authorEmailId",author.getAuthorEmailId());
                authorJsonObject.put("authorName",author.getAuthorName());
                authorJsonObject.put("authorPassword",author.getAuthorPassword());

                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(authorJsonObject.toString());
                dataOutputStream.flush();
                dataOutputStream.close();

                httpURLConnection.connect();

                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == 201){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line=bufferedReader.readLine())!=null){
                        stringBuilder.append(line);
                    }
                    response = stringBuilder.toString();
                }
            Log.i(TAG,response);

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }

        }
    };




}
