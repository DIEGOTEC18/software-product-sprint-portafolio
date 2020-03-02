package com.google.sps.data;

public class Comment {

    private String username;
    private String message;
    private String date;

    //Default class constructor:
    public Comment(){

        username = "UNKNOWN";
        message = "No comment";
        date = "No date available";

    }

    //Class constructor with parameters:
    public Comment(String username, String message, String date){

        this.username = username;
        this.message = message;
        this.date = date;

    }

    //Getters/Setters:

    public String getUsername(){

        return username;

    }

    public String getMessage(){

        return message;

    }

    public String getDate(){

        return date;

    }

    public void setUsername(String username){

        this.username = username;

    }

    public void setMessage(String message){

        this.message = message;

    }

}