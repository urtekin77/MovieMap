package com.example.moviapp.Data;

import java.util.ArrayList;

public class MoviesToWatch {

    private ArrayList<Integer> film_id;

    public MoviesToWatch(){

    }
    public MoviesToWatch(ArrayList<Integer> film_id){
        this.film_id = film_id;
    }

    public ArrayList<Integer> getFilm_id() {
        return film_id;
    }
}
