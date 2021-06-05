package com.example.firebaseauth;

public class DataRV {
 private String nama;
 private String nim;
 private String kelas;

 public DataRV() {

 }

 public DataRV(String nim, String nama, String kelas){
  this.nama = nama;
  this.nim = nim;
  this.kelas = kelas;
 }

 public void setNama(String nama) {
  this.nama = nama;
 }

 public String getNama() {
  return nama;
 }

 public void setNim(String nim) {
  this.nim = nim;
 }

 public String getNim() {
  return nim;
 }

 public void setKelas(String kelas) {
  this.kelas = kelas;
 }

 public String getKelas() {
  return kelas;
 }
}
