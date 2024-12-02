package com.example.listadecomprasryan;
public class Item {
    public String nome;
    public String quantidade;

    public Item(String nome, String quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    // Getters e setters, se necessário

    public String getNome() {
        return nome;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

}
