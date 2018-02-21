package com.codetutor.androidrestwebserviceintegration.network;

/**
 * Created by anildeshpande on 2/21/18.
 */

public interface ToDoAppRestAPI {
    String baseUrl="http://10.0.2.2:8080/dotolist/webapi";

    String registerAuthor="/authors";

    String login = registerAuthor+"/login";

}
