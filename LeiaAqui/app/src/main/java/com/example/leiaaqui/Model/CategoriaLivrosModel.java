package com.example.leiaaqui.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categoriaLivros")
public class CategoriaLivrosModel {

    @PrimaryKey
    @NonNull
    private Long id;
    private String codCategoria;
    private String descricaoCategoria;
    private String multaAtraso;
    private String numDiasEmprestimo;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getDescricaoCategoria() {
        return descricaoCategoria;
    }

    public void setDescricaoCategoria(String descricaoCategoria) {
        this.descricaoCategoria = descricaoCategoria;
    }

    public String getMultaAtraso() {
        return multaAtraso;
    }

    public void setMultaAtraso(String multaAtraso) {
        this.multaAtraso = multaAtraso;
    }

    public String getNumDiasEmprestimo() {
        return numDiasEmprestimo;
    }

    public void setNumDiasEmprestimo(String numDiasEmprestimo) {
        this.numDiasEmprestimo = numDiasEmprestimo;
    }

    @Override
    public String toString() {
        return this.descricaoCategoria;
    }
}
