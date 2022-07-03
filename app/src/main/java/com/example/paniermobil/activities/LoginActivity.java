package com.example.paniermobil.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_login);
        password = findViewById(R.id.passeword_login);

    }

public void singIn(View view){
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
    if (TextUtils.isEmpty(userEmail)){
        Toast.makeText(this, "Entre votre adresse email", Toast.LENGTH_SHORT).show();
        return;
    }
    if (TextUtils.isEmpty(userPassword)){
        Toast.makeText(this, "Entre votre modepass", Toast.LENGTH_SHORT).show();
        return;
    }

    if(userPassword.length() < 6){
        Toast.makeText(this, "modepass trop court, minimum 6 lettre!", Toast.LENGTH_SHORT).show();
        return;
    }
    auth.signInWithEmailAndPassword(userEmail,userPassword)
            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

    public void singUp(View view){
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }

}