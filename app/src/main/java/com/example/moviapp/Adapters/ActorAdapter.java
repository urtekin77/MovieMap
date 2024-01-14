package com.example.moviapp.Adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviapp.Model.CastModel;
import com.example.moviapp.Model.CastModel2;
import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ActorViewHolder> {

    private List<CastModel2> castModelList;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public ActorAdapter(List<CastModel2> castModelList){
        this.castModelList = castModelList;
    }

    @NonNull
    @Override
    public ActorAdapter.ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cast_item,parent,false);
        return new ActorViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ActorAdapter.ActorViewHolder holder, int position) {
        CastModel2 castModel = castModelList.get(position);
        holder.charactarName.setText(castModel.getCastCharactarName());
        holder.name.setText(castModel.getCastName());
        holder.actorID = castModel.getCastId();
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500/" + castModel.getCastProfilePath())
                .placeholder(R.drawable.install_50)
                .error(R.drawable.error_50)
                .into(holder.foto);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(castModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return castModelList.size();
    }

    public void setCastModelList(List<CastModel2> castModelList) {
        this.castModelList = castModelList;
    }

    public class ActorViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView name, charactarName;
        int actorID;


        public ActorViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.catsFoto);
            name = itemView.findViewById(R.id.cast_ad);
            charactarName = itemView.findViewById(R.id.karakter_ad);

        }
    }
    public interface OnItemClickListener {
        void onItemClick(CastModel2 castModel);
    }
}
