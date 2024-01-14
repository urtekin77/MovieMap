package com.example.moviapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviapp.Adapters.AvatarAdapter;
import com.example.moviapp.Adapters.CategoryAdapter;
import com.example.moviapp.Adapters.FavoriteCategoryAdapter;
import com.example.moviapp.Data.Avatar;
import com.example.moviapp.Data.Message;
import com.example.moviapp.Data.User;
import com.example.moviapp.Model.CategoriModel;
import com.example.moviapp.R;
import com.example.moviapp.Response.CategoriApiResponse;
import com.example.moviapp.Service.FilmApi;
import com.example.moviapp.Util.Credentials;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    LinearLayout discover, profile, list;
    List<CategoriModel> categoriModels = new ArrayList<>();

    LinearLayout editLayout, detayEdit, messageLayout, deleteLayout;
    TextView logout, kullanici_ad, kullanici_email, editProfile,error;
    ImageView profileAvatar , editAvatar;
    Button updateBtn , updateBtn2, updateBtn3, editMessageBtn, account_delete, give_up;
    DatabaseReference databaseReference, databaseReference2, favoriteCategory;
    EditText editUser, editPassword, editSendMessage;
    RecyclerView favCategory;
    CategoryAdapter CategoriAdapter;
    CategoriModel categoriModel;
    Retrofit retrofit;

    boolean isPasswordErrorShown = false;
    public ArrayList<String> matchingCategories = new ArrayList<>();

    public ArrayList<String> favCategoryList = new ArrayList<>();

    int avatarId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editLayout = findViewById(R.id.editLayout);
        detayEdit = findViewById(R.id.detayEdit);
        editProfile = findViewById(R.id.editProfile);
        error = findViewById(R.id.error);

        //kullanıcı bilgilerini alma
        profileAvatar = findViewById(R.id.profileAvatar);
        kullanici_ad = findViewById(R.id.profile_adi);
        kullanici_email = findViewById(R.id.profile_email);

        // güncelleme

        updateBtn = findViewById(R.id.btnUpdate);
        editUser = findViewById(R.id.edit_username);

        updateBtn2 = findViewById(R.id.btnUpdate2);
        editPassword = findViewById(R.id.edit_password);

        updateBtn3 = findViewById(R.id.btnUpdate3);
        editAvatar = findViewById(R.id.edit_avatar);

        //mesaj gönderme
        messageLayout = findViewById(R.id.MessageLayout);
        editSendMessage = findViewById(R.id.editSendMessage);
        editMessageBtn = findViewById(R.id.sendMessageBtn);

        // account delete
        deleteLayout = findViewById(R.id.deleteLayout);
        account_delete = findViewById(R.id.account_delete);
        give_up = findViewById(R.id.give_up);


        Gson gson = new GsonBuilder().setLenient().create(); //Gson oluşturdu

        retrofit =new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        //kullanıcı bilgierlini yazdırma
        if (user != null){
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("users").child(user.getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        User user = snapshot.getValue(User.class);
                        if (user != null){
                            int avatarId = getResources().getIdentifier(user.getUser_avatar(),"drawable",getPackageName());
                            profileAvatar.setImageResource(avatarId);
                            kullanici_ad.setText(user.getUser_name());
                            kullanici_email.setText(user.getUser_email());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Veritabanı okuma hatası
                    Toast.makeText(ProfileActivity.this, "Error reading user data", Toast.LENGTH_SHORT).show();
                }
            });

        }

        //user name güncelleme
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = editUser.getText().toString().trim();
                if (userName != null){
                    updateBtn.setEnabled(true);
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .build();

                    user.updateProfile(profileChangeRequest);

                    databaseReference = FirebaseDatabase.getInstance()
                            .getReference("users");
                    databaseReference.child(user.getUid()).child("user_name").setValue(userName);
                    Toast.makeText(ProfileActivity.this, "Username updated successfully.", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateBtn.setEnabled(false);

                }

            }
        });
        //şifre güncelleme
        updateBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = editPassword.getText().toString().trim();
                user.updatePassword(newPassword);
                Toast.makeText(ProfileActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
            }
        });
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateBtn2.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String password = editable.toString();

                if(Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$").matcher(password).matches()){
                    clearPasswordError();
                    updateBtn2.setEnabled(true);
                }else{
                    setPasswordError("Enter a strong password! Your password must contain at least " +
                            "one uppercase letter, lowercase letter, number and special character " +
                            "and must be 8 characters long.");
                    updateBtn2.setEnabled(false);
                }
            }
        });

        // avatar güncelleme
        editAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAvatar();
            }
        });
        updateBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(String.valueOf(avatarId)))
                        .build();

                user.updateProfile(profileChangeRequest);

                databaseReference = FirebaseDatabase.getInstance().getReference("users");
                databaseReference.child(user.getUid()).child("user_avatar").setValue(String.valueOf(avatarId));
            }
        });

        // mesaj gönderme işelvi
        editMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editSendMessage.getText().toString();
                String userId = auth.getUid();


                if (message != null){
                    databaseReference2 = FirebaseDatabase.getInstance().getReference("messages");
                    String messageId = databaseReference2.push().getKey();
                    Message message1 = new Message(userId,message);
                    databaseReference2.child(messageId).setValue(message1);
                    Toast.makeText(ProfileActivity.this,"The message has been delivered. Thanks.",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ProfileActivity.this,"Enter text!",Toast.LENGTH_LONG).show();
                }
            }
        });

        //favori kategorileri gösterme

        favoriteCategory = FirebaseDatabase.getInstance().getReference("favoriteCategory").child(user.getUid());
        FavoriteCategoryAdapter favoriteCategoryAdapter = new FavoriteCategoryAdapter(retrofit,matchingCategories);
        favCategory = findViewById(R.id.favCategory);
        favCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        favCategory.setAdapter(favoriteCategoryAdapter);
        favoriteCategory.child("category_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                        String category_id = String.valueOf(categorySnapshot.getValue(Long.class));
                        Log.d("Mesaj", "Category ID: " + category_id);
                        favCategoryList.add(category_id);
                    }
                    FilmApi filmApi = retrofit.create(FilmApi.class);
                    Call<CategoriApiResponse> call = filmApi.getCategori(Credentials.API_KEY);
                    call.enqueue(new Callback<CategoriApiResponse>() {
                        @Override
                        public void onResponse(Call<CategoriApiResponse> call, Response<CategoriApiResponse> response) {
                            CategoriApiResponse categoriApiResponse = response.body();
                            categoriModels = categoriApiResponse.getCategories();
                            for (String categoryId : favCategoryList) {
                                for (CategoriModel categoriModel : categoriModels) {
                                    // Eşleşme bulunduğunda ismi yeni listeye ekle
                                    if (String.valueOf(categoriModel.getId()).equals(categoryId)) {
                                        matchingCategories.add(categoriModel.getName());

                                        break;  // Eşleşme bulunduğunda iç içe döngüden çık
                                    }
                                }
                            }
                            for (String matchedCategory : matchingCategories) {
                                Log.d("Mesajlarr", "Matching Category: " + matchedCategory);
                            }
                            favoriteCategoryAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(Call<CategoriApiResponse> call, Throwable t) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // account delete
        give_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        account_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("MoviesToWatch");
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("favoriteCategory");
                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("favoriteFilm");
                DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("WatchedMovies");
                DatabaseReference databaseReference5 = FirebaseDatabase.getInstance().getReference("users");
                databaseReference1.child(user.getUid()).removeValue();
                databaseReference2.child(user.getUid()).removeValue();
                databaseReference3.child(user.getUid()).removeValue();
                databaseReference4.child(user.getUid()).removeValue();
                databaseReference5.child(user.getUid()).removeValue();
                user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProfileActivity.this, "Account Delete", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        startActivity(new Intent(ProfileActivity.this, TanitimActivity.class));
                        finish();
                    }
                });
            }
        });


        // uygulamadan çıkış işlevi
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



        //bottom navigator
        discover = findViewById(R.id.discover_activity);
        list = findViewById(R.id.list_activity);
        profile = findViewById(R.id.profil_activity);

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ListsActivity.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            }

        });

    }

    public void expand(View view) {
        int v = (detayEdit.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(editLayout, new AutoTransition());
        detayEdit.setVisibility(v);
    }
    public void expand2(View view) {
        int x = (messageLayout.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(messageLayout, new AutoTransition());
        messageLayout.setVisibility(x);
    }
    public void expand3(View view) {
        int y = (favCategory.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(favCategory, new AutoTransition());
        favCategory.setVisibility(y);
    }
    public void expand4(View view) {
        int w = (deleteLayout.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(deleteLayout, new AutoTransition());
        deleteLayout.setVisibility(w);
    }
    private void selectAvatar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Avatar");

        final List<Avatar> avatarList = Avatar.getAvatarList();

        AvatarAdapter avatarAdapter = new AvatarAdapter(avatarList, new AvatarAdapter.OnAvatarClickListener() {
            @Override
            public void onAvatarClick(int position) {
                avatarId = avatarList.get(position).getAvatarResourceId();
                editAvatar.setImageResource(avatarId);
            }
        });
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setAdapter(avatarAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        builder.setView(recyclerView);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void setPasswordError(String errorMessage) {
        error.setText(errorMessage);
        error.setVisibility(View.VISIBLE);
        isPasswordErrorShown = true;
    }
    private void clearPasswordError() {
        error.setText("");
        error.setVisibility(View.GONE);
        isPasswordErrorShown = false;
    }



}