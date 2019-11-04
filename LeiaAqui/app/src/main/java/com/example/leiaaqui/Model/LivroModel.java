package com.example.leiaaqui.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "livros")
public class LivroModel {

    @PrimaryKey
    @NonNull
    private Long id;
    public int copias;
    private String codigo;
    private String ISBN;
    private String titulo;
    private String codCategoria;
    private String autores;
    private String paravrasChave;
    private String dataPublicacao;
    private String numEdicao;
    private String editora;
    private int numPaginas;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public int getCopias() {
        return copias;
    }

    public void setCopias(int copias) {
        this.copias = copias;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public String getParavrasChave() {
        return paravrasChave;
    }

    public void setParavrasChave(String paravrasChave) {
        this.paravrasChave = paravrasChave;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getNumEdicao() {
        return numEdicao;
    }

    public void setNumEdicao(String numEdicao) {
        this.numEdicao = numEdicao;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getNumPaginas() { return numPaginas; }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }
}
