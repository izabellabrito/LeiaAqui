package com.example.leiaaqui.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "emprestimo")
public class EmprestimoModel {

    @PrimaryKey
    @NonNull
    private Long id;
    private Long clienteId;
    private String codigoLivro;
    private String dataRetirada;
    private String previsaoDevolucao;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getCodigoLivro() {
        return codigoLivro;
    }

    public void setCodigoLivro(String codigoLivro) {
        this.codigoLivro = codigoLivro;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(String dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public String getPrevisaoDevolucao() {
        return previsaoDevolucao;
    }

    public void setPrevisaoDevolucao(String previsaoDevolucao) {
        this.previsaoDevolucao = previsaoDevolucao;
    }
}
