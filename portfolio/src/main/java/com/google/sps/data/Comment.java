package com.google.sps.data;

import java.time.LocalDate;

public class Comment {

    private String username;
    private String message;
    private String date;

    //Default class constructor:
    public Comment(){

        username = "UNKNOWN";
        message = "No comment";

        LocalDate messageDate = LocalDate.now();
        date = messageDate.toString();

    }

    //Class constructor with parameters:
    public Comment(String username, String message){

        this.username = username;
        this.message = message;

        LocalDate messageDate = LocalDate.now();
        date = messageDate.toString();

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