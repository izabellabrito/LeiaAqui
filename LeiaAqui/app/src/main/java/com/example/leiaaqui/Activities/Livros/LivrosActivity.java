package com.example.leiaaqui.Activities.Livros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leiaaqui.Adapters.AcervoAdapter;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.MainActivity;
import com.example.leiaaqui.Model.LivroModel;
import com.example.leiaaqui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LivrosActivity extends AppCompatActivity {

    TextView nenhumLivro;
    RecyclerView recycler;
    AcervoAdapter adapter;
    List<LivroModel> livros = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton btn_adicionar;
    LivrosDAO livrosDAO;
    AppDataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livros);

        /* Inicializar variaveis */
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        livrosDAO = database.getLivrosDAO();

        /* Obter os livros */
        obtemLivros();

        /* Envento ao clicar no botão adicionar */
        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CadastroLivrosActivity.class);
            }
        });
    }

    /* Método para inicializar variaveis */
    private void init() {
        nenhumLivro = (TextView) findViewById(R.id.tv_nenhum_livro);
        btn_adicionar = (FloatingActionButton) findViewById(R.id.btn_adicionar_livro);
        recycler = (RecyclerView) findViewById(R.id.rv_livros);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
    }

    /* Método para obter os livros */
    public void obtemLivros() {
        try {
            livros = livrosDAO.getLivros();
            if(livros != null && livros.size() > 0) {
                adapter = new AcervoAdapter(livros, this);
                recycler.setAdapter(adapter);
                nenhumLivro.setVisibility(View.GONE);
            } else {
                nenhumLivro.setVisibility(View.VISIBLE);
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
