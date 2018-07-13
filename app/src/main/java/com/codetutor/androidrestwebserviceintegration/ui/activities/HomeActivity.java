package com.codetutor.androidrestwebserviceintegration.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codetutor.androidrestwebserviceintegration.AppConfig;
import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.ModifyToDoPayloadBean;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoItem;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            AppConfig.getApiServiceProvider().addToDoItem(item).enqueue(new Callback<ToDoItem>() {
                @Override
                public void onResponse(Call<ToDoItem> call, Response<ToDoItem> response) {
                    dismissBusyDialog();
                    toDoItems.add(response.body());
                    setNewList(toDoItems);
                }

                @Override
                public void onFailure(Call<ToDoItem> call, Throwable t) {
                    dismissBusyDialog();
                    toastMessage(t.getMessage(),Toast.LENGTH_SHORT);
                }
            });

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
                showBusyDialog("Modifying ToDo");
                ModifyToDoPayloadBean modifyToDoPayloadBean = new ModifyToDoPayloadBean(currentToDoItem,proposedToBeModified);

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
            AppConfig.getApiServiceProvider().getToDoList().enqueue(new Callback<List<ToDoItem>>() {
                @Override
                public void onResponse(Call<List<ToDoItem>> call, Response<List<ToDoItem>> response) {
                    dismissBusyDialog();
                    if(response.body().size()>0){
                        setNewList(response.body());
                    }else {
                        toastMessage("No todo items",Toast.LENGTH_SHORT);
                        textViewToDos.setText("");
                    }
                }

                @Override
                public void onFailure(Call<List<ToDoItem>> call, Throwable t) {
                    dismissBusyDialog();
                    toastMessage(t.getMessage(),Toast.LENGTH_SHORT);
                }
            });

        }else{
            toastMessage("Network Issue",Toast.LENGTH_SHORT);
        }
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
