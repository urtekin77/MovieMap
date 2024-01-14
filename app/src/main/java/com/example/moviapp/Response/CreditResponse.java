package com.example.moviapp.Response;

import com.example.moviapp.Model.CastModel;
import com.example.moviapp.Model.CastModel2;
import com.example.moviapp.Model.CrewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditResponse {
    @SerializedName("cast")
    private List<CastModel2> cast;

    public List<CastModel2> getCast() {
        return cast;
    }

    @SerializedName("crew")
    private List<CrewModel> crew;

    public List<CrewModel> getCrew() {
        return crew;
    }
}
