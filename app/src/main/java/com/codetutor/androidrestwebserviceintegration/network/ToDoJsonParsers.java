package com.codetutor.androidrestwebserviceintegration.network;

import android.util.Log;

import com.codetutor.androidrestwebserviceintegration.restbean.Author;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anildeshpande on 4/16/18.
 */

public class ToDoJsonParsers {

    private static final String JSON_KEY_TODO_ID = "id";
    private static final String JSON_KEY_TODO_DATE = "date";
    private static final String JSON_KEY_TODO_AUTHOR_EMAIL = "authorEmailId";
    private static final String JSON_KEY_TODO_PLACE = "place";
    private static final String JSON_KEY_TODO_STRING = "todoString";

    private static final String JSON_KEY_AUTHOR_ID = "id";
    private static final String JSON_KEY_AUTHOR_EMAIL_ID = "authorEmailId";
    private static final String JSON_KEY_AUTHOR_NAME = "authorName";
    private static final String JSON_KEY_AUTHOR_PASSWORD = "authorPassword";



    public static final String TAG = ToDoJsonParsers.class.getSimpleName();

    public static Author getAuthor(JSONObject jsonObject){
        Author author = new Author();
        try{
            author.setAuthorId(jsonObject.getLong(JSON_KEY_AUTHOR_ID));
            author.setAuthorEmailId(jsonObject.getString(JSON_KEY_AUTHOR_EMAIL_ID));
            author.setAuthorName(jsonObject.getString(JSON_KEY_AUTHOR_NAME));
            author.setAuthorPassword(jsonObject.getString(JSON_KEY_AUTHOR_PASSWORD));
        }catch (JSONException e){
            Log.i(TAG,e.getMessage());
        }
        return author;
    }

    public static ToDoItem getToDoItem(JSONObject jsonObject){
        ToDoItem item = new ToDoItem();
        try{
            item.setAuthorEmailId(jsonObject.getString(JSON_KEY_TODO_AUTHOR_EMAIL));
            item.setDate(jsonObject.getLong(JSON_KEY_TODO_DATE));
            item.setId(jsonObject.getLong(JSON_KEY_TODO_ID));
            item.setPlace(jsonObject.getString(JSON_KEY_TODO_PLACE));
            item.setTodoString(jsonObject.getString(JSON_KEY_TODO_STRING));
        }catch (JSONException e){
            Log.i(TAG,e.getMessage());
        }

        return  item;
    }

    public static List<ToDoItem> getToDoArray(JSONArray jsonArray){
        List<ToDoItem> toDoItems = new ArrayList<ToDoItem>();
        try{
            for(int i=0;i<jsonArray.length();i++){
                toDoItems.add(getToDoItem(jsonArray.getJSONObject(i)));
            }
        }catch (JSONException  e){
            Log.i(TAG,e.getMessage());
        }
        return toDoItems;
    }


    public static JSONObject getJsonAuthor(Author author){
        JSONObject authorJsonObject = new JSONObject();
        try{
            authorJsonObject.put(JSON_KEY_AUTHOR_EMAIL_ID,author.getAuthorEmailId());
            authorJsonObject.put(JSON_KEY_AUTHOR_NAME,author.getAuthorName());
            authorJsonObject.put(JSON_KEY_AUTHOR_PASSWORD,author.getAuthorPassword());
            authorJsonObject.put(JSON_KEY_AUTHOR_ID, author.getAuthorId());
        }catch (JSONException e){
            Log.i(TAG, e.getMessage());
        }
        return authorJsonObject;
    }

    public static JSONObject getJsonToDoItem(ToDoItem toDoItem){
        JSONObject toDoItemJsonObject = new JSONObject();
        try{
            toDoItemJsonObject.put(JSON_KEY_TODO_ID,toDoItem.getId());
            toDoItemJsonObject.put(JSON_KEY_TODO_STRING,toDoItem.getTodoString());
            toDoItemJsonObject.put(JSON_KEY_TODO_DATE,toDoItem.getDate());
            toDoItemJsonObject.put(JSON_KEY_TODO_PLACE, toDoItem.getPlace());
            toDoItemJsonObject.put(JSON_KEY_TODO_AUTHOR_EMAIL, toDoItem.getAuthorEmailId());
        }catch (JSONException e){
            Log.i(TAG, e.getMessage());
        }
        return toDoItemJsonObject;
    }

    public static JSONArray getJsonToDoArray(List<ToDoItem> toDoItems){
        JSONArray jsonArray = new JSONArray();
        for(ToDoItem item: toDoItems){
            JSONObject jsonObject = getJsonToDoItem(item);
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
