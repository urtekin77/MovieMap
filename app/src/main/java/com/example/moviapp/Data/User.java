package com.example.moviapp.Data;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String user_name;
    private String user_email;
    private String user_avatar;
    private String user_id;

    public User() {
        // Boş yapıcı
    }
    public User(String user_name, String user_email, String user_id, String user_avatar){
       this.user_name = user_name;
       this.user_email = user_email;
       this.user_id = user_id;
       this.user_avatar = user_avatar;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
