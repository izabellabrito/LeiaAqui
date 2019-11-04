package com.example.leiaaqui.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.leiaaqui.Model.CategoriaLivrosModel;

import java.util.List;

@Dao
public interface CategoriaLivrosDAO {
    @Insert
    void insert(CategoriaLivrosModel... categoria);

    @Update
    void update(CategoriaLivrosModel... categoria);

    @Delete
    void delete(CategoriaLivrosModel categoria);

    @Query("SELECT * FROM categoriaLivros")
    List<CategoriaLivrosModel> getCategoriaLivros();

    @Query("SELECT * FROM categoriaLivros WHERE codCategoria = :codigo")
    CategoriaLivrosModel getCategoriaByCodigo(String codigo);
}
