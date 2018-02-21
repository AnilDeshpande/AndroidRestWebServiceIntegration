package com.codetutor.androidrestwebserviceintegration.ui.activities;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.ToDoAppRestAPI;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.codetutor.androidrestwebserviceintegration.ui.dialogs.RegisterDialogFragment;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private  static String TAG = MainActivity.class.getSimpleName();

    TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewRegister = (TextView)findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textViewRegister: showRegisterDialog(); break;
        }
    }

    private void showRegisterDialog(){
        DialogFragment dialogFragment = new RegisterDialogFragment();
        dialogFragment.show(getFragmentManager(),"register");

    }

}
