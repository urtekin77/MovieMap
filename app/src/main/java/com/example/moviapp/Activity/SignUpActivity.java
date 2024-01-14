package com.example.moviapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.moviapp.Adapters.AvatarAdapter;
import com.example.moviapp.Data.User;
import com.example.moviapp.Data.Avatar;
import com.example.moviapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    TextView singup_hata;
    ImageView foto;
    TextView password_error;
    int secilenAvatarResimId;
    boolean isEmailErrorShown = false;

    boolean isPasswordErrorShown = false;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //firebase nesnesi oluşturuldu
        FirebaseAuth auth = FirebaseAuth.getInstance(); //FirebaseAuth başlatıldı
        databaseReference = FirebaseDatabase.getInstance().getReference("users");


        EditText kullanici_ad = findViewById(R.id.kullanici_ad);
        EditText singup_email = findViewById(R.id.singup_email);
        TextView singup_password = findViewById(R.id.singup_password);
        foto = findViewById(R.id.kullanici_resim);
        singup_hata = findViewById(R.id.singup_hata);
        password_error = findViewById(R.id.password_error);

        ImageView kullanici_resim = findViewById(R.id.kullanici_resim);

        Button sing_up = findViewById(R.id.sing_up);


        //eposta formatı kontrol edildi
        singup_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sing_up.setEnabled(false);
                //Text değişmeden önce
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Text değişirken
                String email = charSequence.toString(); // Eposta değişkeni güncellendi

                //Patterns ile email adresinin doğruluğunu kontrol edildi
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    clearEmailError();
                    sing_up.setEnabled(true);
                }else{
                    setEmailError("Please enter a valid email!");
                    sing_up.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Text değiştikten sonra

            }
        });

        //güçlü şifre kontrolü
        //(?=.*[a-z]) küçük harf ;(?=.*[A-Z]) büyük harf (?=.*\\d) rakam
        //Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$").matcher(password).matches()
        singup_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sing_up.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String password = editable.toString();
                if(Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$").matcher(password).matches()){
                    clearPasswordError();
                    sing_up.setEnabled(true);
                }else{
                    setPasswordError("Enter a strong password! Your password must contain at least " +
                            "one uppercase letter, lowercase letter, number and special character " +
                            "and must be 8 characters long.");
                    sing_up.setEnabled(false);
                }

            }
        });

        kullanici_resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarSecimDialogunuGoster();
            }
        });
        //Firebase işlemleri
        sing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // kullanıcıdan girişler alınıdı
                // trim() methodu ile boşluklar kaldırıldı
                String name = kullanici_ad.getText().toString().trim();
                String email = singup_email.getText().toString().trim();
                String password = singup_password.getText().toString().trim();

                if (password.isEmpty() || email.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please enter your information!",
                            Toast.LENGTH_LONG).show();

                }
                else {
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            String userId = authResult.getUser().getUid();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                            User user = new User(name, email, userId, String.valueOf(secilenAvatarResimId));
                            databaseReference.child(userId).setValue(user);
                            Toast.makeText(SignUpActivity.this,"You have signup successfully!",Toast.LENGTH_LONG).show();

                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            finish();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });




    }

    public void avatarSecimDialogunuGoster() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Avatar");

        final List<Avatar> avatarList = Avatar.getAvatarList();

        AvatarAdapter avatarAdapter = new AvatarAdapter(avatarList, new AvatarAdapter.OnAvatarClickListener() {
            @Override
            public void onAvatarClick(int position) {
                secilenAvatarResimId = avatarList.get(position).getAvatarResourceId();
                foto.setImageResource(secilenAvatarResimId);
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


    private void setEmailError(String errorMessage) {
        singup_hata.setText(errorMessage);
        singup_hata.setVisibility(View.VISIBLE);
        isEmailErrorShown = true;
    }

    public void clearEmailError() {
        singup_hata.setText("");
        singup_hata.setVisibility(View.GONE);
        isEmailErrorShown = false;
    }
    private void setPasswordError(String errorMessage) {
        password_error.setText(errorMessage);
        password_error.setVisibility(View.VISIBLE);
        isPasswordErrorShown = true;
    }

    private void clearPasswordError() {
        password_error.setText("");
        password_error.setVisibility(View.GONE);
        isPasswordErrorShown = false;
    }

}