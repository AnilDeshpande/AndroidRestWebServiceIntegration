package com.codetutor.androidrestwebserviceintegration.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codetutor.androidrestwebserviceintegration.R;
import com.codetutor.androidrestwebserviceintegration.network.APICallListener;
import com.codetutor.androidrestwebserviceintegration.network.AppNetworkRequest;
import com.codetutor.androidrestwebserviceintegration.network.Util;
import com.codetutor.androidrestwebserviceintegration.restbean.ToDoItem;

import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener, APICallListener{

    private EditText editTextNewToDoString, editTextToDoId, editTextNewToDo, editTextPlace;
    private TextView textViewToDos;
    private Button buttonAddToDo, buttonRemoveToDo, buttonModifyToDo;

    List<ToDoItem> toDoItems;

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


    }

    private void removeToDo(){


    }

    private void modifyToDo(){
        int id=Integer.parseInt(editTextToDoId.getText().toString());
        String newToDO=editTextNewToDo.getText().toString();
    }

    private void getToDoItems(){
        if(Util.isAppOnLine(getApplicationContext())){
            showBusyDialog("Fetching ToDos");
            AppNetworkRequest appNetworkRequest = AppNetworkRequest.getReqestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_GET_TODOS,this,null);
            appNetworkRequest.makeBackEndRequest();

        }else{
            Toast.makeText(getApplicationContext(),"Network Issue",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCallBackSuccess(Object o) {
        dismissBusyDialog();
        try {
            toDoItems = (List<ToDoItem>)o;
            setNewList(toDoItems);
        }catch (ClassCastException e){
            Error error = (Error)o;
            Toast.makeText(this,error.toString(),Toast.LENGTH_SHORT).toString();
        }
    }

    @Override
    public void onCallBackFailure(String message) {
        dismissBusyDialog();
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }
}
