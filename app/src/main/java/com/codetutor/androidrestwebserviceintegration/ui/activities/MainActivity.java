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

import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.APICallListener;
import com.codetutor.androidrestwebserviceintegration.network.AppNetworkRequest;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.codetutor.androidrestwebserviceintegration.restbean.Error;
import com.codetutor.androidrestwebserviceintegration.restbean.LoginToken;
import com.codetutor.androidrestwebserviceintegration.ui.dialogs.RegisterDialogFragment;
import com.google.gson.Gson;

public class MainActivity extends BaseActivity implements View.OnClickListener, RegisterDialogFragment.RegistrationListener, APICallListener {

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

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

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
        if(Util.isAppOnLine(this)){
            showBusyDialog("Logging In");
            AppNetworkRequest appNetworkRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_LOGIN_AUTHOR, this,author);
            appNetworkRequest.makeBackEndRequest();
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

    @Override
    public void onCallBackFailure(String message) {
        dismissBusyDialog();
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCallBackSuccess(Object o) {
        dismissBusyDialog();
        try{
            AppConfig.saveSessionTokenValue(((LoginToken)o).getToken());
            Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        }catch (ClassCastException e){
            Error error = (Error)o;
            Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show();
        }



    }
}
