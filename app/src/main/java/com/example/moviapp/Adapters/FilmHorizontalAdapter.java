package com.example.moviapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviapp.Model.CastModel;
import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FilmHorizontalAdapter extends RecyclerView.Adapter<FilmHorizontalAdapter.HorizontalHolder> {

    private List<FilmModel> filmModelList;
    public void addFilm(FilmModel film) {
        filmModelList.add(film);
        notifyItemInserted(filmModelList.size() - 1);
    }

    private FilmVerticalAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(FilmVerticalAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public FilmHorizontalAdapter(List<FilmModel> filmModelList){
        this.filmModelList = filmModelList;
    }
    @NonNull
    @Override
    public FilmHorizontalAdapter.HorizontalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.horizontalholder_film,parent,false);
        return new HorizontalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmHorizontalAdapter.HorizontalHolder holder, int position) {

        FilmModel filmModel = filmModelList.get(position);
        holder.baslikTxt.setText(filmModel.getTitle());
        holder.puanTxt.setText(String.valueOf(filmModel.vote_average));
        holder.filmId = filmModel.getId();
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500/"+filmModel.getPoster_path())
                .placeholder(R.drawable.install_50)   //yükleme sırasında gösterilecek
                .error(R.drawable.error_50)   // yükleme hatasında
                .into(holder.foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(filmModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmModelList.size();
    }
    public void setFilmModelList(List<FilmModel> filmModelList) {
        this.filmModelList=filmModelList;
    }

    public class HorizontalHolder extends RecyclerView.ViewHolder {
        TextView baslikTxt;
        TextView puanTxt;
        ImageView foto;
        int filmId;


        public HorizontalHolder(@NonNull View itemView) {
            super(itemView);

            baslikTxt = itemView.findViewById(R.id.name);
            foto = itemView.findViewById(R.id.foto);
            puanTxt = itemView.findViewById(R.id.film_puan);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(FilmModel filmModel);
    }
}
