package com.example.moviapp.Response;

import com.example.moviapp.Model.CastModel;
import com.example.moviapp.Model.FilmModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastApiResponse {
    @SerializedName("cast")
    private List<CastModel> cast;

    // Diğer gerekli alanları da ekleyebilirsiniz

    public List<CastModel> getCast() {
        return cast;
    }
}
