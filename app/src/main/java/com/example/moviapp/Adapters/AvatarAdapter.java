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

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarHolder> {

    private List<Avatar> avatarList;
    private OnAvatarClickListener onAvatarClickListener;

    public interface OnAvatarClickListener{
        void onAvatarClick(int position);
    }


    public AvatarAdapter(List<Avatar> avatarList, OnAvatarClickListener onAvatarClickListener){
        this.avatarList = avatarList;
        this.onAvatarClickListener = onAvatarClickListener;
    }

    @NonNull
    @Override
    public AvatarAdapter.AvatarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater  layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.avatar_item,parent,false);
        return new AvatarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarAdapter.AvatarHolder holder, int position) {
        Avatar avatar = avatarList.get(position);
        holder.avatarImage.setImageResource(avatar.getAvatarResourceId());
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

    public class AvatarHolder extends RecyclerView.ViewHolder {

        ImageView avatarImage;
        CardView cardView;
        public AvatarHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            avatarImage = itemView.findViewById(R.id.avatarImage);
        }
    }

}
