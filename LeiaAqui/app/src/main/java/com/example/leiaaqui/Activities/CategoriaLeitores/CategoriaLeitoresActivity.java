package com.example.leiaaqui.Activities.CategoriaLeitores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leiaaqui.Adapters.CategoriaLeitoresAdapter;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.CategoriaLeitoresDAO;
import com.example.leiaaqui.MainActivity;
import com.example.leiaaqui.Model.CategoriaLeitoresModel;
import com.example.leiaaqui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoriaLeitoresActivity extends AppCompatActivity {

    TextView nenhumaCategoria;
    RecyclerView recycler;
    CategoriaLeitoresAdapter adapter;
    List<CategoriaLeitoresModel> categorias = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton btn_adicionar;
    CategoriaLeitoresDAO categoriaLeitoresDAO;
    AppDataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_leitores);

        /* Inicializar variavéis */
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        categoriaLeitoresDAO = database.getCategoriaLeitoresDAO();

        /* Obtem as categorias de leitores */
        obterCategorias();

        /* Envento ao clicar no botão adicionar */
        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CadastroCategoriaLeitores.class);
            }
        });
    }

    /* Método para inicializar variaveis */
    private void init() {
        nenhumaCategoria = (TextView) findViewById(R.id.tv_nenhuma_categoria_leitor);
        btn_adicionar = (FloatingActionButton) findViewById(R.id.btn_adicionar_categoria_leitor);
        recycler = (RecyclerView) findViewById(R.id.rv_categoriaLeitores);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
    }

    /* Método para obter as categorias de leitores */
    private void obterCategorias() {
        try {
            categorias = categoriaLeitoresDAO.getCategoriasLeitores();
            if(categorias != null && categorias.size() > 0) {
                adapter = new CategoriaLeitoresAdapter(categorias, this);
                recycler.setAdapter(adapter);
                nenhumaCategoria.setVisibility(View.GONE);
            } else {
                nenhumaCategoria.setVisibility(View.VISIBLE);
            }
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Metódo para iniciar uma activity */
    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    /* Metódo para quando apertar no botão voltar no appbar */
    @Override
    public boolean onSupportNavigateUp(){
        startActivity(MainActivity.class);
        return true;
    }
}

