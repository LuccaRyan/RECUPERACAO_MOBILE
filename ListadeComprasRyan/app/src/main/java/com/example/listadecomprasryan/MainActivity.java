package com.example.listadecomprasryan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa o Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Referência para o nó "items"
        DatabaseReference myRef = database.getReference("items");

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Carregar o fragmento padrão (Adicionar Item) e o fragmento do tempo
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FragmentAdicionarItem()) // Container principal
                    .replace(R.id.fragment_container_top, new FragmentTempo()) // Container superior (tempo)
                    .commit();
        }

        // Listener de seleção do BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Simples troca de fragmentos dependendo da seleção no BottomNavigationView
            if (item.getItemId() == R.id.menu_explorar) {
                selectedFragment = new FragmentAdicionarItem(); // Substitui com FragmentAdicionarItem
            } else if (item.getItemId() == R.id.menu_carrinho) {
                selectedFragment = new FragmentLista(); // Substitui com FragmentCarrinho
            }

            // Se o fragmento foi encontrado, substituímos o fragmento no container principal
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment) // Substitui no container principal
                        .commit();
            }

            return true;
        });
    }
}
