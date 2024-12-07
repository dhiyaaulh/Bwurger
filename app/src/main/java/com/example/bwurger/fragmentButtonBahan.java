package com.example.bwurger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class fragmentButtonBahan extends Fragment {
    private rvPilihanBahanAdapter adapter;
    private Button btRoti, btDaging, btPelengkap, btSaus;
    private BottomSheetDialog bottomSheetDialog;
    private ArrayList<pilihanBahanModel> pilihanBahanModelArrayList = new ArrayList<>();
    private Button lastSelectedButton; // Menyimpan tombol terakhir yang dipilih
    private ArrayList<pilihanBahanModel> selectedBahanList = new ArrayList<>();
    private fragmentBahanTerpilih fragmentBahanTerpilih;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button_bahan, container, false);
        // Inisialisasi adapter
        adapter = new rvPilihanBahanAdapter(getContext(), pilihanBahanModelArrayList, bahan -> addBahanToSelected(bahan));

        if(getArguments() != null){
            selectedBahanList = getArguments().getParcelableArrayList("selectedBahanList");
        }

        Log.d("FragmentButtonBahan", "loadFragmentBahanTerpilih() called" + selectedBahanList.toString());

        btRoti = view.findViewById(R.id.btRoti);
        btDaging = view.findViewById(R.id.btDaging);
        btPelengkap = view.findViewById(R.id.btPelengkap);
        btSaus = view.findViewById(R.id.btSaus);

        btRoti.setOnClickListener(v -> handleButtonClick(btRoti, "Roti"));
        btDaging.setOnClickListener(v -> handleButtonClick(btDaging, "Daging"));
        btPelengkap.setOnClickListener(v -> handleButtonClick(btPelengkap, "Pelengkap"));
        btSaus.setOnClickListener(v -> handleButtonClick(btSaus, "Saus"));


        return view;
    }

    private void handleButtonClick(Button selectedButton, String kategori) {
        // Ubah warna tombol terakhir ke default jika ada
        if (lastSelectedButton != null) {
            lastSelectedButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.botBarGreen));
        }

        // Ubah warna tombol yang dipilih ke btYellow
        selectedButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.btYellow));
        lastSelectedButton = selectedButton; // Simpan tombol yang baru saja dipilih

        // Load kategori bahan
        loadKategori(kategori);

        // Tampilkan fragment_bahan_terpilih dan BottomSheet
        loadFragmentBahanTerpilih();
        showBottomSheet();
    }

    private void loadFragmentBahanTerpilih() {
        if(getArguments() == null){
            fragmentBahanTerpilih = new fragmentBahanTerpilih();
        } else if(getArguments() != null){
            fragmentBahanTerpilih = (fragmentBahanTerpilih) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        }

        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragmentBahanTerpilih)
                .addToBackStack(null).commit();

    }

    private void loadKategori(String kategori) {
        pilihanBahanModelArrayList.clear();

        switch (kategori) {
            case "Roti":
                pilihanBahanModelArrayList.add(new pilihanBahanModel("Roti Brioche", "bun_brioche"));
                pilihanBahanModelArrayList.add(new pilihanBahanModel("Roti Normal", "bun_normal"));
                pilihanBahanModelArrayList.add(new pilihanBahanModel("Roti Pretzel", "bun_pretzel"));
                pilihanBahanModelArrayList.add(new pilihanBahanModel("Roti Gandum", "bun_wheat"));
                break;
            case "Daging":
                pilihanBahanModelArrayList.add(new pilihanBahanModel("Patty Ayam", "patty_chicken"));
                pilihanBahanModelArrayList.add(new pilihanBahanModel("Patty Vegan", "patty_vegan"));
                pilihanBahanModelArrayList.add(new pilihanBahanModel("Patty Normal", "patty_normal"));
                break;
            case "Pelengkap":
                pilihanBahanModelArrayList.add(new pilihanBahanModel("Lettuce", "pelengkap_lettuce"));
                break;
            case "Saus":
                pilihanBahanModelArrayList.add(new pilihanBahanModel("Saus Mustard", "sauce_mustard"));
                pilihanBahanModelArrayList.add(new pilihanBahanModel("Saus Ketchup", "sauce_ketchup"));
                break;
        }

        adapter.notifyDataSetChanged();
    }

    private void showBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet_pilihan_bahan, null);
        bottomSheetDialog.setContentView(sheetView);

        RecyclerView recyclerView = sheetView.findViewById(R.id.rvPilihanBahan);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        bottomSheetDialog.show();
    }

    // Method untuk menambah bahan yang dipilih
    public void addBahanToSelected(pilihanBahanModel bahan) {
        // Periksa apakah bahan sudah ada di daftar bahan yang dipilih
        Log.d("FragmentButtonBahan", "Bahan yang diterima: " + bahan.getClass().getName());

        boolean exists = false;
        for (pilihanBahanModel selected : selectedBahanList) {
            if (selected.getNama().equals(bahan.getNama())){
                exists = true;
                break;
            }
        }

        if (!exists) {
            selectedBahanList.add(bahan);
        }

        // Jika fragmentBahanTerpilih tidak null, update RecyclerView
        if (fragmentBahanTerpilih != null) {
            fragmentBahanTerpilih.updateSelectedBahanList(selectedBahanList);
        }
    }

}
