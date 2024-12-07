package com.example.bwurger;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class pilihanBahanModel implements Parcelable {
    public String nama;
    public String gambar;

    public pilihanBahanModel(String nama, String gambar) {
        this.nama = nama;
        this.gambar = gambar;
    }

    public String getGambar() {
        return gambar;
    }

    public String getNama() {
        return nama;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

    }
}
