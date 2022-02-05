package com.example.recipeapp;

public class User { //singleton
    static User user = new User();
    String username;
    String email;
    String password;
    int id;

    private User(){

    }
    public static User getInstance(){
        return user;
    }
    public int getUserID(){
        return id;
    }
    public void setUserData(int id, String username,String email,String password){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
