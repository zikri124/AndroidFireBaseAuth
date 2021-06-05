package com.example.firebaseauth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<com.example.firebaseauth.RVAdapter.MyViewHolder> {
 private List<DataRV> RVList;
 private RecyclerViewClickListener listener;

 public RVAdapter(ArrayList<DataRV> studentList, RecyclerViewClickListener listener) {
  this.RVList = studentList;
  this.listener = listener;
 }

 class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  TextView tvId;
  TextView tvNama;
  TextView tvNIM;
  TextView tvKelas;
  LinearLayout card;

  MyViewHolder(View view) {
   super(view);
   tvNIM = view.findViewById(R.id.textNIM);
   tvNama = view.findViewById(R.id.textName);
   tvKelas = view.findViewById(R.id.textClass);
   card = view.findViewById(R.id.card);

   card.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
   int id = 0;
   if (view.getId() == card.getId()) {
    id = 1;
   }
   listener.onClick(view, getAdapterPosition());
  }
 }

 @NonNull
 @Override
 public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
  return new MyViewHolder(view);
 }

 @Override
 public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
  final DataRV result = RVList.get(position);;
  holder.tvNama.setText(result.getNama());
  holder.tvNIM.setText(result.getNim());
  holder.tvKelas.setText(result.getKelas());
 }

 @Override
 public int getItemCount() {
  return RVList.size();
 }

 public interface RecyclerViewClickListener {
  void onClick(View view, int position);
 }
}

