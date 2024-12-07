package com.example.bwurger;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btBuatanSaya, btTersimpan;
    private ImageButton btBack;
 // Daftar bahan yang dipilih
    private fragmentBahanTerpilih fragmentBahanTerpilih;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btBuatanSaya = findViewById(R.id.btBuatanSaya);
        btTersimpan = findViewById(R.id.btTersimpan);

        // Load fragment awal
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new fragmentMain());
        fragmentTransaction.add(R.id.fragment_container_bahan, new fragmentButtonBahan());
        fragmentTransaction.commit();

        // Klik button untuk memuat data kategori tertentu
        btBuatanSaya.setOnClickListener(this);

        btTersimpan.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        btTersimpan.setSelected(false);
        btBuatanSaya.setSelected(false);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragmentBahanTerpilih fbt = new fragmentBahanTerpilih();
        if (view.getId() == R.id.btBuatanSaya) {
            view.setSelected(true);
            if (fbt == null) {
                fbt = new fragmentBahanTerpilih();
            }

            ft.replace(R.id.fragment_container, fbt).commit();
        }
        if (view.getId() == R.id.btTersimpan) {
            view.setSelected(true);
                // Ubah warna latar belakang tombol yang dikli
                // Ganti fragment
                ft.replace(R.id.fragment_container, new fragmentBurgerTersimpan())
                        .addToBackStack(null).commit();
        }
    }
}
