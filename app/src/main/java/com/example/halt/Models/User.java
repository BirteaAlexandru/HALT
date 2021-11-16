package com.example.halt.Models;

import java.util.ArrayList;

public class User {
    String userId=null;
    String email;
    String username;
    boolean smoking_available;
    //ArrayList<User> friends;
   // ArrayList<User> friendRequests;

    public User(){

    }
    public User(String email){
        this.email =  email;
        smoking_available = true;
   //     friends = null;
   //     friendRequests = null;
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

    public void addFriend(){

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

    public boolean isSmoking_available() {
        return smoking_available;
    }

    public void setSmoking_available(boolean smoking_available) {
        this.smoking_available = smoking_available;
    }
}
