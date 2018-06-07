package com.codetutor.androidrestwebserviceintegration.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.APICallListener;
import com.codetutor.androidrestwebserviceintegration.network.AppNetworkRequest;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.Error;
import com.codetutor.androidrestwebserviceintegration.restbean.ModifyToDoPayloadBean;
import com.codetutor.androidrestwebserviceintegration.restbean.Success;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoItem;

import java.util.List;

import okhttp3.RequestBody;

public class HomeActivity extends BaseActivity implements View.OnClickListener, APICallListener{

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
            AppNetworkRequest appNetworkRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_ADD_TODO_ITEM,this,item);
            appNetworkRequest.makeBackEndRequest();

        }else{
            toastMessage("Network Issue",Toast.LENGTH_SHORT);
        }

    }

    private void removeToDo(){
        long removiewId = Long.parseLong(editTextToDoId.getText().toString());
        itemToBeRemoved = getToDoItemById(removiewId);
        if(itemToBeRemoved!=null){
            if(Util.isAppOnLine(getApplicationContext())){
                showBusyDialog("Deleting ToDo");
                AppNetworkRequest appNetworkRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_DELETE_TODO,this,itemToBeRemoved);
                appNetworkRequest.makeBackEndRequest();

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

        ToDoItem currentToDoItem = getToDoItemById(id);

        if(currentToDoItem!=null){
            ToDoItem proposedToBeModified = new ToDoItem(currentToDoItem.getId(),currentToDoItem.getTodoString(), currentToDoItem.getAuthorEmailId(),currentToDoItem.getPlace());
            proposedToBeModified.setTodoString(editTextNewToDo.getText().toString());
            if(Util.isAppOnLine(getApplicationContext())){
                ModifyToDoPayloadBean modifyToDoPayloadBean = new ModifyToDoPayloadBean(currentToDoItem,proposedToBeModified);
                AppNetworkRequest appNetworkRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_MODIFY_TODO,this, modifyToDoPayloadBean);
                appNetworkRequest.makeBackEndRequest();
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
            AppNetworkRequest appNetworkRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_GET_TODOS,this,null);
            appNetworkRequest.makeBackEndRequest();
        }else{
            toastMessage("Network Issue",Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onCallBackSuccess(AppNetworkRequest.REQUEST_TYPE requestType, Object o) {

        dismissBusyDialog();
        try {
            switch (requestType){
                case REQUEST_GET_TODOS:
                    if(o!=null){
                        toDoItems = (List<ToDoItem>)o;
                        setNewList(toDoItems);
                        if(toDoItems.size()==0){
                            toastMessage("No todo items", Toast.LENGTH_LONG);
                        }
                    }
                    break;

                case REQUEST_ADD_TODO_ITEM:
                    getToDoItems();
                    toastMessage("Added",Toast.LENGTH_LONG);
                    break;
                case REQUEST_DELETE_TODO:
                    Success success = (Success)o;
                    toDoItems.remove(itemToBeRemoved);
                    setNewList(toDoItems);
                    toastMessage("Deleted",Toast.LENGTH_LONG);
                    break;
                case REQUEST_MODIFY_TODO:
                    ToDoItem item = (ToDoItem)o;
                    updateToDoItem(item);
                    toastMessage("Modified",Toast.LENGTH_LONG);
                    break;
            }

            clearEditTexsts();

        }catch (ClassCastException e){
            Error error = (Error)o;
            toastMessage(error.getErrorMessage(), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onCallBackFailure(String message) {
        dismissBusyDialog();
        toastMessage(message,Toast.LENGTH_SHORT);
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
