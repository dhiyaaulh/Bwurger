package com.example.bwurger;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class fragmentDetailBurger extends Fragment implements View.OnClickListener {
    private TextView nama_burger, resep;
    private Button kembali, delete, edit;

    private static final String DBURL = "https://bwurger-p6-default-rtdb.asia-southeast1.firebasedatabase.app";
    private Bundle args;
    private Burger detail;
    private FirebaseDatabase db;
    private DatabaseReference ref;

    public fragmentDetailBurger() {}


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        this.db = FirebaseDatabase.getInstance(DBURL);
        this.ref = db.getReference("burgers");

        this.nama_burger = v.findViewById(R.id.nama_burger);
        this.resep = v.findViewById(R.id.tvResep);
        this.kembali = v.findViewById(R.id.button_back);
        this.delete = v.findViewById(R.id.button_hapus_burger);
        this.edit = v.findViewById(R.id.button_edit_burger);



        args = getArguments();
        if(args != null){
            detail = (Burger) args.getSerializable("burgers");

            nama_burger.setText(detail.getName());
            resep.setText(detail.getIngredients().toString());
        }

        this.kembali.setOnClickListener(this);
        this.edit.setOnClickListener(this);
        this.delete.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_back) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        if (view.getId() == R.id.button_hapus_burger) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Burger")
                    .setMessage("Konfirmasi penghapusan Burger?")
                    .setPositiveButton("Ya", (dialogInterface, i) -> {
                        // Hapus burger jika pengguna menekan tombol "Ya"
                        new Thread(() -> {
                            // Ambil argumen burger dari bundle
                            Bundle args = getArguments();
                            if (args != null) {
                                String id = args.getString("ID");

                                if (detail != null && id != null) {
                                    // Hapus burger jika ditemukan
                                    ref.child(id).removeValue();

                                    // Update UI di thread utama
                                    requireActivity().runOnUiThread(() -> {
                                        Toast.makeText(requireContext(), "Burger deleted successfully!", Toast.LENGTH_SHORT).show();
                                        // Kembali ke fragment sebelumnya
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    });
                                }
                            }
                        }).start();
                    })
                    .setNegativeButton("Tidak", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .create()
                    .show();
        }


        if (view.getId() == R.id.button_edit_burger) {
            Bundle bundle = new Bundle();
            fragmentBahanTerpilih edit = new fragmentBahanTerpilih();
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            new Thread(() -> {
                ArrayList<String> pilihan = detail.getIngredients();
                bundle.putStringArrayList("ingredients", pilihan);
                bundle.putString("ID", detail.getId());
                bundle.putString("name", detail.getName());
                edit.setArguments(bundle);
                fm.beginTransaction().replace(R.id.fragment_container, edit,"fbt" ).
                        addToBackStack("fbt").commit();
                resep.setText(detail.getIngredients().toString());
            }).start();
        }
    }
}