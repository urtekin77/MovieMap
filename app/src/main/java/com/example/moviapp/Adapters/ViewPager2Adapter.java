package com.example.moviapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.AdapterHolder> {

    private List<FilmModel> filmList;
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public ViewPager2Adapter(List<FilmModel> filmList) {
        this.filmList=filmList;
    }
    @NonNull
    @Override
    public ViewPager2Adapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_pager2_view,parent,false);
        return new AdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, int position) {
        FilmModel filmModel = filmList.get(position);
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
        return filmList.size();
    }

    public void setFilmList(List<FilmModel> filmList){
        this.filmList= filmList;
    }

    public List<FilmModel> getFilmList(){
        return filmList;
    }
    public class AdapterHolder extends RecyclerView.ViewHolder {
        ImageView foto;
        int filmId;


        public AdapterHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.foto);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(FilmModel filmModel);
    }

}
