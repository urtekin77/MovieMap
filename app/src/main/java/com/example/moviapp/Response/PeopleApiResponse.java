package com.example.moviapp.Response;

import com.example.moviapp.Model.PeopleModel;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PeopleApiResponse {

    @SerializedName("results")
    private List<PeopleModel> people;

    public List<PeopleModel> getPeople() {
        return people;
    }
}
