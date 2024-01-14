package com.example.moviapp.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FilmVerticalAdapter extends RecyclerView.Adapter<FilmVerticalAdapter.VerticalHolder> {

    private List<FilmModel> filmList;
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public FilmVerticalAdapter(List<FilmModel> filmList) {
        if (filmList == null) {
            this.filmList = new ArrayList<>();
        } else {
            this.filmList = filmList;
        }
    }

    @NonNull
    @Override
    public VerticalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.verticalholder_film,parent,false);
        return new VerticalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalHolder holder, int position) {
        FilmModel filmModel = filmList.get(position);
        holder.puanTxt.setText(String.valueOf(filmModel.getVote_average()));
        holder.filmId = filmModel.getId();
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500/"+filmModel.getPoster_path())
                .placeholder(R.drawable.install_50)   //yükleme sırasında gösterilecek
                .error(R.drawable.error_50)   // yükleme hatasında
                .into(holder.foto);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(filmModel);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (filmList != null) {
            return filmList.size();
        } else {
            return 0; // veya başka bir değer
        }
    }

  /*  public void setFilmList(List<FilmModel> filmList) {
        this.filmList = filmList;
    }*/
    public void setFilmList(List<FilmModel> filmList) {
        if (filmList == null) {
            this.filmList = new ArrayList<>();
        } else {
            this.filmList = filmList;
        }
    }
    public List<FilmModel> getFilmList() {
        return filmList;
    }

    public class VerticalHolder extends RecyclerView.ViewHolder{

        ImageView foto;

        TextView puanTxt;

        int filmId;


        public VerticalHolder(@NonNull View itemView) {
            super(itemView);
            puanTxt = itemView.findViewById(R.id.puanTxt);
            foto = itemView.findViewById(R.id.foto);

        }
    }

    public interface OnItemClickListener {
         void onItemClick(FilmModel filmModel);
    }
}
