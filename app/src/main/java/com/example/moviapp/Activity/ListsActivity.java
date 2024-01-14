package com.example.moviapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moviapp.R;

public class ListsActivity extends AppCompatActivity {
    LinearLayout discover, profile, list;
    TextView favoriList, izlenenList, izlenecekList;
    int listeID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        discover = findViewById(R.id.discover_activity);
        list = findViewById(R.id.list_activity);
        profile = findViewById(R.id.profil_activity);

        favoriList = findViewById(R.id.favori_film_list);
        izlenecekList = findViewById(R.id.izlenecek_film_list);
        izlenenList = findViewById(R.id.izlenen_film_list);

        favoriList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listeID = 1;
                openListDetayActiviy();

            }
        });
        izlenecekList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listeID = 2;
                openListDetayActiviy();

            }
        });
        izlenenList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listeID = 3;
                openListDetayActiviy();

            }
        });




        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListsActivity.this, MainActivity.class));
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListsActivity.this, ListsActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListsActivity.this, ProfileActivity.class));
            }
        });

    }
    private void openListDetayActiviy(){
        Intent intent = new Intent(ListsActivity.this, ListeDetayActivity.class);
        intent.putExtra("listeId", listeID);
        startActivity(intent);
    }
}