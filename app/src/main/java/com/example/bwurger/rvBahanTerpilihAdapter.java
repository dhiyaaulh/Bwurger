package com.example.bwurger;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class rvBahanTerpilihAdapter extends RecyclerView.Adapter<rvBahanTerpilihAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<pilihanBahanModel> selectedBahanList;
    private ImageButton btRemove, btAdd;

    public rvBahanTerpilihAdapter(Context context, ArrayList<pilihanBahanModel> selectedBahanList) {
        this.context = context;
        this.selectedBahanList = selectedBahanList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pilihan_pengguna_rv, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // Mendapatkan nama gambar dari objek bahan yang dipilih
        String imageName = selectedBahanList.get(position).getGambar();

        // Mendapatkan ID resource gambar dari nama gambar
        int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        // Memuat gambar ke dalam ImageView menggunakan Glide (atau Picasso, jika Anda lebih suka)
        Glide.with(context)
                .load(imageResId)
                .into(holder.ivBahanImage);
    }


    @Override
    public int getItemCount() {
        return selectedBahanList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBahanImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBahanImage = itemView.findViewById(R.id.imageBahan);
        }
    }

    public void clearItems() {
        selectedBahanList.clear();
        notifyDataSetChanged();
    }
}
