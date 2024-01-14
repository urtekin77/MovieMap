package com.example.moviapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FilmModel {

    @SerializedName("title")
    public String title;
    @SerializedName("id")
    public int id;
    @SerializedName("poster_path")
    public String poster_path;
    @SerializedName("original_title")
    public String original_title;
    @SerializedName("overview")
    public String overview;
    @SerializedName("release_date")
    public String release_date;
    @SerializedName("vote_average")
    public float vote_average; // puan
    @SerializedName("runtime")
    public int runtime;  // film süresi
    @SerializedName("backdrop_path")
    public String backdrop_path; // arkaplan resmi
    @SerializedName("genres")
    public List<Genre> genres; //kategoriler
    @SerializedName("tagline")
    public String tagline;


    @SerializedName("credits")
    public List<CreditModel> credits;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }


    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }


    public String getRelease_date() {
        return release_date;
    }

    public float getVote_average() {
        return vote_average;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getTagline() {
        return tagline;
    }

    public List<CreditModel> getCredits() {
        return credits;
    }

    public class Genre {
        @SerializedName("id")
        public int genre_id;
        @SerializedName("name")
        public String genre_name;

        public int getGenre_id() {
            return genre_id;
        }

        public String getGenre_name() {
            return genre_name;
        }
    }

}
