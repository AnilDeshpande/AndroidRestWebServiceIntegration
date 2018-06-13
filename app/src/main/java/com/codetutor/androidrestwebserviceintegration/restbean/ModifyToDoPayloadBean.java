package com.codetutor.androidrestwebserviceintegration.restbean;

public class ModifyToDoPayloadBean {

    private ToDoItem currentToDoItem;
    private ToDoItem proposedToDoItem;

    public ModifyToDoPayloadBean(ToDoItem currentToDoItem, ToDoItem proposedToDoItem){
        this.currentToDoItem = currentToDoItem;
        this.proposedToDoItem = proposedToDoItem;
    }

    public void setCurrentToDoItem(ToDoItem currentToDoItem) {
        this.currentToDoItem = currentToDoItem;
    }

    public void setProposedToDoItem(ToDoItem proposedToDoItem) {
        this.proposedToDoItem = proposedToDoItem;
    }

    public ToDoItem getCurrentToDoItem() {
        return currentToDoItem;
    }

    public ToDoItem getProposedToDoItem() {
        return proposedToDoItem;
    }
}
