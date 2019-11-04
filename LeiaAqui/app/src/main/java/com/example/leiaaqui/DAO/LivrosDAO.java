package com.example.leiaaqui.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.leiaaqui.Model.LivroModel;
import java.util.List;

@Dao
public interface LivrosDAO {
    @Insert
    void insert(LivroModel... livro);

    @Update
    void update(LivroModel... livro);

    @Delete
    void delete(LivroModel livro);

    @Query("SELECT * FROM livros")
    List<LivroModel> getLivros();

    @Query("SELECT * FROM livros WHERE codigo = :codigo")
    LivroModel getLivroByCodigo(String codigo);
}
