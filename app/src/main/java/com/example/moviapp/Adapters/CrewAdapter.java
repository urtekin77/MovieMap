package com.example.moviapp.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviapp.Model.CrewModel;
import com.example.moviapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {

    List<CrewModel> crewModels;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
    public CrewAdapter(List<CrewModel> crewModels){
        this.crewModels = crewModels;
    }

    @NonNull
    @Override
    public CrewAdapter.CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.crew_item, parent, false);
        return new CrewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewAdapter.CrewViewHolder holder, int position) {
        CrewModel crewModel = crewModels.get(position);
        holder.name.setText(crewModel.getCrew_name());
        holder.departmant_ad.setText(crewModel.getDepartment());
        holder.crewId = crewModel.getCrew_id();
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500/" + crewModel.getCrew_profile_path())
                .placeholder(R.drawable.install_50)
                .error(R.drawable.error_50)
                .into(holder.foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(crewModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return crewModels.size();
    }

    public void setCrewModels(List<CrewModel> crewModels) {
        this.crewModels = crewModels;
    }

    public class CrewViewHolder extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView name,departmant_ad;
        int crewId;
        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.crew_foto);
            name = itemView.findViewById(R.id.crew_ad);
            departmant_ad = itemView.findViewById(R.id.department_ad);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(CrewModel crewModel);
    }
}
