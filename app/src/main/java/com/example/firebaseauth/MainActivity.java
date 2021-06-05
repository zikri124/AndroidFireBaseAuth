package com.example.firebaseauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

 FirebaseAuth mAuth;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);

  mAuth = FirebaseAuth.getInstance();
 }

 @Override
 public void onStart() {
  super.onStart();
  FirebaseUser currentUser = mAuth.getCurrentUser();
  Intent intent;
  if (currentUser == null) {
   intent = new Intent(MainActivity.this, LoginActivity.class);
  } else {
   intent = new Intent(MainActivity.this, HomeActivity.class);
  }

  new Handler().postDelayed(new Runnable() {
   @Override
   public void run() {
    startActivity(intent);
    finishAffinity();
   }
  }, 1000);
 }
}