package com.example.listadecomprasryan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentAdicionarItem extends Fragment {

    private EditText editTextItem;
    private EditText editTextQuantidade;
    private Button btnAdicionarItem;
    private FirebaseFirestore db;
    private CollectionReference itemsRef;

    public FragmentAdicionarItem() {
        // Requer um construtor vazio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar o layout para o fragmento
        View view = inflater.inflate(R.layout.fragment_adicionar_item, container, false);

        // Encontrando os elementos no layout
        editTextItem = view.findViewById(R.id.editTextItem);
        editTextQuantidade = view.findViewById(R.id.editTextQuantidade);
        btnAdicionarItem = view.findViewById(R.id.btnAdicionarItem);

        // Inicializando o Firestore
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("itens");

        // Definindo o clique do botão para adicionar o item
        btnAdicionarItem.setOnClickListener(v -> {
            // Pegando os valores dos campos de texto
            String nomeItem = editTextItem.getText().toString().trim();
            String quantidade = editTextQuantidade.getText().toString().trim();

            // Verificando se os campos estão preenchidos
            if (nomeItem.isEmpty() || quantidade.isEmpty()) {
                Toast.makeText(getActivity(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            } else {
                // Verificando se o item já existe na lista
                checkItemExists(nomeItem, quantidade);
            }
        });

        return view;
    }

    private void checkItemExists(String nomeItem, String quantidade) {
        // Realiza uma consulta para verificar se o item já está na coleção
        Query query = itemsRef.whereEqualTo("nome", nomeItem);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    // Item já existe na lista
                    Toast.makeText(getActivity(), "Este item já foi adicionado.", Toast.LENGTH_SHORT).show();
                } else {
                    // Item não existe, podemos adicionar
                    addItemToFirestore(nomeItem, quantidade);
                }
            } else {
                Toast.makeText(getActivity(), "Erro ao verificar a existência do item.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addItemToFirestore(String nomeItem, String quantidade) {
        // Criando um objeto Item com os dados inseridos
        Item item = new Item(nomeItem, quantidade);

        // Adicionando o item no Firestore
        itemsRef.add(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Limpar os campos após adicionar
                editTextItem.setText("");
                editTextQuantidade.setText("");
                Toast.makeText(getActivity(), "Item adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Erro ao adicionar item.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Classe que representa o item (com os dados)
    public static class Item {
        public String nome;
        public String quantidade;

        public Item(String nome, String quantidade) {
            this.nome = nome;
            this.quantidade = quantidade;
        }
    }
}
