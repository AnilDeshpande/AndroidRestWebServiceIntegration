package com.codetutor.androidrestwebserviceintegration.network;

public interface APICallListener {
    void onCallBackSuccess(AppNetworkRequest.REQUEST_TYPE requestType, Object o);
    void onCallBackFailure(String message);
}
