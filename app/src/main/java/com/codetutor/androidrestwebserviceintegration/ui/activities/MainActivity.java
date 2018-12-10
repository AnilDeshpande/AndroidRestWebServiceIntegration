package com.codetutor.androidrestwebserviceintegration.ui.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.GsonRequest;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.codetutor.androidrestwebserviceintegration.restbean.LoginToken;
import com.codetutor.androidrestwebserviceintegration.ui.dialogs.RegisterDialogFragment;
import com.google.gson.GsonBuilder;

public class MainActivity extends BaseActivity implements View.OnClickListener, RegisterDialogFragment.RegistrationListener{

    private  static String TAG = MainActivity.class.getSimpleName();

    TextView textViewRegister;
    Switch switchEndPoint;
    EditText editTextUserName, editTextPassword;

    Button buttoLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        buttoLogin = (Button)findViewById(R.id.buttonLogin);
        buttoLogin.setOnClickListener(this);

        if(AppConfig.getSavedUserName()!=null){
            editTextUserName.setText(AppConfig.getSavedUserName());
        }

        textViewRegister = (TextView)findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(this);

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
            case R.id.buttonLogin: login(); break;

        }
    }

    private void login(){

        String password = editTextPassword.getText().toString();
        Author author = AppConfig.getSavedSuccessfulAuthor();
        author.setAuthorPassword(password);

        if(Util.isAppOnLine(MainActivity.this)){
            showBusyDialog("Logging In");
            String jsonString = new GsonBuilder().create().toJson(author);
            GsonRequest<LoginToken> loginRequest = GsonRequest.getGsonRequest(GsonRequest.REQ_TYPE.LOGIN,jsonString, LoginToken.class,
                new Response.Listener<LoginToken>() {
                    @Override
                    public void onResponse(LoginToken response) {
                        dismissBusyDialog();
                        AppConfig.saveSessionTokenValue(response.getToken());
                        startLoginActivity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissBusyDialog();
                        toastMessage(error.getMessage(), Toast.LENGTH_SHORT);
                    }
                });

            AppConfig.getWebServiceProvider().addToRequestQueue(loginRequest);
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
        AppConfig.saveSuccessfulLoginUser(new GsonBuilder().create().toJson(author).toString());
        editTextUserName.setText(AppConfig.getSavedUserName());
    }

    private void startLoginActivity(){
        Intent loginIntent = new Intent(this,HomeActivity.class);
        startActivity(loginIntent);
    }
}
