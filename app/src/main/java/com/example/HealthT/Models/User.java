package com.example.HealthT.Models;

public class User {
    String userId=null;
    String email;
    String username;


    public User(){

    }
    public User(String email){
        this.email =  email;

    }
    public User(String username, String email) {
        this.email = email;
        this.username = username;
    }
    public User(String username, String email, String userId) {
        this.email = email;
        this.username = username;
        this.userId= userId;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(String userId) { this.userId = userId;}

    public String getUserId() {  return userId;   }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
