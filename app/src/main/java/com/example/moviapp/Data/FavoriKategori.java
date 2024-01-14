package com.example.moviapp.Data;

import java.util.ArrayList;
import java.util.List;

public class FavoriKategori {

    private ArrayList<Integer> category_id;

    public FavoriKategori(){

    }
    public FavoriKategori( ArrayList<Integer> categori_id){
        this.category_id = categori_id;

    }


    public List<Integer> getCategory_id() {
        return category_id;
    }

    public void setCategory_id(ArrayList<Integer> category_id) {
        this.category_id = category_id;
    }
}
