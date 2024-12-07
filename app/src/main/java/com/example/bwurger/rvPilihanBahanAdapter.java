package com.example.bwurger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class rvPilihanBahanAdapter extends RecyclerView.Adapter<rvPilihanBahanAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<pilihanBahanModel> pilihanBahanModelArrayList;
    private OnBahanClickListener listener;

    public rvPilihanBahanAdapter(Context context, ArrayList<pilihanBahanModel> pilihanBahanModelArrayList, OnBahanClickListener listener) {
        this.context = context;
        this.pilihanBahanModelArrayList = pilihanBahanModelArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        pilihanBahanModel model = pilihanBahanModelArrayList.get(position);
        holder.tvBahanPilihan.setText(model.getNama());

        // Memuat gambar dari drawable
        int imageResId = context.getResources().getIdentifier(model.getGambar(), "drawable", context.getPackageName());
        Glide.with(context).load(imageResId).into(holder.btBahanPilihan);

        holder.btBahanPilihan.setOnClickListener(view -> {
            if (listener != null) {
                listener.onBahanClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pilihanBahanModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageButton btBahanPilihan;
        TextView tvBahanPilihan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btBahanPilihan = itemView.findViewById(R.id.btBahanPilihan);
            tvBahanPilihan = itemView.findViewById(R.id.tvBahanPilihan);
        }
    }

    public interface OnBahanClickListener {
        void onBahanClick(pilihanBahanModel bahan);
    }
}
