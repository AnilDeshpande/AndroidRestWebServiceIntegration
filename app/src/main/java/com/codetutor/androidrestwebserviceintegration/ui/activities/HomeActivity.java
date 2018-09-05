package com.codetutor.androidrestwebserviceintegration.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.GsonRequest;
import com.codetutor.androidrestwebserviceintegration.network.RestAPIs;
import com.codetutor.androidrestwebserviceintegration.network.ToDoAppRestAPI;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Error;
import com.codetutor.androidrestwebserviceintegration.restbean.LoginToken;
import com.codetutor.androidrestwebserviceintegration.restbean.ModifyToDoPayloadBean;
import com.codetutor.androidrestwebserviceintegration.restbean.Success;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoItem;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoListResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private EditText editTextNewToDoString, editTextToDoId, editTextNewToDo, editTextPlace;
    private TextView textViewToDos;
    private Button buttonAddToDo, buttonRemoveToDo, buttonModifyToDo;

    List<ToDoItem> toDoItems;

    ToDoItem itemToBeRemoved;

    ToDoItem itemToBeModified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        editTextNewToDoString=(EditText)findViewById(R.id.editTextNewToDoString);
        editTextToDoId=(EditText)findViewById(R.id.editTextToDoId);
        editTextNewToDo=(EditText)findViewById(R.id.editTextNewToDo);
        editTextPlace=(EditText)findViewById(R.id.editTextPlace);

        textViewToDos=(TextView)findViewById(R.id.textViewToDos);


        buttonAddToDo=(Button)findViewById(R.id.buttonAddToDo);
        buttonRemoveToDo=(Button)findViewById(R.id.buttonRemoveToDo);
        buttonModifyToDo=(Button)findViewById(R.id.buttonModifyToDo);

        buttonModifyToDo.setOnClickListener(this);
        buttonRemoveToDo.setOnClickListener(this);
        buttonAddToDo.setOnClickListener(this);

        getToDoItems();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAddToDo: addNewToDo(); break;
            case R.id.buttonRemoveToDo: removeToDo(); break;
            case R.id.buttonModifyToDo: modifyToDo(); break;
            default: break;
        }
    }

    private void setNewList(List<ToDoItem> toDoItems){
        StringBuilder stringBuilder = new StringBuilder();
        for(ToDoItem item: toDoItems){
            stringBuilder.append(item.toString()+"\n");
        }
        textViewToDos.setText(stringBuilder.toString());
    }

    private void addNewToDo(){
        String stringToDoString = editTextNewToDoString.getText().toString();
        String stringPlace = editTextPlace.getText().toString();

        ToDoItem item = new ToDoItem(0,stringToDoString, AppConfig.getSavedSuccessfulAuthor().getAuthorEmailId(),stringPlace);
        if(Util.isAppOnLine(getApplicationContext())){
            showBusyDialog("Adding ToDo");
            GsonRequest<ToDoItem> gsonRequest = GsonRequest.getGsonRequest(GsonRequest.REQ_TYPE.ADD_TODO, new GsonBuilder().create().toJson(item), ToDoItem.class,
                new Response.Listener<ToDoItem>() {
                    @Override
                    public void onResponse(ToDoItem response) {
                        dismissBusyDialog();
                        toDoItems.add(response);
                        refreshList();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissBusyDialog();
                        toastMessage(error.getMessage(),Toast.LENGTH_SHORT);
                    }
                });

            AppConfig.getWebServiceProvider().addToRequestQueue(gsonRequest);
        }else{
            toastMessage("Network Issue",Toast.LENGTH_SHORT);
        }

    }

    private void removeToDo(){
        long removiewId = Long.parseLong(editTextToDoId.getText().toString());
        itemToBeRemoved = getToDoItemById(removiewId);
        AppConfig.saveToBeDeletedToDoId(removiewId);
        if(itemToBeRemoved!=null){
            if(Util.isAppOnLine(getApplicationContext())){
                showBusyDialog("Deleting ToDo");
                GsonRequest gsonRequest = GsonRequest.getGsonRequest(GsonRequest.REQ_TYPE.DELETE_TODO, null, String.class,
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            dismissBusyDialog();
                            toDoItems.remove(itemToBeRemoved);
                            clearEditTexsts();
                            refreshList();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissBusyDialog();
                            toastMessage(error.getMessage(),Toast.LENGTH_SHORT);
                        }
                    });
                AppConfig.getWebServiceProvider().addToRequestQueue(gsonRequest);
            }else{
                toastMessage("Network Issue",Toast.LENGTH_SHORT);
            }
        }else{
            toastMessage("Please select existing id", Toast.LENGTH_LONG);
        }



    }

    private void modifyToDo(){
        int id=Integer.parseInt(editTextToDoId.getText().toString());
        String newToDOString=editTextNewToDo.getText().toString();

        final ToDoItem currentToDoItem = getToDoItemById(id);

        if(currentToDoItem!=null){
            final ToDoItem proposedToBeModified = new ToDoItem(currentToDoItem.getId(),currentToDoItem.getTodoString(), currentToDoItem.getAuthorEmailId(),currentToDoItem.getPlace());
            proposedToBeModified.setTodoString(editTextNewToDo.getText().toString());
            if(Util.isAppOnLine(getApplicationContext())){
                showBusyDialog("Modifying ToDo");
                ModifyToDoPayloadBean modifyToDoPayloadBean = new ModifyToDoPayloadBean(currentToDoItem,proposedToBeModified);
                GsonRequest<ToDoItem> gsonRequest = GsonRequest.getGsonRequest(GsonRequest.REQ_TYPE.MODIFY_TODOS, new GsonBuilder().create().toJson(modifyToDoPayloadBean), ToDoItem.class,
                    new Response.Listener<ToDoItem>() {
                        @Override
                        public void onResponse(ToDoItem response) {
                            dismissBusyDialog();
                            toDoItems.remove(currentToDoItem);
                            toDoItems.add(proposedToBeModified);
                            clearEditTexsts();
                            refreshList();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissBusyDialog();
                            toastMessage(error.getMessage(),Toast.LENGTH_SHORT);
                        }
                    });

                AppConfig.getWebServiceProvider().addToRequestQueue(gsonRequest);

            }else{
                toastMessage("Network Issue",Toast.LENGTH_SHORT);

            }
        }else{
            toastMessage("Please select existing id", Toast.LENGTH_LONG);
        }

    }

    private void getToDoItems(){
        if(Util.isAppOnLine(getApplicationContext())){
            showBusyDialog("Fetching ToDos");

            GsonRequest<ToDoListResponse> getToDoListRequest =  GsonRequest.getGsonRequest(GsonRequest.REQ_TYPE.GET_TODOS,null, ToDoListResponse.class,
                new Response.Listener<ToDoListResponse>() {
                    @Override
                    public void onResponse(ToDoListResponse response) {
                        dismissBusyDialog();
                        if(response.size()>0){
                            toDoItems = response;
                            refreshList();
                            clearEditTexsts();
                        }else {
                            toDoItems = new ArrayList<ToDoItem>();
                            toastMessage("No todo items",Toast.LENGTH_SHORT);
                            textViewToDos.setText("");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissBusyDialog();
                        toastMessage(error.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
            AppConfig.getWebServiceProvider().addToRequestQueue(getToDoListRequest);
        } else {
            toastMessage("Network Issue",Toast.LENGTH_SHORT);
        }
    }

    private void refreshList(){

        textViewToDos.setText(toDoItems.size()==0?"":toDoItems.toString());
    }

    private ToDoItem getToDoItemById(long id){
        ToDoItem item = null;
        for(ToDoItem doItem: toDoItems){
            if(doItem.getId()==id){
                item = doItem;
                break;
            }
        }

        return item;
    }

    private void updateToDoItem(ToDoItem proposedItem){
        ToDoItem item = getToDoItemById(proposedItem.getId());
        if(item!=null){
            toDoItems.remove(item);
            toDoItems.add(proposedItem);
            setNewList(toDoItems);
        }
    }

    private void clearEditTexsts(){
        editTextNewToDo.clearComposingText();
        editTextNewToDoString.clearComposingText();
        editTextPlace.clearComposingText();
        editTextToDoId.clearComposingText();
    }
}
