package com.google.sps.data;

public class Comment {

    private String username;
    private String message;
    private String date;
    private String emoji;
    private double score;

    //Default class constructor:
    public Comment(){

        username = "UNKNOWN";
        message = "No comment";
        date = "No date available";
        emoji = ";|";

    }

    //Class constructor with parameters:
    public Comment(String username, String message, String date, double score, String emoji){

        this.username = username;
        this.message = message;
        this.date = date;
        this.score = score;
        this.emoji = emoji;

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

    public String getEmoji(){

        return emoji;

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

    public void setEmoji(String emoji){

        this.emoji = emoji;

    }

}