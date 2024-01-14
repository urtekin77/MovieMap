package com.example.moviapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviapp.Data.Avatar;
import com.example.moviapp.R;

import java.util.ArrayList;
import java.util.List;

public class SelectAvatarAdapter extends RecyclerView.Adapter<SelectAvatarAdapter.SelectHolder> {
    private List<Avatar> avatarList;
    private OnAvatarClickListener onAvatarClickListener;

    public interface OnAvatarClickListener{
        void onAvatarClick(int position);
    }
    public SelectAvatarAdapter(List<Avatar> avatarList, OnAvatarClickListener onAvatarClickListener){
        this.avatarList = avatarList; // Yeni bir ArrayList oluştur

        this.onAvatarClickListener = onAvatarClickListener;
    }
    @NonNull
    @Override
    public SelectAvatarAdapter.SelectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.avatar_select,parent,false);
        return new SelectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAvatarAdapter.SelectHolder holder, int position) {
        int avatar= avatarList.get(position).getAvatarResourceId();
        holder.avatarImage.setImageResource(avatar);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAvatarClickListener.onAvatarClick(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return avatarList.size();
    }

    public class SelectHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        CardView cardView;
        public SelectHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            avatarImage = itemView.findViewById(R.id.avatarImage);
        }
    }
}
