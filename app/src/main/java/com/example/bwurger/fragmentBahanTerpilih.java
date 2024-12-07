package com.example.bwurger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class fragmentBahanTerpilih extends Fragment {

    private static final String DBURL = "https://bwurger-p6-default-rtdb.asia-southeast1.firebasedatabase.app"; // URL database
    private Button btSelesai;
    private ArrayList<pilihanBahanModel> selectedBahanList = new ArrayList<>();
    private ArrayList<String> ingredients = new ArrayList<>();
    private EditText etNamaBurger;
    private RecyclerView recyclerView;
    private rvBahanTerpilihAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bahan_terpilih, container, false);

        btSelesai = view.findViewById(R.id.btSelesai);
        etNamaBurger = view.findViewById(R.id.etNamaBurger);
        recyclerView = view.findViewById(R.id.rvBahanTerpilih);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(getArguments() != null){
            selectedBahanList.clear();
            ingredients = getArguments().getStringArrayList("ingredients");
            fragmentButtonBahan fragmentButtonBahan = new fragmentButtonBahan();
            for (String bahan : ingredients) {
                pilihanBahanModel bahanModel = new pilihanBahanModel(bahan, bahan);
                selectedBahanList.add(bahanModel);
                fragmentButtonBahan.addBahanToSelected(bahanModel);
            }

            etNamaBurger.setText(getArguments().getString("name"));

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("selectedBahanList", selectedBahanList);
            fragmentButtonBahan.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_bahan, fragmentButtonBahan)
                    .commit();
        }

        adapter = new rvBahanTerpilihAdapter(getContext(), selectedBahanList);
        recyclerView.setAdapter(adapter);

        btSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBurgerToDatabase();
                adapter.notifyDataSetChanged();
                requireActivity().getSupportFragmentManager().
                        popBackStack("fbt", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }); // Menyimpan burger saat tombol diklik



        return view;
    }

    public void updateSelectedBahanList(ArrayList<pilihanBahanModel> selectedBahanList) {
        if(getArguments() == null){
            this.selectedBahanList.clear();
            this.selectedBahanList.addAll(selectedBahanList);
        }
        else {
            this.selectedBahanList = selectedBahanList;
        }
        adapter.notifyDataSetChanged();
    }

    public void saveBurgerToDatabase() {
        fragmentButtonBahan fragmentButton = (fragmentButtonBahan) requireActivity().
                getSupportFragmentManager().findFragmentById(R.id.fragment_container_bahan);
        getArguments();
        // Tentukan ID gambar burger dari drawable
        int imageResId = R.drawable.ic_burger;
        if(getArguments() != null){
           String id = getArguments().getString("ID");
           String name = etNamaBurger.getText().toString();
           DatabaseReference ref = FirebaseDatabase.getInstance(DBURL).getReference("burgers");

           ingredients.clear();
            for (pilihanBahanModel bahan : selectedBahanList) {
                ingredients.add(bahan.getGambar());
            }

           Burger burger = new Burger(id, name, ingredients, imageResId);
           Log.d("FragmentBahanTerpilih", "saveBurgerToDatabase: " +id);
           ref.child(id).setValue(burger);

           Toast.makeText(getContext(), "Burger berhasil diperbarui", Toast.LENGTH_SHORT).show();
           selectedBahanList.clear();
           adapter.notifyDataSetChanged();
       } else{
           if (!selectedBahanList.isEmpty()) {
               DatabaseReference databaseReference = FirebaseDatabase.getInstance(DBURL).getReference("burgers");

               for (pilihanBahanModel bahan : selectedBahanList) {
                   ingredients.add(bahan.getGambar());
               }

               String id = databaseReference.push().getKey();
               String burgerName = etNamaBurger.getText().toString();
               // Simpan ke database
               Burger burger = new Burger(id, burgerName, ingredients, imageResId);
               databaseReference.child(id).setValue(burger)
                       .addOnSuccessListener(aVoid -> {
                           Toast.makeText(getContext(), "Burger berhasil disimpan", Toast.LENGTH_SHORT).show();// Perbarui RecyclerView
                           selectedBahanList.clear();
                           adapter.notifyDataSetChanged();
                       });
           }
       }
    }
}