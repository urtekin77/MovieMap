package com.example.moviapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditModel {
    @SerializedName("cast")
    private List<CastModel> cast;

    @SerializedName("crew")
    private List<CrewModel> crew;

    public List<CastModel> getCast() {
        return cast;
    }

    public List<CrewModel> getCrew() {
        return crew;
    }
}
