package com.example.leiaaqui;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.leiaaqui.DAO.CategoriaLeitoresDAO;
import com.example.leiaaqui.DAO.CategoriaLivrosDAO;
import com.example.leiaaqui.DAO.ClienteDAO;
import com.example.leiaaqui.DAO.EmprestimoDAO;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.Model.CategoriaLeitoresModel;
import com.example.leiaaqui.Model.CategoriaLivrosModel;
import com.example.leiaaqui.Model.ClienteModel;
import com.example.leiaaqui.Model.EmprestimoModel;
import com.example.leiaaqui.Model.LeitoresModel;
import com.example.leiaaqui.Model.LivroModel;

@Database(entities = {ClienteModel.class, LivroModel.class, CategoriaLeitoresModel.class,
        CategoriaLivrosModel.class, LeitoresModel.class, EmprestimoModel.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract ClienteDAO getClienteDAO();

    public abstract LivrosDAO getLivrosDAO();

    public abstract CategoriaLeitoresDAO getCategoriaLeitoresDAO();

    public abstract CategoriaLivrosDAO getCategoriaLivrosDAO();

    public abstract EmprestimoDAO getEmprestimosDAO();

}
