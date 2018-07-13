package com.codetutor.androidrestwebserviceintegration.ui.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.APIServiceProvider;
import com.codetutor.androidrestwebserviceintegration.network.ToDoAppRestAPI;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.codetutor.androidrestwebserviceintegration.restbean.LoginToken;
import com.codetutor.androidrestwebserviceintegration.ui.dialogs.RegisterDialogFragment;
import com.google.gson.Gson;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener, RegisterDialogFragment.RegistrationListener{

    private  static String TAG = MainActivity.class.getSimpleName();

    TextView textViewRegister;
    Switch switchEndPoint;
    EditText editTextUserName, editTextPassword;
    Button buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        if(AppConfig.getSavedUserName()!=null){
            editTextUserName.setText(AppConfig.getSavedUserName());
        }

        textViewRegister = (TextView)findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(this);

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        switchEndPoint = (Switch)findViewById(R.id.switchEndPoint);
        if(AppConfig.getSeverEndPointPreference()){
            switchEndPoint.setChecked(true);
        }else{
            switchEndPoint.setChecked(false);
        }

        switchEndPoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppConfig.selectedEndPoint = AppConfig.API_ENDPOINTS.remote;
                    AppConfig.setServerEndPointPreference(true);
                }else{
                    AppConfig.selectedEndPoint = AppConfig.API_ENDPOINTS.localhost;
                    AppConfig.setServerEndPointPreference(true);
                }
            }
        });

        switchEndPoint.setTextOff("localhost");
        switchEndPoint.setTextOn("Remote");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textViewRegister: showRegisterDialog(); break;
            case R.id.buttonLogin: login();break;
        }
    }

    private void showRegisterDialog(){
        DialogFragment dialogFragment = new RegisterDialogFragment();
        ((RegisterDialogFragment)dialogFragment).setRegistrationListener(this);
        dialogFragment.show(getFragmentManager(),"register");
    }

    @Override
    public void onRegistrationSuccess(Author author) {
        AppConfig.saveUserName(author.getAuthorName());
        AppConfig.saveSuccessfulLoginUser(new Gson().toJson(author).toString());
        editTextUserName.setText(AppConfig.getSavedUserName());
    }

    private void login(){
        String password = editTextPassword.getText().toString();
        Author author = AppConfig.getSavedSuccessfulAuthor();
        author.setAuthorPassword(password);
        if(Util.isAppOnLine(this)){
            showBusyDialog("Logging In");
            AppConfig.getApiServiceProvider().loginAuthor(author).enqueue(new Callback<LoginToken>() {
                @Override
                public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                    dismissBusyDialog();
                    AppConfig.saveSessionTokenValue(response.body().getToken());
                    startLoginActivity();
                }

                @Override
                public void onFailure(Call<LoginToken> call, Throwable t) {
                    dismissBusyDialog();
                    toastMessage(t.getMessage(), Toast.LENGTH_SHORT);
                }
            });

        }
    }

    private void startLoginActivity(){
        Intent loginIntent = new Intent(this,HomeActivity.class);
        startActivity(loginIntent);
    }
}
