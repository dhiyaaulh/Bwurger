package com.example.bwurger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragmentBurgerTersimpan extends Fragment implements rvBurgerTersimpanAdapter.OnItemClickListener {

    private RecyclerView rvBurgerTersimpan;
    private rvBurgerTersimpanAdapter adapter;
    private List<Burger> burgerList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_burger_tersimpan, container, false);


        rvBurgerTersimpan = view.findViewById(R.id.rvBurgerTersimpan);
        rvBurgerTersimpan.setLayoutManager(new GridLayoutManager(this.getActivity(),2));

        // Inisialisasi adapter dengan daftar burger kosong
        adapter = new rvBurgerTersimpanAdapter(getContext(), burgerList, this);
        rvBurgerTersimpan.setAdapter(adapter); // Set adapter ke RecyclerView

        loadSavedBurgers(); // Muat data burger dari Firebase

        return view;
    }

    private void loadSavedBurgers() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("burgers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                burgerList.clear(); // Bersihkan daftar sebelum menambahkan yang baru
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Burger burger = snapshot.getValue(Burger.class);
                    if (burger != null) {
                        burgerList.add(burger); // Tambahkan burger ke daftar
                    }
                }
                adapter.notifyDataSetChanged(); // Beritahu adapter bahwa data telah berubah
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public void onItemClick(int position) {
        Burger selectedBurger = burgerList.get(position);
        Bundle bundle = new Bundle();
        fragmentDetailBurger detail = new fragmentDetailBurger();


        Thread t = new Thread(() -> {
            if (selectedBurger != null) {
                bundle.putString("ID", selectedBurger.getId());
                bundle.putSerializable("burgers", selectedBurger);
                detail.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, detail)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(requireContext(), "Burger not found!", Toast.LENGTH_SHORT).show();
            }
        });
        t.start();
    }
}