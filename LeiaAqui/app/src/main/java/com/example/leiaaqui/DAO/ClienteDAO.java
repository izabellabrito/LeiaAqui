package com.example.leiaaqui.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.leiaaqui.Model.ClienteModel;

import java.util.List;

@Dao
public interface ClienteDAO {

    @Insert
    void insert(ClienteModel... cliente);

    @Update
    void update(ClienteModel... cliente);

    @Delete
    void delete(ClienteModel cliente);

    @Query("SELECT * FROM clientes")
    List<ClienteModel> getClientes();

    @Query("SELECT * FROM clientes WHERE codCategoria = :codigo")
    List<ClienteModel> getClientesByCategoria(String codigo);

    @Query("SELECT * FROM clientes WHERE id = :codigo")
    ClienteModel getClienteById(Long codigo);

    @Query("SELECT * FROM clientes WHERE cpf = :cpf")
    ClienteModel getClienteByCPF(String cpf);
}
