package com.codetutor.androidrestwebserviceintegration.restbean;

import java.io.Serializable;

public class LoginToken implements Serializable{

    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
