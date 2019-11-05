package com.example.leiaaqui.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.leiaaqui.Model.EmprestimoModel;

import java.util.List;

@Dao
public interface EmprestimoDAO {
    @Insert
    void insert(EmprestimoModel... emprestimo);

    @Update
    void update(EmprestimoModel... emprestimo);

    @Delete
    void delete(EmprestimoModel emprestimo);

    @Query("SELECT * FROM emprestimo")
    List<EmprestimoModel> getEmprestimos();

    @Query("SELECT * FROM emprestimo WHERE id = :id")
    EmprestimoModel getEmprestimoById(Long id);

    @Query("SELECT * FROM emprestimo WHERE clienteId = :clienteId")
    EmprestimoModel getEmprestimoByCliente(Long clienteId);

    // 0 é false
    @Query("SELECT * FROM emprestimo " +
            "INNER JOIN livros ON emprestimo.codigoLivro = livros.codigo " +
            "WHERE livros.codCategoria = :codigo")
    List<EmprestimoModel> getEmprestimoByCategoria(String codigo);

    @Query("DELETE FROM emprestimo WHERE id = :id")
    int deleteById(Long id);

}
