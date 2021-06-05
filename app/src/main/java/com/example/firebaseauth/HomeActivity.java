package com.example.firebaseauth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

 FirebaseDatabase mDatabase;
 FirebaseAuth mAuth;
 RVAdapter adapter;
 RVAdapter.RecyclerViewClickListener listener;
 ArrayList<DataRV> DataRVList;
 Button addStudent;
 ImageButton logout;
 RecyclerView rv;
 TextView emaiText;
 EditText inputNim, inputNama, inputKelas;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_home);

  mDatabase = FirebaseDatabase.getInstance();
  mAuth = FirebaseAuth.getInstance();

  setView();
 }

 @Override
 public void onStart() {
  super.onStart();
  check();
 }

 public void setView() {
  logout = findViewById(R.id.btn_logout);
  addStudent = findViewById(R.id.button_add);
  rv = findViewById(R.id.rv_students);
  emaiText = findViewById(R.id.email_user);

  logout.setOnClickListener(this);
  addStudent.setOnClickListener(this);
 }

 public void check() {
  FirebaseUser currentUser = mAuth.getCurrentUser();
  if (currentUser == null) {
   Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
   startActivity(intent);
   finishAffinity();
  } else {
   String email = currentUser.getEmail();
   emaiText.setText(email);
   getStudentsData();
  }
 }

 @Override
 public void onClick(View view) {
  if (view.getId() == logout.getId()) {
   mAuth.signOut();
   check();
  } else  if (view.getId() == addStudent.getId()) {
   showCreateDialog();
  }
 }

 private void setOnClickListener() {
  listener = new RVAdapter.RecyclerViewClickListener() {
   @Override
   public void onClick(View view, int position) {
    String nimStudent = DataRVList.get(position).getNim();
    showOptDialog(nimStudent, position);
   }
  };
 }

 private void writeStudentData(String nim, String nama, String kelas) {
  DataRV student = new DataRV(nim, nama, kelas);
  mDatabase.getReference("Students").child(nim).setValue(student);
 }

 private void getStudentsData() {
  mDatabase.getReference().child("Students").addValueEventListener(new ValueEventListener() {
   @Override
   public void onDataChange(DataSnapshot dataSnapshot) {

    DataRVList = new ArrayList<>();
    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

     DataRV student = noteDataSnapshot.getValue(DataRV.class);
     student.setNim(noteDataSnapshot.getKey());

     DataRVList.add(student);
     setAdapter();
    }
   }

   @Override
   public void onCancelled(DatabaseError databaseError) {
    Toast.makeText(HomeActivity.this,"Error to load data, try to load again", Toast.LENGTH_SHORT).show();
    System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
    getStudentsData();
   }
  });
 }

 public void deleteStudent(String nim) {
  mDatabase.getReference().child("Students").child(nim).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
   @Override
   public void onSuccess(Void aVoid) {
    Toast.makeText(HomeActivity.this,"Success delete", Toast.LENGTH_SHORT).show();
    check();
   }
  });
 }

 public void setAdapter() {
  setOnClickListener();
  adapter = new RVAdapter(DataRVList, listener);
  RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
  rv.setLayoutManager(layoutManager);
  rv.setAdapter(adapter);
 }

 private void showOptDialog(String nim, int position) {
  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
          this);

  alertDialogBuilder
          .setMessage("Do you want to edit or delete the data?")
          .setCancelable(false)
          .setPositiveButton("Delete",new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog,int id) {
            showDelDialog(nim);
           }
          })
          .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog,int id) {
            dialog.cancel();
           }
          })

          .setNegativeButton("Edit",new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
            showEditDialog(position);
           }
          });

  AlertDialog alertDialog = alertDialogBuilder.create();

  alertDialog.show();
 }

 private void showCreateDialog() {
  View dialogView = getLayoutInflater().inflate(R.layout.form_data_student, null);
  inputNim = dialogView.findViewById(R.id.txt_nim);
  inputNama = dialogView.findViewById(R.id.txt_nama);
  inputKelas = dialogView.findViewById(R.id.txt_kelas);

  AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
  dialog.setView(dialogView);
  dialog.setCancelable(false);
  dialog.setTitle("Add New Data");

  dialog.setPositiveButton("Add",new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog,int id) {
            if (!validateForm()) {
             return;
            }
            String nim = inputNim.getText().toString();
            String nama = inputNama.getText().toString();
            String kelas = inputKelas.getText().toString();
            writeStudentData(nim, nama, kelas);
           }
          })
          .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
           }
          });

  dialog.show();
 }

 private void showEditDialog(int position) {
  String nimStudent = DataRVList.get(position).getNim();
  String nameStudent = DataRVList.get(position).getNama();
  String kelasStudent = DataRVList.get(position).getKelas();

  View dialogView = getLayoutInflater().inflate(R.layout.form_data_student, null);
  inputNim = dialogView.findViewById(R.id.txt_nim);
  inputNama = dialogView.findViewById(R.id.txt_nama);
  inputKelas = dialogView.findViewById(R.id.txt_kelas);

  AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
  dialog.setView(dialogView);
  dialog.setCancelable(false);
  dialog.setTitle("Edit Data");

  inputNim.setText(nimStudent);
  inputNama.setText(nameStudent);
  inputKelas.setText(kelasStudent);

  inputNim.setEnabled(false);

  dialog.setPositiveButton("Edit",new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog,int id) {
            if (!validateForm()) {
             return;
            }

            String nim = inputNim.getText().toString();
            String nama = inputNama.getText().toString();
            String kelas = inputKelas.getText().toString();
            writeStudentData(nim, nama, kelas);
           }
          })
          .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
           }
          });

  dialog.show();
 }

 private void showDelDialog(String nim) {
  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
          this);

  alertDialogBuilder
          .setMessage("Are you sure to delete the data?")
          .setCancelable(false)
          .setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog,int id) {
            deleteStudent(nim);
           }
          })
          .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
           }
          });

  AlertDialog alertDialog = alertDialogBuilder.create();

  alertDialog.show();
 }

 private boolean validateForm() {
  boolean result = true;
  if (inputNim.getText().toString().equals("")) {
   inputNim.setError("Required");
   result = false;
  }
  if (inputNama.getText().toString().equals("")) {
   inputNama.setError("Required");
   result = false;
  }
  if (inputKelas.getText().toString().equals("")) {
   inputKelas.setError("Required");
   result = false;
  }

  return result;
 }
}
