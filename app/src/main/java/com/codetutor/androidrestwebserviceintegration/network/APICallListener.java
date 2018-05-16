package com.codetutor.androidrestwebserviceintegration.network;

public interface APICallListener {
    void onCallBackSuccess(Object o);
    void onCallBackFailure(String message);
}
