package com.example.moviapp.Model;

import com.google.gson.annotations.SerializedName;

public class CastModel2 {
    @SerializedName("id")
    private int castId;

    @SerializedName("name")
    private String castName;

    @SerializedName("profile_path")
    private String castProfilePath;

    @SerializedName("known_for_department")
    private String alan;
    @SerializedName("character")
    private String castCharactarName;

    public String getCastProfilePath() {
        return castProfilePath;
    }

    public int getCastId() {
        return castId;
    }

    public String getCastName() {
        return castName;
    }

    public String getAlan() {
        return alan;
    }

    public String getCastCharactarName() {
        return castCharactarName;
    }
}
