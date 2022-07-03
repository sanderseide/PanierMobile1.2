package com.example.paniermobil.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paniermobil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText name,email,password,phone;

    private FirebaseAuth auth;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgistration);


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            finish();
        }
        name = findViewById(R.id.name_reg);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.passeword_reg);
        phone = findViewById(R.id.phone_reg);

        sharedPreferences = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
        boolean isFirsTime = sharedPreferences.getBoolean("firsTime", true);

        if (isFirsTime){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firsTime",false);
            editor.commit();

            Intent intent = new Intent(RegistrationActivity.this,OnBoardingActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void signup(View view){

        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userPhone = phone.getText().toString();

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Entre votre nom", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Entre votre adresse email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Entre votre modepass", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPhone)){
            Toast.makeText(this, "Entre votre numero de telephone", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userPassword.length() < 6){
            Toast.makeText(this, "modepass trop court, minimum 6 lettre!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this, "Successfullu Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        }else{
                            Toast.makeText(RegistrationActivity.this, "Registered Failled"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //startActivity(new Intent(RegistrationActivity.this, MainActivity.class));

    }
    public void singin(View view){
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));

    }

}