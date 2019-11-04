package com.example.leiaaqui.Activities.Livros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leiaaqui.Activities.Clientes.ClientesActivity;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.CategoriaLivrosDAO;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.Model.CategoriaLivrosModel;
import com.example.leiaaqui.Model.LivroModel;
import com.example.leiaaqui.R;

public class DetalhesLivroActivity extends AppCompatActivity {

    TextView copias;
    TextView codigo;
    TextView ISBN;
    TextView titulo;
    TextView codCategoria;
    TextView categoria;
    TextView autores;
    TextView paravrasChave;
    TextView dataPublicacao;
    TextView numEdicao;
    TextView editora;
    TextView numPaginas;
    AppDataBase database;
    LivrosDAO livrosDAO;
    CategoriaLivrosDAO categoriaLivrosDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_livro);

        /* Inicializar variavéis */
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        livrosDAO = database.getLivrosDAO();
        categoriaLivrosDAO = database.getCategoriaLivrosDAO();

        /* Verificar se no intent possuí um extra de edição, se tiver, é para editar o cliente */
        boolean detalhes = getIntent().getBooleanExtra("detalhes", false);
        if(detalhes) {
            carregarDados(getIntent().getStringExtra("codigoLivro"));
        }
    }

    /* Método para inicializar as variavéis */
    public void init() {
        copias = (TextView) findViewById(R.id.detalhe_livro_copias);
        codigo = (TextView) findViewById(R.id.detalhe_livro_codigo);
        ISBN = (TextView) findViewById(R.id.detalhe_livro_isbn);
        titulo = (TextView) findViewById(R.id.detalhe_livro_titulo);
        codCategoria = (TextView) findViewById(R.id.detalhe_livro_codigo_categoria);
        categoria = (TextView) findViewById(R.id.detalhe_livro_categoria);
        autores = (TextView) findViewById(R.id.detalhe_livro_autores);
        paravrasChave = (TextView) findViewById(R.id.detalhe_livro_palavras_chave);
        dataPublicacao = (TextView) findViewById(R.id.detalhe_livro_data_publicacao);
        numEdicao = (TextView) findViewById(R.id.detalhe_livro_num_edicao);
        editora = (TextView) findViewById(R.id.detalhe_livro_editora);
        numPaginas = (TextView) findViewById(R.id.detalhe_livro_num_paginas);
    }

    /* Método para obter os dados e preencher os campos */
    public void carregarDados(String cod) {
        try {
            LivroModel livro = livrosDAO.getLivroByCodigo(cod);
            if (livro != null) {
                CategoriaLivrosModel cat = categoriaLivrosDAO.getCategoriaByCodigo(livro.getCodCategoria());
                copias.setText("Cópias: " + livro.getCopias());
                codigo.setText("Código: " + livro.getCodigo());
                ISBN.setText("ISBN: " + livro.getISBN());
                titulo.setText("Titulo: " + livro.getTitulo());
                codCategoria.setText("Código da categoria: " + livro.getCodCategoria());
                categoria.setText("Descrição da categoria: " + cat.getDescricaoCategoria());
                autores.setText("Autores: " + livro.getAutores());
                paravrasChave.setText("Palavras-chave: " + livro.getParavrasChave());
                dataPublicacao.setText("Data de publicação: " + livro.getDataPublicacao());
                numEdicao.setText("Número de edição: " + livro.getNumEdicao());
                editora.setText("Editora: " + livro.getEditora());
                numPaginas.setText("Número de páginas: " + livro.getNumPaginas());
            }
        } catch (Exception e) {
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
        startActivity(LivrosActivity.class);
        return true;
    }
}
