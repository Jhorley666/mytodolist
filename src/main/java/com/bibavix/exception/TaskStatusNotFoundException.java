package com.bibavix.exception;

public class TaskStatusNotFoundException extends RuntimeException{
    public TaskStatusNotFoundException(Integer taskStatusId){
        super("Task status not found with id: " + taskStatusId);
    }
}
