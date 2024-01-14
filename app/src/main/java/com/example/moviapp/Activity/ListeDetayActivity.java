package com.example.moviapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviapp.Adapters.FilmHorizontalAdapter;
import com.example.moviapp.Adapters.FilmVerticalAdapter;
import com.example.moviapp.Model.FilmModel;

import com.example.moviapp.R;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListeDetayActivity extends AppCompatActivity {
    Retrofit retrofit;
    String appandend = "credits";

    int listeID;
    ImageView backBtn;
    TextView listeAdi;
    RecyclerView recyclerViewListe;
    FilmHorizontalAdapter filmHorizontalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_detay);

        listeAdi = findViewById(R.id.listAdi);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("listeId")){
            Toast.makeText(this, "Geçersiz List ID'si", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            listeID = intent.getIntExtra("listeId", -1);

        }
        Gson gson = new GsonBuilder().setLenient().create(); //Gson oluşturdu

        retrofit =new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        backBtn = findViewById(R.id.imageView2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerViewListe = findViewById(R.id.list);
        recyclerViewListe.setLayoutManager(new LinearLayoutManager(this));
        filmHorizontalAdapter = new FilmHorizontalAdapter(new ArrayList<>());
        recyclerViewListe.setAdapter(filmHorizontalAdapter);

        filmHorizontalAdapter.setOnItemClickListener(new FilmVerticalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FilmModel filmModel) {
                openFilmDetayActivity(filmModel);
            }
        });

        DatabaseReference favoriteFilm = FirebaseDatabase.getInstance().getReference("favoriteFilm").child(user.getUid());
        DatabaseReference moviesToWatch = FirebaseDatabase.getInstance().getReference("MoviesToWatch").child(user.getUid());
        DatabaseReference watchedMovies = FirebaseDatabase.getInstance().getReference("WatchedMovies").child(user.getUid());

        if (listeID == 1){
            listeAdi.setText("My Favorite List");
            favoriteFilm.child("film_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String filmId = String.valueOf(dataSnapshot.getValue(Long.class));
                            Log.d("ListeDetayyy","Film ID : " + filmId);
                            FilmApi filmApi = retrofit.create(FilmApi.class);
                            Call<FilmModel> call = filmApi.getFilmDetail(Integer.parseInt(filmId), Credentials.API_KEY,"");
                            call.enqueue(new Callback<FilmModel>() {
                                @Override
                                public void onResponse(Call<FilmModel> call, Response<FilmModel> response) {
                                    if (response.isSuccessful()){
                                        FilmModel filmModel = response.body();
                                        if (filmModel != null) {
                                            Log.d("Listelerr", "Film avarage : " + filmModel.getVote_average());
                                            Log.d("Listelerr", "Film url : " + filmModel.getPoster_path());
                                            filmHorizontalAdapter.addFilm(filmModel);
                                            filmHorizontalAdapter.notifyDataSetChanged();
                                        }
                                        else {
                                            Log.e("Listelerr", "FilmModel is null");
                                        }
                                    }
                                    else {
                                        Log.e("List12333", "Response not successful. Code: " + response.code());
                                    }
                                }
                                @Override
                                public void onFailure(Call<FilmModel> call, Throwable t) {
                                    Log.e("Lis123", "Film details request failed", t);
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
        else if (listeID ==2 ) {
            listeAdi.setText("Movies To Watch");
            moviesToWatch.child("film_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String filmId = String.valueOf(dataSnapshot.getValue(Long.class));
                            FilmApi filmApi = retrofit.create(FilmApi.class);
                            Call<FilmModel> call = filmApi.getFilmDetail(Integer.parseInt(filmId),Credentials.API_KEY,"");
                            call.enqueue(new Callback<FilmModel>() {
                                @Override
                                public void onResponse(Call<FilmModel> call, Response<FilmModel> response) {
                                    if (response.isSuccessful()){
                                        FilmModel filmModel = response.body();
                                        if (filmModel != null){
                                            filmHorizontalAdapter.addFilm(filmModel);
                                            filmHorizontalAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<FilmModel> call, Throwable t) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (listeID == 3) {
            listeAdi.setText("Watched Movies");
            watchedMovies.child("film_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String filmId = String.valueOf(dataSnapshot.getValue(Long.class));
                            FilmApi filmApi = retrofit.create(FilmApi.class);
                            Call<FilmModel> call = filmApi.getFilmDetail(Integer.parseInt(filmId),Credentials.API_KEY,"");
                            call.enqueue(new Callback<FilmModel>() {
                                @Override
                                public void onResponse(Call<FilmModel> call, Response<FilmModel> response) {
                                    if (response.isSuccessful()){
                                        FilmModel filmModel = response.body();
                                        if (filmModel != null){
                                            filmHorizontalAdapter.addFilm(filmModel);
                                            filmHorizontalAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<FilmModel> call, Throwable t) {

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

       /*

*/


    }
    private void openFilmDetayActivity(FilmModel filmModel) {
        Intent intent = new Intent(ListeDetayActivity.this, FilmDetayActivity.class);
        intent.putExtra("filmId", filmModel.getId());
        startActivity(intent);
    }
}