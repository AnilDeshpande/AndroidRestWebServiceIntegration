package com.codetutor.androidrestwebserviceintegration.restbean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by anildeshpande on 2/21/18.
 */

public class ToDoItem implements Serializable {

    private long id;
    private String todoString;
    private long date;
    private String place;

    private String authorEmailId;

    public ToDoItem() {
        super();
    }

    public ToDoItem(ToDoItem doItem) {
        this(doItem.id, doItem.todoString, doItem.authorEmailId, doItem.place);
    }

    public ToDoItem(long id, String todoString, String authorEmailId,String place) {
        super();
        this.id = id;
        this.todoString = todoString;
        this.date = new Date().getTime();
        this.authorEmailId = authorEmailId;
        this.place = place;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTodoString() {
        return todoString;
    }
    public void setTodoString(String todoString) {
        this.todoString = todoString;
    }
    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }

    public String getAuthorEmailId() {
        return authorEmailId;
    }

    public void setAuthorEmailId(String authorEmailId) {
        this.authorEmailId = authorEmailId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof ToDoItem) {
            ToDoItem doItem = (ToDoItem) obj;
            if(this.authorEmailId.equals(doItem.authorEmailId) && this.id==doItem.id && this.date==doItem.date && this.todoString.equals(doItem.todoString) && this.place.equals(doItem.place)) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (id+""+authorEmailId+todoString+place).hashCode();
    }

    @Override
    public String toString() {
        return "("+this.id+", "+this.todoString+", "+this.place+")";
    }
}
