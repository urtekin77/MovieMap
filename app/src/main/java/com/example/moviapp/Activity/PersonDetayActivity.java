package com.example.moviapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviapp.Adapters.CastAdapter;
import com.example.moviapp.Adapters.FilmHorizontalAdapter;
import com.example.moviapp.Adapters.FilmVerticalAdapter;
import com.example.moviapp.Model.CastModel;
import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.Model.PeopleModel;
import com.example.moviapp.R;
import com.example.moviapp.Response.CastApiResponse;
import com.example.moviapp.Service.FilmApi;
import com.example.moviapp.Util.Credentials;
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

public class PersonDetayActivity extends AppCompatActivity {

    ImageView personFoto, backBtn;
    TextView personAd,personPlace, knowForPerson, personBirthday, personPopularity
            ,personBiography;

    RecyclerView knowForMovies;
    List<CastModel> filmModels = new ArrayList<>();

    CastAdapter castAdapter;

    Retrofit retrofit;
    String appandend="credits";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detay);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });

        knowForMovies = findViewById(R.id.knownForMovies);
        knowForMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        castAdapter = new CastAdapter(filmModels);
        knowForMovies.setAdapter(castAdapter);

        knowForPerson = findViewById(R.id.know_for_person);
        personBirthday = findViewById(R.id.personBirthday);
        personPopularity = findViewById(R.id.personPopularity);
        personBiography = findViewById(R.id.personBiography);
        personPlace = findViewById(R.id.person_place);
        personAd = findViewById(R.id.personAd);
        personFoto = findViewById(R.id.personFoto);

        Gson gson = new GsonBuilder().setLenient().create(); //Gson oluşturdu

        retrofit = new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        castAdapter.setOnItemClickListener(new CastAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CastModel castModel) {
                Intent intent = new Intent(PersonDetayActivity.this, FilmDetayActivity.class);
                intent.putExtra("filmId", castModel.getId());
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("personId")){
            Toast.makeText(this, "Geçersiz person ID'si", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            int personId = intent.getIntExtra("personId", -1);
            if(personId != -1){
                getPersonDetay(personId);
            }
            else {
                Toast.makeText(this, "GeçersizPersonId", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void getPersonDetay(int personId) {
        FilmApi filmApi = retrofit.create(FilmApi.class);
        Call<PeopleModel> call = filmApi.getPersonDetail(personId,Credentials.API_KEY);
        call.enqueue(new Callback<PeopleModel>() {
            @Override
            public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                if (response.isSuccessful()){
                    PeopleModel peopleModel = response.body();
                    if (peopleModel != null){
                        Log.d("Robertt", "idsii: "+ personId);
                        Log.d("PersonDetayActivity","person ad"+ peopleModel.getPeople_name());
                        Picasso.get()
                                .load("https://image.tmdb.org/t/p/w500/"+peopleModel.getPeople_profile_path())
                                .placeholder(R.drawable.install_50)   //yükleme sırasında gösterilecek
                                .error(R.drawable.error_50)   // yükleme hatasında
                                .into(personFoto);
                        personAd.setText(peopleModel.getPeople_name());
                        knowForPerson.setText(peopleModel.getKnow_for_department());
                        personPlace.setText(peopleModel.getPerson_place_of_birth());
                        personBiography.setText(peopleModel.getPerson_biography());
                        personPopularity.setText(String.valueOf(peopleModel.getPeople_popularity()));
                        personBirthday.setText(peopleModel.getPerson_birthday());
                    }
                    else {
                        Log.e("PersonDetayActivity", "filmModel null");
                    }
                }
            }
            @Override
            public void onFailure(Call<PeopleModel> call, Throwable t) {
                Log.e("FilmPersonActivity", "API çağrısı başarısız. Hata: " + t.getMessage());
                Toast.makeText(PersonDetayActivity.this, "API çağrısı başarısız", Toast.LENGTH_SHORT).show();
            }
        });


        Call<CastApiResponse> call1 = filmApi.getPeopleMovie(personId,Credentials.API_KEY);
        call1.enqueue(new Callback<CastApiResponse>() {
            @Override
            public void onResponse(Call<CastApiResponse> call, Response<CastApiResponse> response) {
                if (response.isSuccessful()){
                    CastApiResponse castApiResponse = response.body();
                    if (castApiResponse != null){
                       filmModels = castApiResponse.getCast();
                       castAdapter.setCastModelList(filmModels);
                       castAdapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(PersonDetayActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CastApiResponse> call, Throwable t) {

            }
        });
    }
}