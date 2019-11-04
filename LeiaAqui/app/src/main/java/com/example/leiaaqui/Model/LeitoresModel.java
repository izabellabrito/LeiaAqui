package com.example.leiaaqui.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "leitores")
public class LeitoresModel {

    @PrimaryKey
    @NonNull
    private Long id;
    private String codCategoria;
    private String descCategoria;
    private int numDiasMax;

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

    public String getDescCategoria() {
        return descCategoria;
    }

    public void setDescCategoria(String descCategoria) {
        this.descCategoria = descCategoria;
    }

    public int getNumDiasMax() {
        return numDiasMax;
    }

    public void setNumDiasMax(int numDiasMax) {
        this.numDiasMax = numDiasMax;
    }
}
