package com.example.moviapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.moviapp.Adapters.CategoryAdapter;
import com.example.moviapp.Adapters.FilmHorizontalAdapter;
import com.example.moviapp.Adapters.FilmVerticalAdapter;
import com.example.moviapp.Data.FavoriKategori;
import com.example.moviapp.Model.CategoriModel;
import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.R;
import com.example.moviapp.Response.CategoriApiResponse;
import com.example.moviapp.Response.FilmApiResponse;
import com.example.moviapp.Service.FilmApi;
import com.example.moviapp.Util.Credentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryActivity extends AppCompatActivity {


    Retrofit retrofit;
    DatabaseReference databaseReference;
    TextView category_ad;
    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;
    FilmHorizontalAdapter filmHorizontalAdapter;
    List<FilmModel> filmModels = new ArrayList<>();
    ToggleButton categoryFav;
    public int kategori;
    public int categoriId;

    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        categoryFav = findViewById(R.id.category_fav);

        categoryRecyclerView = findViewById(R.id.category_view);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        filmHorizontalAdapter = new FilmHorizontalAdapter(filmModels);
        categoryRecyclerView.setAdapter(filmHorizontalAdapter);

        category_ad = findViewById(R.id.category_ad);

        category_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Intent intent = getIntent();
        if(intent == null || !intent.hasExtra("kategoriID")){
            Toast.makeText(this,"Geçersiz categori ID",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            categoriId = intent.getIntExtra("kategoriID",-1);
            if(categoriId != -1){
                caregoriName(categoriId);
            }
            else {
                Toast.makeText(this,"Geçersiz ID",Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        filmHorizontalAdapter.setOnItemClickListener(new FilmVerticalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FilmModel filmModel) {
                openFilmDetayActivity(filmModel);
            }
        });

        DatabaseReference favoriteCategory = FirebaseDatabase.getInstance().getReference("favoriteCategory");

        // kategori kontrolü
        favoriteCategory.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FavoriKategori favoriKategori;
                if (snapshot.exists()){
                    favoriKategori =snapshot.getValue(FavoriKategori.class);
                    Log.d("Mesajlarr", "Categoriess: " + favoriKategori.getCategory_id());
                    if (favoriKategori != null && favoriKategori.getCategory_id() != null) {
                        // Null olmayan durumlarda işlemleri gerçekleştir
                        Log.d("Mesajlarr", "Categoriess: " + favoriKategori.getCategory_id());
                        for (int category : favoriKategori.getCategory_id()) {
                            Log.d("Mesajjlarr", "forr : " + category);
                            if (category == categoriId) {
                                categoryFav.setChecked(true);
                                break;
                            } else {
                                categoryFav.setChecked(false);
                            }
                        }
                    } else {
                        Toast.makeText(CategoryActivity.this, "Null", Toast.LENGTH_SHORT).show();
                    }
                }
           
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        categoryFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                favoriteCategory.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FavoriKategori favoriKategori;
                        if (snapshot.exists()) {
                            favoriKategori = snapshot.getValue(FavoriKategori.class);
                        } else {
                            favoriKategori = new FavoriKategori(new ArrayList<>());
                            Log.d("Mesajj", favoriKategori.toString());
                        }
                        if (b && !favoriKategori.getCategory_id().contains(kategori)) {
                            favoriKategori.getCategory_id().add(kategori);
                            Toast.makeText(CategoryActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        } else if (!b) {
                            favoriKategori.getCategory_id().remove(Integer.valueOf(kategori));
                            Toast.makeText(CategoryActivity.this, "Deleted from favorites", Toast.LENGTH_SHORT).show();

                        }
                        favoriteCategory.child(user.getUid()).setValue(favoriKategori);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
    private void openFilmDetayActivity(FilmModel filmModel) {
        Intent intent = new Intent(CategoryActivity.this, FilmDetayActivity.class);
        intent.putExtra("filmId", filmModel.getId());
        startActivity(intent);
        finish();
    }



    public void caregoriName(int categoriId) {
        FilmApi filmApi =  retrofit.create(FilmApi.class);
        Call<CategoriApiResponse> call = filmApi.getCategori(Credentials.API_KEY);
        call.enqueue(new Callback<CategoriApiResponse>() {
            @Override
            public void onResponse(Call<CategoriApiResponse> call, Response<CategoriApiResponse> response) {

                if (response.isSuccessful()) {
                    CategoriApiResponse categoriApiResponse = response.body();
                    if (categoriApiResponse != null && categoriApiResponse.getCategories() != null) {
                        List<CategoriModel> categoryList = categoriApiResponse.getCategories();
                        for (CategoriModel category : categoryList) {
                            kategori = category.getId();
                            if (category.getId() == categoriId) {
                                String categoryName = category.getName();
                                category_ad.setText(categoryName);
                                categoriFilms(categoriId);
                                break; // İlgili kategori bulunduğunda döngüden çık
                            }
                        }
                    } else {
                        Log.e("API Hatası", "Boş veya geçersiz kategori listesi");
                    }
                } else {
                    Log.e("API Hatası", "Hata kodu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CategoriApiResponse> call, Throwable t) {
                Log.e("API Hatası", "Hata: " + t.getMessage());
            }
        });
    }

    private void categoriFilms(int categoriID){
        FilmApi filmApi = retrofit.create(FilmApi.class);
        Call<FilmApiResponse> call = filmApi.getDiscover(categoriID,Credentials.API_KEY,8);
        call.enqueue(new Callback<FilmApiResponse>() {
            @Override
            public void onResponse(Call<FilmApiResponse> call, Response<FilmApiResponse> response) {
                if (response.isSuccessful()){
                    FilmApiResponse apiResponse = response.body();
                    filmModels = apiResponse.getFilmler();
                    filmHorizontalAdapter.setFilmModelList(filmModels);
                    filmHorizontalAdapter.notifyDataSetChanged();

                }
                else {
                    Log.e("API Hatası", "Hata kodu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<FilmApiResponse> call, Throwable t) {
                Log.e("API Hatası", "Hata: " + t.getMessage());

            }
        });

    }


    public void onToogleClick(View view) {
    }
}
