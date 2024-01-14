package com.example.moviapp.Response;

import com.example.moviapp.Model.FilmModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilmApiResponse {
    @SerializedName("results")
    private List<FilmModel> filmler;

    public List<FilmModel> getFilmler() {
        return filmler;
    }
}
