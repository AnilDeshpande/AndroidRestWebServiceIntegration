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

import com.codetutor.androidrestwebserviceintegration.BuildConfig;
import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.APICallListener;
import com.codetutor.androidrestwebserviceintegration.network.AppNetworkRequest;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Author;

import java.lang.ref.WeakReference;

/**
 * Created by anildeshpande on 2/21/18.
 */

public class RegisterDialogFragment extends DialogFragment implements View.OnClickListener, APICallListener{

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
        try{
            switch (view.getId()){
                case R.id.buttonRegister: register();break;
                case R.id.buttonCancel: dismiss();break;
            }
        }catch (Exception e){
             Log.i(TAG,e.getMessage());
        }

    }

    private void register() throws Exception{
        String userName=editTextUserName.getText().toString();
        String emailId = editTextEmailId.getText().toString();
        String password = editTextPassword.getText().toString();
        author= new Author(0,userName.toString(),emailId,password);

        if(Util.isAppOnLine(contextReference.get())){
            progressBar.setVisibility(View.VISIBLE);
            AppNetworkRequest appNetworkRequest = AppNetworkRequest.getReqestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_REGISTER_AUTHOR, this,author);
            appNetworkRequest.makeBackEndRequest();
        }
    }

    @Override
    public void onCallBackSuccess(Object o) {
        progressBar.setVisibility(View.INVISIBLE);
        try{
            Author author = (Author)o;
            if(registrationListener!=null){
                registrationListener.onRegistrationSuccess(author);
            }
            Toast.makeText(getActivity(),getString(R.string.regisrtation_successful),Toast.LENGTH_LONG).show();
        }catch (ClassCastException e){
            Error error = (Error)o;
            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
        }

        dismiss();
    }

    @Override
    public void onCallBackFailure(String message) {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }
}
