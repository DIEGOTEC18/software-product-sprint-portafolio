package com.google.sps.data;

public class Comment {

    private String username;
    private String message;
    private String date;
    private double score;

    //Default class constructor:
    public Comment(){

        username = "UNKNOWN";
        message = "No comment";
        date = "No date available";

    }

    //Class constructor with parameters:
    public Comment(String username, String message, String date, double score){

        this.username = username;
        this.message = message;
        this.date = date;
        this.score = score;

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

    public double getScore(){

        return score;

    }

    public void setUsername(String username){

        this.username = username;

    }

    public void setMessage(String message){

        this.message = message;

    }

    public void setScore(double score){

        this.score = score;

    }

}