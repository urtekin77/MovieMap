package com.example.moviapp.Data;

import java.util.ArrayList;

public class WatchedMovies {
    private ArrayList<Integer> film_id;

    public WatchedMovies(){

    }
    public WatchedMovies(ArrayList<Integer> film_id){
        this.film_id = film_id;
    }

    public ArrayList<Integer> getFilm_id() {
        return film_id;
    }
}
