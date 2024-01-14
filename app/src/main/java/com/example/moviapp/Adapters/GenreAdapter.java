package com.example.moviapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.R;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreHolder> {
    private List<FilmModel.Genre> genreList;
    public GenreAdapter(List<FilmModel.Genre> genreList){
        this.genreList = genreList;
    }
    @NonNull
    @Override
    public GenreAdapter.GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.genre_view,parent,false);
        return new GenreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.GenreHolder holder, int position) {
        FilmModel.Genre filmModel = genreList.get(position);
        Log.d("GenreAdapter", "Genre Name: " + filmModel.getGenre_name());

        holder.categoriName.setText(filmModel.getGenre_name());
    }

    @Override
    public int getItemCount() {
        Log.d("GenreAdapter", "Genre List Size: " + genreList.size());

        return genreList.size();
    }
    public void setGenreList(List<FilmModel.Genre> filmModelsGenre){
        this.genreList= filmModelsGenre;
    }
    public List<FilmModel.Genre> getGenreList(){
        return genreList;
    }

    public class GenreHolder extends RecyclerView.ViewHolder {
        TextView categoriName;

        public GenreHolder(@NonNull View itemView) {
            super(itemView);
            categoriName = itemView.findViewById(R.id.categoriName);

        }
    }
}
