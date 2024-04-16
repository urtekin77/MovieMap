package com.example.moviapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviapp.R;
import com.example.moviapp.R.id;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView email_hata;
    private boolean isEmailErrorShown = false;
    private FirebaseAuth auth;  //firebase nesnesi oluşturuldu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance(); //FirebaseAuth başlatıldı

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        email_hata = findViewById(R.id.email_hata);

 // log in butonu basıldığında gerçekleşecek olay

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");


        Button giris_btn = findViewById(R.id.button);
        giris_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_email = email.getText().toString().trim();
                String login_password = password.getText().toString().trim();

                if (login_email.isEmpty() || login_password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
               else {
                    auth.signInWithEmailAndPassword(login_email,login_password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = auth.getCurrentUser();
                                Toast.makeText(LoginActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                            else {
                                Exception exception = task.getException();
                                //Hatalı şifre girildiğinde
                                if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                    // Geçersiz şifre
                                    Toast.makeText(LoginActivity.this, "Invalid password. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                                else if (exception instanceof FirebaseAuthInvalidUserException) {
                                    // Kullanıcı bulunamadı
                                    Toast.makeText(LoginActivity.this, "User not found. Please check your email address.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // Diğer hatalar
                                    Toast.makeText(LoginActivity.this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });

// uye ol metnine basıldığında gerçekleşecek olay
        TextView uye_ol = findViewById(R.id.uye_ol);
        uye_ol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        // Şifre unutulduğunda yapılacaklar
        TextView forgetPassword = findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot,null);
                EditText emailBox = dialogView.findViewById(id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString().trim();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(LoginActivity.this,"Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this,"Chech your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this,"Unable to send, please try again later",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(id.btnCanel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });

    }
}


