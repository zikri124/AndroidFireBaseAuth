package com.example.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

 private FirebaseDatabase mDatabase;
 private FirebaseAuth mAuth;
 private EditText inputEmail, inputPass;
 private Button btnRegister, btnLogin;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_register);

  mDatabase = FirebaseDatabase.getInstance();
  mAuth = FirebaseAuth.getInstance();

  setView();
 }

 @Override
 public void onClick(View view) {
  if (view.getId() == btnRegister.getId()) {
   signUp();
  } else if (view.getId() == btnLogin.getId()) {
   Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
   startActivity(intent);
   finishAffinity();
  }
 }

 private void setView() {
  inputEmail = findViewById(R.id.user_email);
  inputPass = findViewById(R.id.user_pass);
  btnRegister = findViewById(R.id.button_register);
  btnLogin = findViewById(R.id.button_login);

  btnRegister.setOnClickListener(this);
  btnLogin.setOnClickListener(this);
 }

 private void updateUI(FirebaseUser user) {
  if (user != null) {
   Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
   startActivity(intent);
   finishAffinity();
  }
 }

 private void signUp() {
  if (!validateForm()) {
   return;
  }

  String email = inputEmail.getText().toString();
  String pass = inputPass.getText().toString();

  mAuth.createUserWithEmailAndPassword(email, pass)
          .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
             Log.d("SignUp status", "createUserWithEmail:success");
             FirebaseUser user = mAuth.getCurrentUser();
             updateUI(user);
            } else {
             Log.w("SignUp status", "createUserWithEmail:failure", task.getException());
             Toast.makeText(RegisterActivity.this, "Failed to sign up : "+task.getException(),
                     Toast.LENGTH_SHORT).show();
             updateUI(null);
            }
           }
          });
 }

 private boolean validateForm() {
  boolean result = true;
  if (inputEmail.getText().toString().equals("")) {
   inputEmail.setError("Required");
   result = false;
  }
  if (inputPass.getText().toString().equals("")) {
   inputPass.setError("Required");
   result = false;
  }

  return result;
 }
}
