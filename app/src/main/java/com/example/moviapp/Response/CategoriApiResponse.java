package com.example.moviapp.Response;

import com.example.moviapp.Model.CategoriModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriApiResponse {

    @SerializedName("genres")
    private List<CategoriModel> categories;

    public List<CategoriModel> getCategories(){
        return categories;
    }
}
