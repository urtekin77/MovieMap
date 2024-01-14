package com.example.moviapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviapp.Model.CategoriModel;
import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoriAdp> {

    private List<CategoriModel> categoriList;
    private  List<FilmModel> filmModelList;
    private CategoryAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CategoryAdapter(List<CategoriModel> categoriList){
        this.categoriList = categoriList;
    }
    @NonNull
    @Override
    public CategoriAdp onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.categori_view,parent,false);
        return new CategoriAdp(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriAdp holder, int position) {
        CategoriModel categoriModel = categoriList.get(position);
        holder.categoriName.setText(categoriModel.getName());
        holder.kategoriID = categoriModel.getId();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(categoriModel);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriList.size();
    }
    public void setCategoriList(List<CategoriModel> categoriList){
        this.categoriList=categoriList;
    }



    public List<CategoriModel> getCategoriList(){
        return categoriList;
    }

    public class CategoriAdp extends RecyclerView.ViewHolder {

        TextView categoriName;

        int kategoriID;
        public CategoriAdp(@NonNull View itemView) {
            super(itemView);
            categoriName = itemView.findViewById(R.id.categoriName);
        }
    }

    public interface  OnItemClickListener {
        void onItemClick(CategoriModel categoriModel);
    }
}
