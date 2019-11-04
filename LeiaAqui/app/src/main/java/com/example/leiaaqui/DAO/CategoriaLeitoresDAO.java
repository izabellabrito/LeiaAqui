package com.example.leiaaqui.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.leiaaqui.Model.CategoriaLeitoresModel;

import java.util.List;

@Dao
public interface CategoriaLeitoresDAO {
    @Insert
    void insert(CategoriaLeitoresModel... categoria);

    @Update
    void update(CategoriaLeitoresModel... categoria);

    @Delete
    void delete(CategoriaLeitoresModel categoria);

    @Query("SELECT * FROM categoriaLeitores")
    List<CategoriaLeitoresModel> getCategoriasLeitores();

    @Query("SELECT * FROM categoriaLeitores WHERE codigoCategoria = :codigo")
    CategoriaLeitoresModel getCategoriaById(String codigo);
}
