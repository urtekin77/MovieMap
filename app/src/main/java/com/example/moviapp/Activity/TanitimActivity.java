package com.example.moviapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.moviapp.Adapters.FilmVerticalAdapter;
import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.R;
import com.example.moviapp.Response.FilmApiResponse;
import com.example.moviapp.Service.FilmApi;
import com.example.moviapp.Util.Credentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Api kontolleri get stardıd butonundan yapıldı

public class TanitimActivity extends AppCompatActivity {

    Button basla_btn;
    RecyclerView recyclerView;
    FilmVerticalAdapter filmVerticalAdapter;
    Retrofit retrofit;
    List<FilmModel> filmModels = new ArrayList<>();
    private FirebaseAuth auth;  //firebase nesnesi oluşturuldu




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanitim);


        FirebaseAuth auth=FirebaseAuth.getInstance(); //FirebaseAuth başlatıldı
        //Kullanıcı daha önceden giriş yaptı mı yapmadı mı sorgusu
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(TanitimActivity.this, MainActivity.class));
            finish();
        }

        //Retrofit & json
        Gson gson = new GsonBuilder().setLenient().create(); //Gson oluşturdu

        retrofit =new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        recyclerView = findViewById(R.id.kesfet);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TanitimActivity.this,LinearLayoutManager.HORIZONTAL,false));

        filmVerticalAdapter = new FilmVerticalAdapter(filmModels);
        recyclerView.setAdapter(filmVerticalAdapter);


        loadData();

        basla_btn=findViewById(R.id.basla_btn);

        basla_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TanitimActivity.this, LoginActivity.class));
            }
        });


    }

    private void loadData(){
        FilmApi filmApi = retrofit.create(FilmApi.class);
        Call<FilmApiResponse> call1=filmApi.getPopularFilms(Credentials.API_KEY,1);
        call1.enqueue(new Callback<FilmApiResponse>() {
            @Override
            public void onResponse(Call<FilmApiResponse> call, Response<FilmApiResponse> response) {
                if (response.isSuccessful()) {
                    FilmApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        filmModels = apiResponse.getFilmler();
                        filmVerticalAdapter.setFilmList(filmModels);
                        filmVerticalAdapter.notifyDataSetChanged();
                    }else {
                        Log.e("API Hatası", "Hata kodu: " + response.code());
                    }
                }
                //Api kontolleri yapıldı
                    for (FilmModel filmModel : filmModels) {
                        Log.d("API Yanıtı", "Başlık: " + filmModel.getTitle());
                        Log.d("API Yanıtı", "puan: " + filmModel.getVote_average());
                        Log.d("API Yanıtı", "url: " + filmModel.getPoster_path());
                    }
                }
            @Override
            public void onFailure(Call<FilmApiResponse> call, Throwable t) {
                Log.e("API Hatası", "Hata: " + t.getMessage());
            }
        });
    }

}