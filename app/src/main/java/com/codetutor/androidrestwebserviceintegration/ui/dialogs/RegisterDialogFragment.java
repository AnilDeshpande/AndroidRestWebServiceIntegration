package com.codetutor.androidrestwebserviceintegration.ui.dialogs;

import android.app.Dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.BuildConfig;
import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.GsonRequest;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by anildeshpande on 2/21/18.
 */

public class RegisterDialogFragment extends DialogFragment implements View.OnClickListener{

    private static final String TAG = RegisterDialogFragment.class.getCanonicalName();

    WeakReference<Context> contextReference;

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

        if(BuildConfig.DEBUG){
            editTextUserName.setText("AnilDeshpande");
            editTextEmailId.setText("AnilDeshpande@samplemail.com");
            editTextPassword.setText("AnilDeshpande");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonRegister: login();break;
            case R.id.buttonCancel: dismiss();break;
        }
    }

    private void login(){
        String userName=editTextUserName.getText().toString();
        String emailId = editTextEmailId.getText().toString();
        String password = editTextPassword.getText().toString();
        author= new Author(0,userName.toString(),emailId,password);

        if(Util.isAppOnLine(contextReference.get())){
            progressBar.setVisibility(View.VISIBLE);

            String jsonString = new GsonBuilder().create().toJson(author);

            GsonRequest<Author> registerRequest = GsonRequest.getGsonRequest(GsonRequest.REQ_TYPE.REGISTER,jsonString,Author.class,
                new Response.Listener<Author>() {
                    @Override
                    public void onResponse(Author response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        toastMessage("Registration Success");
                        if(registrationListener!=null){
                            registrationListener.onRegistrationSuccess(author);
                        }
                        dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        toastMessage(error.getMessage());
                    }
                });

            AppConfig.getWebServiceProvider().addToRequestQueue(registerRequest);
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
