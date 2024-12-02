package com.example.listadecomprasryan;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentLista extends Fragment implements ListaAdapter.OnItemClickListener {

    private RecyclerView recyclerViewCarrinho;
    private ListaAdapter listaAdapter;
    private List<Item> itemList = new ArrayList<>();
    private FirebaseFirestore db;

    public FragmentLista() {
        // Requer um construtor vazio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflando o layout do fragment
        View view = inflater.inflate(R.layout.fragment_carrinho, container, false);

        // Inicializando o Firestore
        db = FirebaseFirestore.getInstance();

        // Configurando o RecyclerView
        recyclerViewCarrinho = view.findViewById(R.id.recyclerViewCarrinho);
        recyclerViewCarrinho.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Carregar os itens do Firestore
        carregarItensDoCarrinho();

        return view;
    }

    private void carregarItensDoCarrinho() {
        itemList.clear();  // Limpar a lista antes de carregar novos itens

        db.collection("itens")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot document : querySnapshot) {
                                String nome = document.getString("nome");
                                String quantidade = document.getString("quantidade");

                                // Adicionando o item na lista
                                itemList.add(new Item(nome, quantidade));
                            }

                            // Criar e configurar o adapter
                            listaAdapter = new ListaAdapter(itemList, this);
                            recyclerViewCarrinho.setAdapter(listaAdapter);
                        }
                    }
                });
    }

    // Método de edição de item
    @Override
    public void onEditClick(Item item, int position) {
        // Abrir um Dialog para editar a quantidade
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Editar Item");

        // Criar um EditText para editar a quantidade
        final EditText input = new EditText(getActivity());
        input.setText(item.getQuantidade());
        builder.setView(input);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String novaQuantidade = input.getText().toString();

            // Verificar se a quantidade é um número válido
            try {
                int quantidadeInt = Integer.parseInt(novaQuantidade);

                if (quantidadeInt < 0) {
                    // Se a quantidade for negativa, definir como 0
                    novaQuantidade = "0";
                }

                // Se a quantidade for 0, excluir o item
                if (novaQuantidade.equals("0")) {
                    excluirItem(item, position);
                } else {
                    // Atualizar o item no Firestore
                    atualizarItemNoFirestore(item, novaQuantidade, position);
                }

            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Por favor, insira uma quantidade válida.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onDeleteClick(Item item, int position) {
        excluirItem(item, position);
    }

    private void excluirItem(Item item, int position) {
        db.collection("itens")
                .whereEqualTo("nome", item.getNome())  // Usando o nome como referência
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            db.collection("itens")
                                    .document(documentSnapshot.getId())
                                    .delete()
                                    .addOnCompleteListener(deleteTask -> {
                                        if (deleteTask.isSuccessful()) {
                                            // Remover da lista local e notificar o adapter
                                            itemList.remove(position);
                                            listaAdapter.notifyItemRemoved(position);
                                            Toast.makeText(getActivity(), "Item excluído.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Erro ao excluir item.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }

    private void atualizarItemNoFirestore(Item item, String novaQuantidade, int position) {
        db.collection("itens")
                .whereEqualTo("nome", item.getNome())  // Usando o nome como chave
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            // Atualizar a quantidade no Firestore
                            db.collection("itens")
                                    .document(documentSnapshot.getId())
                                    .update("quantidade", novaQuantidade)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            // Atualizar a lista local
                                            item.setQuantidade(novaQuantidade);
                                            listaAdapter.notifyItemChanged(position);
                                            Toast.makeText(getActivity(), "Item atualizado!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Erro ao atualizar item.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }


}
