package com.example.moviapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviapp.Model.PeopleModel;
import com.example.moviapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PeopleVerticalAdapter extends RecyclerView.Adapter<PeopleVerticalAdapter.PeopleAdapter> {

    private List<PeopleModel> peopleModelList;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PeopleVerticalAdapter(List<PeopleModel> peopleModelList) {
        this.peopleModelList = peopleModelList;
    }

    @NonNull
    @Override
    public PeopleVerticalAdapter.PeopleAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.verticalholder_person, parent, false);
        return new PeopleAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleVerticalAdapter.PeopleAdapter holder, int position) {
        PeopleModel peopleModel = peopleModelList.get(position);
        holder.person_ad.setText(peopleModel.people_name);
        holder.person_id = peopleModel.people_id;
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500/"+peopleModel.getPeople_profile_path())
                .placeholder(R.drawable.install_50)   //yükleme sırasında gösterilecek
                .error(R.drawable.error_50)
                .into(holder.person_foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(peopleModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return peopleModelList.size();
    }

    public void setPeopleModelList(List<PeopleModel> peopleModelList) {
        this.peopleModelList = peopleModelList;
    }

    public List<PeopleModel> getPeopleModelList(){
        return peopleModelList;
    }
    public class PeopleAdapter extends RecyclerView.ViewHolder {
        ImageView person_foto;
        TextView person_ad;
        int person_id;
        public PeopleAdapter(@NonNull View itemView) {
            super(itemView);
            person_foto = itemView.findViewById(R.id.person_foto);
            person_ad = itemView.findViewById(R.id.person_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick( PeopleModel peopleModel);
    }
}
