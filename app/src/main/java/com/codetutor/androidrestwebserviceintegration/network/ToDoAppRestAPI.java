package com.codetutor.androidrestwebserviceintegration.network;

import com.codetutor.androidrestwebserviceintegration.AppConfig;

/**
 * Created by anildeshpande on 2/21/18.
 */

public interface ToDoAppRestAPI {

    String baseLocalHostUrl = "http://10.0.2.2:8080/dotolist/webapi/";
    String baseRemoteUrl = "http://todolistmobileapp-env.ap-south-1.elasticbeanstalk.com/webapi/";



    String registerAuthor="authors/";

    String login = registerAuthor+"login/";

    String logout = registerAuthor+"signout/";

    String addToDoItem = "todolists/";

    String getToDoItem = addToDoItem+"/";

    String deleteToDo = addToDoItem;

    String modifyToDoUrl = addToDoItem;

}
