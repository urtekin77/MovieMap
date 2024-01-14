package com.example.moviapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PeopleModel {

    @SerializedName("id")
    public int people_id;

    @SerializedName("known_for_department")
    public String know_for_department;

    @SerializedName("name")
    public String people_name;

    @SerializedName("popularity")
    public float people_popularity;

    @SerializedName("profile_path")
    public String people_profile_path;

    @SerializedName("casts")
    public List<Known_for> people_known_for;
    @SerializedName("biography")
    public String person_biography;
    @SerializedName("birthday")
    public String person_birthday;

    @SerializedName("place_of_birth")
    public  String person_place_of_birth;

    public String getPerson_birthday() {
        return person_birthday;
    }

    public String getPerson_place_of_birth() {
        return person_place_of_birth;
    }

    public String getPerson_biography() {
        return person_biography;
    }

    public int getPeople_id() {
        return people_id;
    }

    public String getKnow_for_department() {
        return know_for_department;
    }

    public String getPeople_name() {
        return people_name;
    }

    public float getPeople_popularity() {
        return people_popularity;
    }

    public String getPeople_profile_path() {
        return people_profile_path;
    }

    public List<Known_for> getPeople_known_for() {
        return people_known_for;
    }

    private class Known_for {
        @SerializedName("id")
        public int film_id;

        @SerializedName("title")
        public String film_isim;

        @SerializedName("poster_path")
        public String film_poster_path;

        @SerializedName("vote_average")
        public float film_puan;

        public int getFilm_id() {
            return film_id;
        }

        public String getFilm_isim() {
            return film_isim;
        }

        public String getFilm_poster_path() {
            return film_poster_path;
        }

        public float getFilm_puan() {
            return film_puan;
        }
    }
}
