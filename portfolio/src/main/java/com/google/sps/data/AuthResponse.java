package com.google.sps.data;

public class AuthResponse {

    private String email;
    private String logoutUrl;
    //private String nickname;

    private AuthResponse(){}

    public AuthResponse(String email, String logoutUrl){

        this.email = email;
        this.logoutUrl = logoutUrl;

    }

    public String getEmail(){

        return email;

    }

    public String getLogoutUrl(){

        return logoutUrl;

    }

    public void setEmail(String email){

        this.email = email;

    }

    public void setLogoutUrl(String logoutUrl){

        this.logoutUrl = logoutUrl;

    }

}