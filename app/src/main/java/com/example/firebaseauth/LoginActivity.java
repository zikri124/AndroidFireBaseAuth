package com.example.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

 private FirebaseAuth mAuth;
 private EditText inputEmail, inputPass;
 private Button btnLogin, btnRegister;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_login);

  mAuth = FirebaseAuth.getInstance();

  setView();
 }

 @Override
 public void onClick(View view) {
  if (view.getId() == btnLogin.getId()) {
   signIn();
  } else if (view.getId() == btnRegister.getId()) {
   Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
   startActivity(intent);
   finishAffinity();
  }
 }

 private void setView() {
  inputEmail = findViewById(R.id.user_email);
  inputPass = findViewById(R.id.user_pass);
  btnLogin = findViewById(R.id.button_login);
  btnRegister = findViewById(R.id.button_register);

  btnLogin.setOnClickListener(this);
  btnRegister.setOnClickListener(this);
 }

 private void signIn() {
  if (!validateForm()) {
   return;
  }

  String email = inputEmail.getText().toString();
  String pass = inputPass.getText().toString();

  mAuth.signInWithEmailAndPassword(email, pass)
          .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
             Log.d("loginStatus", "signInWithEmail:success");
             FirebaseUser user = mAuth.getCurrentUser();
             updateUI(user);
            } else {
             Log.w("loginStatus", "signInWithEmail:failure", task.getException());
             Toast.makeText(LoginActivity.this, "Failed to Sign in : "+task.getException(),
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

 private void updateUI(FirebaseUser user) {
  if (user == null) {
   inputEmail.setText("");
   inputPass.setText("");
  } else {
   Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
   startActivity(intent);
   finishAffinity();
  }
 }
}
