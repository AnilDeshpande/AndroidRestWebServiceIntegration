package com.codetutor.androidrestwebserviceintegration.ui.dialogs;

import android.app.Dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.RestAPIs;
import com.codetutor.androidrestwebserviceintegration.network.ToDoAppRestAPI;
import com.codetutor.androidrestwebserviceintegration.network.ToDoJsonParsers;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by anildeshpande on 2/21/18.
 */

public class RegisterDialogFragment extends DialogFragment implements View.OnClickListener{

    private static final String TAG = RegisterDialogFragment.class.getCanonicalName();

    WeakReference<Context> contextReference;

    OkHttpClient okHttpClient;

    View rootView;

    EditText editTextUserName,editTextEmailId,editTextPassword;
    Button buttonRegister, buttonCancel;

    ProgressBar progressBar;
    Author author;

    private RegistrationListener registrationListener;

    public interface RegistrationListener{
        public void onRegistrationSuccess(Author author);
    }

    public void setRegistrationListener(RegistrationListener registrationListener){
        this.registrationListener = registrationListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Holo_Light_Dialog);
        //setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
        //setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Dialog);
        //setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
        //setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Holo_Light);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().setTitle("Register");
        rootView = inflater.inflate(R.layout.fragment_dialog_register,container);
        initUI();
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog= super.onCreateDialog(savedInstanceState);
        dialog.setTitle(getString(R.string.string_register));
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contextReference = new WeakReference<Context>(getActivity());
    }

    private void initUI(){
        editTextUserName = (EditText)rootView.findViewById(R.id.editTextUserName);
        editTextEmailId = (EditText)rootView.findViewById(R.id.editTextEmailId);
        editTextPassword = (EditText)rootView.findViewById(R.id.editTextPassword);

        buttonRegister = (Button)rootView.findViewById(R.id.buttonRegister);
        buttonCancel = (Button)rootView.findViewById(R.id.buttonCancel);

        buttonRegister.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View view) {
        try{
            switch (view.getId()){
                case R.id.buttonRegister: login();break;
                case R.id.buttonCancel: dismiss();break;
            }
        }catch (Exception e){
             Log.i(TAG,e.getMessage());
        }

    }

    private void login() throws Exception{
        String userName=editTextUserName.getText().toString();
        String emailId = editTextEmailId.getText().toString();
        String password = editTextPassword.getText().toString();
        author= new Author(0,userName.toString(),emailId,password);

        if(Util.isAppOnLine(contextReference.get())){
            progressBar.setVisibility(View.VISIBLE);
            okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(5000, TimeUnit.MILLISECONDS);

            JSONObject authorJsonObject = new JSONObject();

            authorJsonObject.put("authorEmailId",author.getAuthorEmailId());
            authorJsonObject.put("authorName",author.getAuthorName());
            authorJsonObject.put("authorPassword",author.getAuthorPassword());


            Request request =  new Request.Builder().url(RestAPIs.getBaseUrl()+ToDoAppRestAPI.registerAuthor)
                    .addHeader("Content-Type","application/json")
                    .post(RequestBody.create(MediaType.parse("application/json"), authorJsonObject.toString()))
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i(TAG,e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.i(TAG,""+response.body().string());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });



        }
    }


    private void toastMessage(final String message){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(contextReference.get(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
