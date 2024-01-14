package com.example.moviapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.moviapp.Adapters.ActorAdapter;
import com.example.moviapp.Adapters.CastAdapter;
import com.example.moviapp.Adapters.CrewAdapter;
import com.example.moviapp.Adapters.GenreAdapter;
import com.example.moviapp.Data.FavoriFilm;
import com.example.moviapp.Data.MoviesToWatch;
import com.example.moviapp.Data.WatchedMovies;
import com.example.moviapp.Model.CastModel;
import com.example.moviapp.Model.CastModel2;
import com.example.moviapp.Model.CreditModel;
import com.example.moviapp.Model.CrewModel;
import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.R;
import com.example.moviapp.Response.CreditResponse;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntBiFunction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//
public class FilmDetayActivity extends AppCompatActivity {

    Retrofit retrofit;
    List<FilmModel.Genre> filmModelsGenre = new ArrayList<>();
    ToggleButton film_fav, film_izlenen, film_izlenecek;
    FilmModel filmModel;
    TextView title, puan, sure, release_date, tagline, overview;
    ImageView foto, back_btn;
    RecyclerView recyclerView_genre, recyclerView_stars, recyclerView_direction;
    GenreAdapter genreAdapter;
    ActorAdapter actorAdapter;
    CrewAdapter crewAdapter;
    List<CrewModel> crewModels = new ArrayList<>();
    List<CastModel2> castModels = new ArrayList<>();

