package com.example.listadecomprasryan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.CarrinhoViewHolder> {

    private List<Item> itemList;
    private OnItemClickListener onItemClickListener;

    public ListaAdapter(List<Item> itemList, OnItemClickListener onItemClickListener) {
        this.itemList = itemList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public CarrinhoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar o layout do item da lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_compras, parent, false);
        return new CarrinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarrinhoViewHolder holder, int position) {
        // Vincula os dados do item à visualização
        Item item = itemList.get(position);
        holder.nomeTextView.setText(item.getNome());
        holder.quantidadeTextView.setText(item.getQuantidade());

        // Definindo as ações de clique para editar e excluir
        holder.btnEditar.setOnClickListener(v -> onItemClickListener.onEditClick(item, position));
        holder.btnExcluir.setOnClickListener(v -> onItemClickListener.onDeleteClick(item, position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder que mantém as referências dos itens do layout
    public static class CarrinhoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView, quantidadeTextView;
        Button btnEditar, btnExcluir;

        public CarrinhoViewHolder(View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.textViewNome);
            quantidadeTextView = itemView.findViewById(R.id.textViewQuantidade);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnExcluir = itemView.findViewById(R.id.btnExcluir);
        }
    }

    // Interface para as ações de clique (editar, excluir)
    public interface OnItemClickListener {
        void onEditClick(Item item, int position);
        void onDeleteClick(Item item, int position);
    }
}
