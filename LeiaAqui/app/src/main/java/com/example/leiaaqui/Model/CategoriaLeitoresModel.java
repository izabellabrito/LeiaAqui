package com.example.leiaaqui.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categoriaLeitores")
public class CategoriaLeitoresModel {

    @PrimaryKey
    @NonNull
    private Long id;
    private String codigoCategoria;
    private String descricao;
    private String diasEmprestimo;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    public void setCodigoCategoria(String codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDiasEmprestimo() {
        return diasEmprestimo;
    }

    public void setDiasEmprestimo(String diasEmprestimo) {
        this.diasEmprestimo = diasEmprestimo;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
