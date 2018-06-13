package com.codetutor.androidrestwebserviceintegration.restbean;

public class Success {
    private int successCode;
    private String successMessage;

    public Success(int successCode, String successMessage) {

        this.successCode = successCode;
        this.successMessage = successMessage;
    }


    public int getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(int successCode) {
        this.successCode = successCode;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }



}
