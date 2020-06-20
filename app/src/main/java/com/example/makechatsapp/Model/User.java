package com.example.makechatsapp.Model;

import android.net.Uri;

public class User {
    String profileIMG;
    String id;
    String userName;
    String password;
    String email;

    public User() {
    }

    public User(String userName, String password, String email, String profileIMG) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.profileIMG = profileIMG;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileIMG() {
        return profileIMG;
    }

    public void setProfileIMG(String profileIMG) {
        this.profileIMG = profileIMG;
    }
}
