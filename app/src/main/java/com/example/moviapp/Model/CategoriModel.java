package com.example.moviapp.Model;

import com.google.gson.annotations.SerializedName;

public class CategoriModel {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    private String name;

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
}
