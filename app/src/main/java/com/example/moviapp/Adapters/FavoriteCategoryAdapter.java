package com.example.moviapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviapp.R;

import java.util.List;

import retrofit2.Retrofit;

public class FavoriteCategoryAdapter extends RecyclerView.Adapter<FavoriteCategoryAdapter.FavCategoryAdapter> {
    private List<String> categories;
    private Retrofit retrofit;
    public FavoriteCategoryAdapter(Retrofit retrofit, List<String> categories){
        this.categories = categories;
        this.retrofit = retrofit;
    }
    @NonNull
    @Override
    public FavoriteCategoryAdapter.FavCategoryAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categori_view,parent,false);
        return new FavCategoryAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteCategoryAdapter.FavCategoryAdapter holder, int position) {
        String category = categories.get(position);
        holder.categoriName.setText(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class FavCategoryAdapter extends RecyclerView.ViewHolder {
        private TextView categoriName;
        public FavCategoryAdapter(@NonNull View itemView) {
            super(itemView);
            categoriName = itemView.findViewById(R.id.categoriName);
        }
    }
}
