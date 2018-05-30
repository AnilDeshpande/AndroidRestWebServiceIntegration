package com.codetutor.androidrestwebserviceintegration.ui.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codetutor.androidrestwebserviceintegration.R;

import java.lang.ref.WeakReference;

public class CustomProgressDialog extends DialogFragment {

    private static final String TAG = CustomProgressDialog.class.getSimpleName();

    WeakReference<Context> contextReference;

    View rootView;

    String message;

    TextView textViewMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.custom_progress_dialog_fragment,container);
        initUI();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contextReference = new WeakReference<Context>(getActivity());

    }

    private void initUI(){
        textViewMessage = (TextView)rootView.findViewById(R.id.textViewMessage);
        message = getArguments().getString("message","Loading");
        textViewMessage.setText(message);
    }
}
