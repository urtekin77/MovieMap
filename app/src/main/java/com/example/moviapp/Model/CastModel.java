package com.example.moviapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastModel {
    @SerializedName("adult")
    private boolean adult;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    @SerializedName("id")
    private int id;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("known_for_department")
    private String knowForDepatment;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("title")
    private String title;

    @SerializedName("video")
    private boolean video;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("character")
    private String character;

    @SerializedName("credit_id")
    private String creditId;

    @SerializedName("order")
    private int order;

    public String getKnowForDepatment() {
        return knowForDepatment;
    }

    public int getId() {
        return id;
    }

    public double getPopularity() {
        return popularity;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public int getOrder() {
        return order;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getCharacter() {
        return character;
    }

    public String getCreditId() {
        return creditId;
    }
    // Diğer gerekli alanları da ekleyebilirsiniz

    // Getter ve setter metotlarını ekleyebilirsiniz
}

