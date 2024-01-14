package com.example.moviapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.moviapp.Adapters.FilmHorizontalAdapter;
import com.example.moviapp.Adapters.FilmVerticalAdapter;
import com.example.moviapp.Adapters.PeopleVerticalAdapter;
import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.Model.PeopleModel;
import com.example.moviapp.R;
import com.example.moviapp.Response.FilmApiResponse;
import com.example.moviapp.Response.PeopleApiResponse;
import com.example.moviapp.Service.FilmApi;
import com.example.moviapp.Util.Credentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultActivity extends AppCompatActivity {

    EditText editSearch;
    RecyclerView recyclerView ,people_view;
    FilmVerticalAdapter filmVerticalAdapter;
    PeopleVerticalAdapter peopleVerticalAdapter;
    Retrofit retrofit;
    List<FilmModel> filmModels = new ArrayList<>();
    List<PeopleModel> peopleModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        //Sayfa açıldığında klavye açılsın
        // EditText'in referansı alındı
        editSearch = findViewById(R.id.editSearch);
        // EditText'i odaklayın ve klavyeyi açın
        editSearch.requestFocus();

        Gson gson = new GsonBuilder().setLenient().create(); //Gson oluşturdu

        retrofit =new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        recyclerView = findViewById(R.id.search_view);
        recyclerView.setHasFixedSize(true);
       // recyclerView.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this,LinearLayoutManager.HORIZONTAL,false));
        GridLayoutManager layoutManager = new GridLayoutManager(SearchResultActivity.this, 2, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        filmVerticalAdapter = new FilmVerticalAdapter(filmModels);
        recyclerView.setAdapter(filmVerticalAdapter);
        populerFilm();

        people_view = findViewById(R.id.people_view);
        people_view.setHasFixedSize(true);
        people_view.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this,LinearLayoutManager.HORIZONTAL,false));

        peopleVerticalAdapter = new PeopleVerticalAdapter(peopleModels);
        people_view.setAdapter(peopleVerticalAdapter);
        popularPeople();


        String query = editSearch.getText().toString();

        filmVerticalAdapter.setOnItemClickListener(new FilmVerticalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FilmModel filmModel) {
                openFilmDetayActivity(filmModel);
            }
        });

        peopleVerticalAdapter.setOnItemClickListener(new PeopleVerticalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PeopleModel peopleModel) {
                openPersonActivity(peopleModel);
            }


        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query= charSequence.toString();
                searchFilm(query);
                if (query.length()>=2){
                    searchPeople(query);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void openFilmDetayActivity(FilmModel filmModel) {
        Intent intent = new Intent(SearchResultActivity.this, FilmDetayActivity.class);
        intent.putExtra("filmId", filmModel.getId());
        startActivity(intent);
    }
    private void openPersonActivity(PeopleModel peopleModel) {
        Intent intent = new Intent(SearchResultActivity.this, PersonDetayActivity.class);
        intent.putExtra("personId", peopleModel.people_id);
        startActivity(intent);
    }



    private void searchFilm(String query){
        FilmApi filmApi = retrofit.create(FilmApi.class);

        Call<FilmApiResponse> call = filmApi.getSearchFilm(Credentials.API_KEY,query);
        call.enqueue(new Callback<FilmApiResponse>() {
            @Override
            public void onResponse(Call<FilmApiResponse> call, Response<FilmApiResponse> response) {
                if(response.isSuccessful()){
                    FilmApiResponse apiResponse = response.body();
                    filmModels = apiResponse.getFilmler();
                    filmVerticalAdapter.setFilmList(filmModels);
                    filmVerticalAdapter.notifyDataSetChanged();
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
    private void searchPeople(String query){
        FilmApi filmApi = retrofit.create(FilmApi.class);
        Call<PeopleApiResponse> call = filmApi.getSearchPeople(Credentials.API_KEY, query);
        call.enqueue(new Callback<PeopleApiResponse>() {
            @Override
            public void onResponse(Call<PeopleApiResponse> call, Response<PeopleApiResponse> response) {
                if (response.isSuccessful()){
                    PeopleApiResponse peopleApiResponse = response.body();
                    peopleModels = peopleApiResponse.getPeople();
                    peopleVerticalAdapter.setPeopleModelList(peopleModels);
                    peopleVerticalAdapter.notifyDataSetChanged();

                }
                else {
                    Log.e("API Hatası", "Hata kodu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PeopleApiResponse> call, Throwable t) {
                Log.e("API Hatası", "Hata: " + t.getMessage());
            }
        });
    }

    private void populerFilm(){
        FilmApi filmApi = retrofit.create(FilmApi.class);
        Call<FilmApiResponse> call = filmApi.getPopularFilms(Credentials.API_KEY,1);
        call.enqueue(new Callback<FilmApiResponse>() {
            @Override
            public void onResponse(Call<FilmApiResponse> call, Response<FilmApiResponse> response) {
                if(response.isSuccessful()){
                    FilmApiResponse apiResponse= response.body();
                    filmModels = apiResponse.getFilmler();
                    filmVerticalAdapter.setFilmList(filmModels);
                    filmVerticalAdapter.notifyDataSetChanged();
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
    private void popularPeople(){
        FilmApi filmApi = retrofit.create(FilmApi.class);
        Call<PeopleApiResponse> call = filmApi.getPopularPeople(Credentials.API_KEY,1);
        call.enqueue(new Callback<PeopleApiResponse>() {
            @Override
            public void onResponse(Call<PeopleApiResponse> call, Response<PeopleApiResponse> response) {
                if (response.isSuccessful()){
                    PeopleApiResponse peopleApiResponse = response.body();
                    peopleModels = peopleApiResponse.getPeople();
                    peopleVerticalAdapter.setPeopleModelList(peopleModels);
                    peopleVerticalAdapter.notifyDataSetChanged();
                }
                else {
                    Log.e("API Hatası", "Hata kodu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PeopleApiResponse> call, Throwable t) {
                Log.e("API Hatası", "Hata: " + t.getMessage());
            }
        });

    }

}