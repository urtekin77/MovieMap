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

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastHolder> {
    private List<CastModel> castModelList;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public CastAdapter(List<CastModel> castModelList){
        this.castModelList = castModelList;
    }
    @NonNull
    @Override
    public CastAdapter.CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.verticalholder_film,parent,false);
        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastHolder holder, int position) {
        CastModel castModel = castModelList.get(position);
        holder.puanTxt.setText(String.valueOf(castModel.getVoteAverage()));
        holder.filmId = castModel.getId();
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500/" + castModel.getPosterPath())
                .placeholder(R.drawable.install_50)   //yükleme sırasında gösterilecek
                .error(R.drawable.error_50)   // yükleme hatasında
                .into(holder.foto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(castModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return castModelList.size();
    }

    public void setCastModelList(List<CastModel> castModelList) {
        this.castModelList = castModelList;
    }

    public class CastHolder extends RecyclerView.ViewHolder {
        ImageView foto;

        TextView puanTxt;

        int filmId;
        public CastHolder(@NonNull View itemView) {
            super(itemView);
            puanTxt = itemView.findViewById(R.id.puanTxt);
            foto = itemView.findViewById(R.id.foto);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(CastModel castModel);
    }
}