    public int filmId;
    public int film;
    String appandend="credits";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detay);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        recyclerView_genre = findViewById(R.id.recyclerView_genre);
        recyclerView_genre.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        genreAdapter = new GenreAdapter(new ArrayList<>());
        recyclerView_genre.setAdapter(genreAdapter);
        genreAdapter.notifyDataSetChanged();

        recyclerView_stars = findViewById(R.id.stars_view);
        recyclerView_stars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        actorAdapter = new ActorAdapter(new ArrayList<>());
        recyclerView_stars.setAdapter(actorAdapter);

        recyclerView_direction = findViewById(R.id.direction_view);
        recyclerView_direction.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        crewAdapter = new CrewAdapter(new ArrayList<>());
        recyclerView_direction.setAdapter(crewAdapter);


        actorAdapter.setOnItemClickListener(new ActorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CastModel2 castModel) {
                openPersonDetayActiviy(castModel);
            }
        });
        crewAdapter.setOnItemClickListener(new CrewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CrewModel crewModel) {
                openPersonDetayActiviy2(crewModel);
            }
        });

        film_fav = findViewById(R.id.film_fav);
        film_izlenecek = findViewById(R.id.film_izlenecek);
        film_izlenen = findViewById(R.id.film_izlenen);

        back_btn = findViewById(R.id.back_btn);
        overview = findViewById(R.id.overview);
        tagline = findViewById(R.id.tagline);
        release_date = findViewById(R.id.release_date);
        sure = findViewById(R.id.sure);
        puan = findViewById(R.id.puan);
        title = findViewById(R.id.title);
        foto = findViewById(R.id.imageView6);

        back_btn.setOnClickListener(new View.OnClickListener() {
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
        if (intent == null || !intent.hasExtra("filmId")) {
            // Hatalı durum, intent null veya "filmId" ekstrası yok
            Toast.makeText(this, "Geçersiz film ID'si", Toast.LENGTH_SHORT).show();
            finish(); // Hata durumunda aktiviteyi kapat
        } else {
            // Doğru türde bir intent alındı
            filmId = intent.getIntExtra("filmId", -1);
            film = filmId;
            if (filmId != -1) {
                getFilmDetay(filmId);
            } else {
                // Hatalı durum, geçerli bir film ID'si yok
                Toast.makeText(this, "Geçersiz film ID'si", Toast.LENGTH_SHORT).show();
                finish(); // Hata durumunda aktiviteyi kapat
            }
        }

        //Favori filmler
        DatabaseReference favoriteFilm = FirebaseDatabase.getInstance().getReference("favoriteFilm");
        favoriteFilm.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FavoriFilm favoriFilm;
                if (snapshot.exists()){
                    favoriFilm = snapshot.getValue(FavoriFilm.class);
                    Log.d("FilmDetayy","Filmss : "+ favoriFilm.getFilm_id());
                    if (favoriFilm != null && favoriFilm.getFilm_id() != null){
                        Log.d("FilmDetayyy", "Filmdd" + favoriFilm.getFilm_id());
                        for (int film : favoriFilm.getFilm_id()){
                            Log.d("FilmDetayyyyy", "forrr : " + film);
                            if (film == filmId){
                                film_fav.setChecked(true);
                                break;
                            }
                            else {
                                film_fav.setChecked(false);
                            }              }  }
                    else {
                        Toast.makeText(FilmDetayActivity.this, "Null", Toast.LENGTH_SHORT).show();
                    }             }             }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        film_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                favoriteFilm.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FavoriFilm favoriFilm;
                        if (snapshot.exists()){
                            favoriFilm = snapshot.getValue(FavoriFilm.class);
                        } else {
                            favoriFilm = new FavoriFilm(new ArrayList<>());
                            Log.d("FilmDetayyyyy", favoriFilm.toString());
                        }
                        if (b && !favoriFilm.getFilm_id().contains(film)){
                            favoriFilm.getFilm_id().add(film);
                            Toast.makeText(FilmDetayActivity.this, "Add", Toast.LENGTH_SHORT).show();
                        } else if (!b) {
                            favoriFilm.getFilm_id().remove(Integer.valueOf(film));
                            Toast.makeText(FilmDetayActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                        }
                        favoriteFilm.child(user.getUid()).setValue(favoriFilm);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {  }
                });}});
        //izlenecek filmler
        DatabaseReference moviesToWatch = FirebaseDatabase.getInstance().getReference("MoviesToWatch");
        moviesToWatch.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MoviesToWatch moviesToWatchData;
                if (snapshot.exists()){
                    moviesToWatchData = snapshot.getValue(MoviesToWatch.class);
                    Log.d("Burakhaaaaaaannnn", " Filmss: " + moviesToWatchData.getFilm_id());
                    if (moviesToWatchData != null && moviesToWatchData.getFilm_id() != null){
                        Log.d("Burakhaaaaaaannnn", "Filmmm : " + moviesToWatchData.getFilm_id());
                        for (int film : moviesToWatchData.getFilm_id()){
                            if (film == filmId){
                                film_izlenecek.setChecked(true);
                                break;
                            }
                            else {
                                film_izlenecek.setChecked(false);
                            }
                        }                    }                }            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        film_izlenecek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                moviesToWatch.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MoviesToWatch moviesToWatchData ;
                        if (snapshot.exists()){
                            moviesToWatchData = snapshot.getValue(MoviesToWatch.class);
                        }else {
                            moviesToWatchData = new MoviesToWatch(new ArrayList<>());
                        }
                        if (b && !moviesToWatchData.getFilm_id().contains(film)){
                            moviesToWatchData.getFilm_id().add(film);
                            Toast.makeText(FilmDetayActivity.this, "Add", Toast.LENGTH_SHORT).show();
                        } else if (!b) {
                            moviesToWatchData.getFilm_id().remove(Integer.valueOf(film));
                            Toast.makeText(FilmDetayActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                        }
                        moviesToWatch.child(user.getUid()).setValue(moviesToWatchData);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });            }        });
        // izlenmiş filmler
        DatabaseReference watchedMovies = FirebaseDatabase.getInstance().getReference("WatchedMovies");
        watchedMovies.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WatchedMovies watchedMoviesData;
                if (snapshot.exists()){
                    watchedMoviesData = snapshot.getValue(WatchedMovies.class);
                    if (watchedMoviesData != null && watchedMoviesData.getFilm_id() != null){
                        for (int film : watchedMoviesData.getFilm_id()){
                            if (film == filmId){
                                film_izlenen.setChecked(true);
                                break;
                            }
                            else {
                                film_izlenen.setChecked(false);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        film_izlenen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                watchedMovies.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        WatchedMovies watchedMoviesData;
                        if (snapshot.exists()){
                            watchedMoviesData = snapshot.getValue(WatchedMovies.class);
                        }else {
                            watchedMoviesData = new WatchedMovies(new ArrayList<>());
                        }
                        if (b && !watchedMoviesData.getFilm_id().contains(film)){
                            watchedMoviesData.getFilm_id().add(film);
                            Toast.makeText(FilmDetayActivity.this, "Add", Toast.LENGTH_SHORT).show();
                        } else if (!b) {
                            watchedMoviesData.getFilm_id().remove(Integer.valueOf(film));
                            Toast.makeText(FilmDetayActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                        }
                        watchedMovies.child(user.getUid()).setValue(watchedMoviesData);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void getFilmDetay(int filmId) {
        FilmApi filmApi = retrofit.create(FilmApi.class);
        Call<FilmModel> call = filmApi.getFilmDetail(filmId, Credentials.API_KEY,"");
        call.enqueue(new Callback<FilmModel>() {
            @Override
            public void onResponse(Call<FilmModel> call, Response<FilmModel> response) {
                if (response.isSuccessful()) {
                    FilmModel filmModel = response.body();
                    if (filmModel != null) {
                        Log.d("FilmDetayActivity", "Genres: " + filmModel.getGenres());

                    } else {
                        Log.e("FilmDetayActivity", "filmModel null");
                    }

                    filmModelsGenre = filmModel.getGenres();
                    genreAdapter.setGenreList(filmModelsGenre);
                    genreAdapter.notifyDataSetChanged();

                    overview.setText(filmModel.getOverview());
                    tagline.setText(filmModel.getTagline());
                    release_date.setText(filmModel.getRelease_date());
                    puan.setText(String.valueOf(filmModel.getVote_average()));
                    sure.setText(String.valueOf((filmModel.getRuntime())));
                    title.setText(filmModel.getTitle());
                    Picasso.get()
                            .load("https://image.tmdb.org/t/p/w500/"+filmModel.getPoster_path())
                            .placeholder(R.drawable.install_50)   //yükleme sırasında gösterilecek
                            .error(R.drawable.error_50)   // yükleme hatasında
                            .into(foto);
                    genreAdapter.setGenreList(filmModelsGenre);
                    genreAdapter.notifyDataSetChanged();

                    // Oyuncular

                    FilmApi filmApi1 = retrofit.create(FilmApi.class);
                    Call<CreditResponse> call1 = filmApi1.getMovieCredits(filmId,Credentials.API_KEY);
                    call1.enqueue(new Callback<CreditResponse>() {
                        @Override
                        public void onResponse(Call<CreditResponse> call, Response<CreditResponse> response) {
                            if (response.isSuccessful()){
                                CreditResponse creditResponse = response.body();
                                castModels = creditResponse.getCast();
                                actorAdapter.setCastModelList(castModels);
                                actorAdapter.notifyDataSetChanged();

                                crewModels = creditResponse.getCrew();
                                crewAdapter.setCrewModels(crewModels);
                                crewAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<CreditResponse> call, Throwable t) {

                        }
                    });

                    //Yönetmen



                } else {
                    Log.e("FilmDetayActivity", "API çağrısı başarısız. Hata kodu: " + response.code());
                    Toast.makeText(FilmDetayActivity.this, "API çağrısı başarısız", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FilmModel> call, Throwable t) {
                Log.e("FilmDetayActivity", "API çağrısı başarısız. Hata: " + t.getMessage());
                Toast.makeText(FilmDetayActivity.this, "API çağrısı başarısız", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void openPersonDetayActiviy(CastModel2 castModel2){
        Intent intent = new Intent(FilmDetayActivity.this, PersonDetayActivity.class);
        intent.putExtra("personId", castModel2.getCastId());
        startActivity(intent);
    }
    private void openPersonDetayActiviy2(CrewModel crewModel){
        Intent intent = new Intent(FilmDetayActivity.this, PersonDetayActivity.class);
        intent.putExtra("personId", crewModel.getCrew_id());
        startActivity(intent);
    }
}
