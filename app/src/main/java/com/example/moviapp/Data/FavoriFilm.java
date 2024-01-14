package com.example.moviapp.Data;

import java.util.ArrayList;

public class FavoriFilm {
    private ArrayList<Integer> film_id;
    public FavoriFilm(){

    }
    public FavoriFilm(ArrayList<Integer> film_id){
        this.film_id = film_id;
    }

    public ArrayList<Integer> getFilm_id() {
        return film_id;
    }

    public void setFilm_id(ArrayList<Integer> film_id) {
        this.film_id = film_id;
    }
}
