package com.example.moviapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviapp.Adapters.CategoryAdapter;
import com.example.moviapp.Adapters.FilmVerticalAdapter;
import com.example.moviapp.Adapters.ViewPager2Adapter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Top-rated ekle
//upcoming bağlanacak
//search işlemi
//viewpager tek bir görünüm türünü destekleyen basit bir yapıdır.

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;
    LinearLayout discover, profile, list, suggestionLayout, suggestionLayout2;

    List<FilmModel> filmModels = new ArrayList<>();
    List<Integer> kategoriler = new ArrayList<>();
    List<Integer> filmler = new ArrayList<>();

    List<CategoriModel> categoriModels = new ArrayList<>();
    FilmVerticalAdapter bestFilmVerticalAdapter, UpcomingAdapter, SuggestionAdapter, SuggestionAdapter2;
    CategoryAdapter CategoriAdapter;
    TextView textViewSearch, textViewOneri2;
    ViewPager2 viewPager2;
    ViewPager2Adapter viewPager2Adapter;

    private RecyclerView recyclerViewEnIyiFilmler, recyclerViewGelecekler, recyclerViewKategori,
            recyclerViewSuggestion, recyclerViewSuggestion2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.view_pager);
        viewPager2Adapter =new ViewPager2Adapter(filmModels);
        viewPager2.setAdapter(viewPager2Adapter);

        recyclerViewEnIyiFilmler=findViewById(R.id.view1);
        recyclerViewEnIyiFilmler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        bestFilmVerticalAdapter = new FilmVerticalAdapter(filmModels);
        recyclerViewEnIyiFilmler.setAdapter(bestFilmVerticalAdapter);

        recyclerViewKategori=findViewById(R.id.view2);
        recyclerViewKategori.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        CategoriAdapter = new CategoryAdapter(categoriModels);
        recyclerViewKategori.setAdapter(CategoriAdapter);


        recyclerViewGelecekler=findViewById(R.id.view3);
        recyclerViewGelecekler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        UpcomingAdapter = new FilmVerticalAdapter(filmModels);
        recyclerViewGelecekler.setAdapter(UpcomingAdapter);

        recyclerViewSuggestion = findViewById(R.id.view4);
        recyclerViewSuggestion.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        SuggestionAdapter = new FilmVerticalAdapter(filmModels);
        recyclerViewSuggestion.setAdapter(SuggestionAdapter);

        recyclerViewSuggestion2 = findViewById(R.id.view5);
        recyclerViewSuggestion2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        SuggestionAdapter2 = new FilmVerticalAdapter(filmModels);
        recyclerViewSuggestion2.setAdapter(SuggestionAdapter2);

        textViewOneri2 = findViewById(R.id.oneri2);

        suggestionLayout = findViewById(R.id.suggestionLayout);
        suggestionLayout2 = findViewById(R.id.suggestionLayout2);

        discover = findViewById(R.id.discover_activity);
        list = findViewById(R.id.list_activity);
        profile = findViewById(R.id.profil_activity);

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));

            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListsActivity.class));

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));

            }
        });


        Gson gson = new GsonBuilder().setLenient().create(); //Gson oluşturdu

        retrofit =new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ViewPager();
        BestMovie();
        Categori();
        UpcomingMovie();
        YouChoseTo();
        YouChoseTo2();




        bestFilmVerticalAdapter.setOnItemClickListener(new FilmVerticalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FilmModel filmModel) {
                openFilmDetayActivity(filmModel);
            }
        });

        UpcomingAdapter.setOnItemClickListener(new FilmVerticalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FilmModel filmModel) {
                openFilmDetayActivity(filmModel);
            }
        });

        viewPager2Adapter.setOnItemClickListener(new ViewPager2Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(FilmModel filmModel) {
                openFilmDetayActivity(filmModel);
            }
        });

        CategoriAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CategoriModel categoriModel) {
                openCategoryActivity(categoriModel);
            }
        });
        SuggestionAdapter.setOnItemClickListener(new FilmVerticalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FilmModel filmModel) {
                openFilmDetayActivity(filmModel);
            }
        });
        SuggestionAdapter2.setOnItemClickListener(new FilmVerticalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FilmModel filmModel) {
                openFilmDetayActivity(filmModel);
            }
        });

        //Search kısmı

        textViewSearch = findViewById(R.id.textViewSearch);
        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchResultActivity.class));
            }
        });


    }
    private void openFilmDetayActivity(FilmModel filmModel) {
        Intent intent = new Intent(MainActivity.this, FilmDetayActivity.class);
        intent.putExtra("filmId", filmModel.getId());
        startActivity(intent);
    }
    private void openCategoryActivity(CategoriModel categoriModel){
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        intent.putExtra("kategoriID",categoriModel.getId());
        startActivity(intent);
    }
    private void ViewPager(){
        FilmApi filmApi = retrofit.create(FilmApi.class);
        Call<FilmApiResponse> call = filmApi.getPopularFilms(Credentials.API_KEY,1);
        call.enqueue(new Callback<FilmApiResponse>() {
            @Override
            public void onResponse(Call<FilmApiResponse> call, Response<FilmApiResponse> response) {
                if(response.isSuccessful()){
                    FilmApiResponse filmApiResponse = response.body();
                    filmModels = filmApiResponse.getFilmler();
                    viewPager2Adapter.setFilmList(filmModels);
                    viewPager2Adapter.notifyDataSetChanged();

                    viewPager2.setClipToPadding(false);
                    viewPager2.setClipChildren(false);
                    viewPager2.setOffscreenPageLimit(3);
                    viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                    compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                    compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                        @Override
                        public void transformPage(@NonNull View page, float position) {
                            float r = 1 - Math.abs(position);
                            page.setScaleY(0.85f + r * 0.15f);
                        }
                    });

                    viewPager2.setPageTransformer(compositePageTransformer);
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
    private void BestMovie(){
        FilmApi filmApi = retrofit.create(FilmApi.class);

        Call<FilmApiResponse> call1 = filmApi.getTopRatedFilms(Credentials.API_KEY,1);
        call1.enqueue(new Callback<FilmApiResponse>() {
            @Override
            public void onResponse(Call<FilmApiResponse> call, Response<FilmApiResponse> response) {
                if(response.isSuccessful()) {
                    FilmApiResponse apiResponse = response.body();
                    filmModels = apiResponse.getFilmler();
                    bestFilmVerticalAdapter.setFilmList(filmModels);
                    bestFilmVerticalAdapter.notifyDataSetChanged();
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
    private void Categori(){
        FilmApi filmApi = retrofit.create(FilmApi.class);
        Call<CategoriApiResponse> call2 = filmApi.getCategori(Credentials.API_KEY);
        call2.enqueue(new Callback<CategoriApiResponse>() {
            @Override
            public void onResponse(Call<CategoriApiResponse> call, Response<CategoriApiResponse> response) {
                if(response.isSuccessful()){
                    CategoriApiResponse categoriApiResponse = response.body();

                    categoriModels = categoriApiResponse.getCategories();
                    CategoriAdapter.setCategoriList(categoriModels);
                    CategoriAdapter.notifyDataSetChanged();
                }
                else{
                    Log.e("API Hatası", "Hata kodu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CategoriApiResponse> call, Throwable t) {
                Log.e("API Hatası", "Hata: " + t.getMessage());
            }
        });


    }
    private void UpcomingMovie(){
        FilmApi filmApi = retrofit.create(FilmApi.class);
        Call<FilmApiResponse> call = filmApi.getUpcomingFilms(Credentials.API_KEY,1);
        call.enqueue(new Callback<FilmApiResponse>() {
            @Override
            public void onResponse(Call<FilmApiResponse> call, Response<FilmApiResponse> response) {
                if(response.isSuccessful()){
                    FilmApiResponse filmApiResponse = response.body();
                    filmModels = filmApiResponse.getFilmler();
                    UpcomingAdapter.setFilmList(filmModels);
                    UpcomingAdapter.notifyDataSetChanged();
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
    private void YouChoseTo(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference favoriCategory = FirebaseDatabase.getInstance().getReference("favoriteCategory").child(user.getUid());
        favoriCategory.child("category_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot categorySnapshot : snapshot.getChildren()){
                        String category_id = String.valueOf(categorySnapshot.getValue(Long.class));
                        Log.d("Mesajlarrrrrrr","CategoryId : "+ category_id);
                        kategoriler.add(Integer.valueOf(category_id));
                    }
                    Log.d("Mesajjjj","kategoriler"+kategoriler);
                    Collections.shuffle(kategoriler);
                    if (kategoriler == null || kategoriler.size()==1){
                        suggestionLayout.setVisibility(View.GONE);
                    }
                    else {
                        Log.d("Messssaajj","Karma"+kategoriler.subList(0,2));
                        suggestionLayout.setVisibility(View.VISIBLE);
                        FilmApi filmApi = retrofit.create(FilmApi.class);
                        Call<FilmApiResponse> call = filmApi.getSuggestion(kategoriler,Credentials.API_KEY,8);
                        call.enqueue(new Callback<FilmApiResponse>() {
                            @Override
                            public void onResponse(Call<FilmApiResponse> call, Response<FilmApiResponse> response) {
                                if (response.isSuccessful()){
                                    Log.d("Cevapppp","kategoriler"+kategoriler);

                                    FilmApiResponse apiResponse = response.body();
                                    filmModels = apiResponse.getFilmler();
                                    SuggestionAdapter.setFilmList(filmModels);
                                    SuggestionAdapter.notifyDataSetChanged();
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
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void YouChoseTo2(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference favoriFilm = FirebaseDatabase.getInstance().getReference("favoriteFilm").child(user.getUid());
        favoriFilm.child("film_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String film_id = String.valueOf(dataSnapshot.getValue(Long.class));
                        filmler.add(Integer.valueOf(film_id));
                    }
                    Collections.shuffle(filmler);
                    if (filmler == null){
                        suggestionLayout2.setVisibility(View.GONE);
                    }else {
                        suggestionLayout2.setVisibility(View.VISIBLE);
                        FilmApi filmApi = retrofit.create(FilmApi.class);
                        Call<FilmModel> call1 = filmApi.getFilmDetail(filmler.get(0),Credentials.API_KEY,"");
                        call1.enqueue(new Callback<FilmModel>() {
                            @Override
                            public void onResponse(Call<FilmModel> call, Response<FilmModel> response) {
                                if (response.isSuccessful()){
                                    FilmModel filmModel = response.body();
                                    textViewOneri2.setText("Suggestions for those who liked the production called " + filmModel.getTitle());
                                }
                            }

                            @Override
                            public void onFailure(Call<FilmModel> call, Throwable t) {

                            }
                        });
                        Call<FilmApiResponse> call = filmApi.getSuggestion2(filmler.get(0), Credentials.API_KEY);
                        call.enqueue(new Callback<FilmApiResponse>() {
                            @Override
                            public void onResponse(Call<FilmApiResponse> call, Response<FilmApiResponse> response) {
                                if (response.isSuccessful()){
                                    FilmApiResponse apiResponse = response.body();
                                    Log.d("Burakkkahhaaan", "Filmler : " + apiResponse.getFilmler());
                                    filmModels = apiResponse.getFilmler();
                                    SuggestionAdapter2.setFilmList(filmModels);
                                    SuggestionAdapter2.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<FilmApiResponse> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

