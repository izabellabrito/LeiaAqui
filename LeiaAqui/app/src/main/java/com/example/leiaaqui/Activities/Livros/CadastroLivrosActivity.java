package com.example.leiaaqui.Activities.Livros;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.CadastroFinalizadoActivity;
import com.example.leiaaqui.DAO.CategoriaLivrosDAO;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.EdicaoRealizadaActivity;
import com.example.leiaaqui.MaskEditUtil;
import com.example.leiaaqui.Model.CategoriaLivrosModel;
import com.example.leiaaqui.Model.LivroModel;
import com.example.leiaaqui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CadastroLivrosActivity extends AppCompatActivity {

    FloatingActionButton btn_finalizar;
    TextInputEditText livro_isbn, livro_codigo, livro_titulo, livro_autores, livro_editora, livro_numeroEdicao,
            livro_dataPublicacao, livro_palavrasChave, livro_numeroPaginas;
    TextInputLayout layout_isbn, layout_codigo, layout_titulo, layout_autores, layout_editora, layout_dataPublicacao,
            layout_numeroEdicao, layout_palavrasChave, layout_numeroPaginas;
    Spinner livro_categoria;
    CategoriaLivrosModel livro_categoriaSelecionada;
    LivrosDAO livrosDAO;
    CategoriaLivrosDAO categoriaLivrosDAO;
    AppDataBase database;

    LivroModel editado;
    boolean edicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_livros);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Inicializar variavéis */
        init();
        /* Inicializar validacao */
        initMask();

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        livrosDAO = database.getLivrosDAO();
        categoriaLivrosDAO = database.getCategoriaLivrosDAO();

        /* Inicializar os spinner */
        List<CategoriaLivrosModel> items = categoriaLivrosDAO.getCategoriaLivros();
        ArrayAdapter<CategoriaLivrosModel> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        livro_categoria.setAdapter(adapter);

        /* Evento ao selecionar um item do spinner */
        livro_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                livro_categoriaSelecionada = (CategoriaLivrosModel) ((Spinner) findViewById(R.id.cadastro_livro_categoria)).getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        /* Envento ao clicar no botão finalizar  */
        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });

        /* Validar se há um intent extra de edição, se tiver, quer dizer que usuário selecionou editar */
        edicao = getIntent().getBooleanExtra("edicao", false);
        if(edicao) {
            carregarDados(getIntent().getStringExtra("codigoLivro"));
        }
    }

    /* Método para validar os campos */
    public void validarCampos() {
        if(livro_titulo.getText().length() == 0) {
            layout_titulo.setError("Você precisa inserir o titulo do livro.");
        } else if (livro_autores.getText().length() == 0) {
            layout_autores.setError("Você precisa inserir o(s) autor(es) do livro.");
        } else if (livro_codigo.getText().length() == 0) {
            layout_codigo.setError("Você precisa inserir o código do livro.");
        } else if (livro_dataPublicacao.getText().length() == 0 || livro_dataPublicacao.getText().length() < 10 || !isValidDate(livro_dataPublicacao.getText().toString())) {
            layout_dataPublicacao.setError("Você precisa inserir a data de publicação do livro.");
        } else if (livro_editora.getText().length() == 0) {
            layout_editora.setError("Você precisa inserir a editora do livro.");
        } else if (livro_isbn.getText().length() == 0) {
            layout_isbn.setError("Você precisa inserir o isbn do livro.");
        } else if (livro_numeroEdicao.getText().length() == 0) {
            layout_numeroEdicao.setError("Você precisa inserir a numero de edição do livro.");
        } else if (livro_palavrasChave.getText().length() == 0) {
            layout_palavrasChave.setError("Você precisa inserir as palavras-chaves do livro.");
        } else if (livro_numeroPaginas.getText().length() == 0) {
            layout_palavrasChave.setError("Você precisa inserir a quantidade de páginas do livro.");
        } else if(livro_categoria.getSelectedItem() == null) {
            Toast.makeText(getApplicationContext(), R.string.escolherCategoria, Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (edicao) {
                editarLivro();
            } else {
                salvarLivro();
            }
        }
    }

    /* Método para inicializar variaveis */
    public void init() {
        livro_isbn = (TextInputEditText) findViewById(R.id.cadastro_livro_isbn);
        livro_codigo = (TextInputEditText) findViewById(R.id.cadastro_livro_codigo);
        livro_titulo = (TextInputEditText) findViewById(R.id.cadastro_livro_titulo);
        livro_autores = (TextInputEditText) findViewById(R.id.cadastro_livro_autores);
        livro_editora = (TextInputEditText) findViewById(R.id.cadastro_livro_editora);
        livro_categoria = (Spinner) findViewById(R.id.cadastro_livro_categoria);
        livro_dataPublicacao = (TextInputEditText) findViewById(R.id.cadastro_livro_data_publicacao);
        livro_palavrasChave = (TextInputEditText) findViewById(R.id.cadastro_livro_palavras_chave);
        livro_numeroPaginas = (TextInputEditText) findViewById(R.id.cadastro_livro_numero_paginas);
        livro_numeroEdicao = (TextInputEditText) findViewById(R.id.cadastro_livro_numero_edicao);
        layout_isbn = (TextInputLayout) findViewById(R.id.cadastro_livro_isbn_layout);
        layout_codigo = (TextInputLayout) findViewById(R.id.cadastro_livro_codigo_layout);
        layout_titulo = (TextInputLayout) findViewById(R.id.cadastro_livro_titulo_layout);
        layout_autores = (TextInputLayout) findViewById(R.id.cadastro_livro_autores_layout);
        layout_editora = (TextInputLayout) findViewById(R.id.cadastro_livro_editora_layout);
        layout_dataPublicacao = (TextInputLayout) findViewById(R.id.cadastro_livro_data_publicacao_layout);
        layout_numeroEdicao = (TextInputLayout) findViewById(R.id.cadastro_livro_numero_edicao_layout);
        layout_palavrasChave = (TextInputLayout) findViewById(R.id.cadastro_livro_palavras_chave_layout);
        layout_numeroPaginas = (TextInputLayout) findViewById(R.id.cadastro_livro_numero_paginas_layout);
        btn_finalizar = (FloatingActionButton) findViewById(R.id.btn_cadastro_livro_finalizar);
    }

    /* Metodo para inicializar o mask */
    public void initMask() {
        livro_dataPublicacao.addTextChangedListener(MaskEditUtil.mask(livro_dataPublicacao, layout_dataPublicacao, MaskEditUtil.FORMAT_DATE, true));
    }

    /* Método para salvar o livro */
    public void salvarLivro() {
        try {
            /* Verificação se o livro existe, caso sim, somente aumenta o número de cópias */
            final LivroModel existe = livrosDAO.getLivroByCodigo(livro_codigo.getText().toString());
            if (existe != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.livroDuplicado)
                        .setTitle(R.string.atencao);

                builder.setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        existe.setCopias(existe.getCopias() + 1);
                        livrosDAO.update(existe);
                        startActivity(CadastroFinalizadoActivity.class);
                    }
                });
                builder.setNegativeButton(R.string.voltar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                /* Se o livro não existir, salva */
                LivroModel livro = new LivroModel();
                livro.setTitulo(livro_titulo.getText().toString());
                livro.setCopias(1);
                livro.setAutores(livro_autores.getText().toString());
                livro.setEditora(livro_editora.getText().toString());
                livro.setCodCategoria(livro_categoriaSelecionada.getCodCategoria());
                livro.setCodigo(livro_codigo.getText().toString());
                livro.setDataPublicacao(livro_dataPublicacao.getText().toString());
                livro.setISBN(livro_isbn.getText().toString());
                livro.setNumEdicao(livro_numeroEdicao.getText().toString());
                livro.setParavrasChave(livro_palavrasChave.getText().toString());
                livro.setNumPaginas(Integer.parseInt(livro_numeroPaginas.getText().toString()));
                livrosDAO.insert(livro);
                startActivity(CadastroFinalizadoActivity.class);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_cadastro_livro, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Metodo para editar o livro */
    public void editarLivro() {
        try {
            editado.setTitulo(livro_titulo.getText().toString());
            editado.setCopias(1);
            editado.setAutores(livro_autores.getText().toString());
            editado.setEditora(livro_editora.getText().toString());
            editado.setCodCategoria(livro_categoriaSelecionada.getCodCategoria());
            editado.setCodigo(livro_codigo.getText().toString());
            editado.setDataPublicacao(livro_dataPublicacao.getText().toString());
            editado.setISBN(livro_isbn.getText().toString());
            editado.setNumEdicao(livro_numeroEdicao.getText().toString());
            editado.setParavrasChave(livro_palavrasChave.getText().toString());
            editado.setNumPaginas(Integer.parseInt(livro_numeroPaginas.getText().toString()));
            livrosDAO.update(editado);

            startActivity(EdicaoRealizadaActivity.class);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_editar_livro, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Metodo para caso for edição, carregar os dados do livro à ser editado */
    public void carregarDados(String codigo) {
        try {
            editado = livrosDAO.getLivroByCodigo(codigo);
            if(editado != null) {
                livro_isbn.setText(editado.getISBN());
                livro_codigo.setText(editado.getCodigo());
                livro_titulo.setText(editado.getTitulo());
                livro_autores.setText(editado.getAutores());
                livro_editora.setText(editado.getEditora());
                // todo
                //livro_categoria.setsele
                livro_dataPublicacao.setText(editado.getDataPublicacao());
                livro_numeroEdicao.setText(editado.getNumEdicao());
                livro_palavrasChave.setText(editado.getParavrasChave());
                livro_numeroPaginas.setText(editado.getNumPaginas() + "");
            } else {
                Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Metodo para validar a data */
    public static boolean isValidDate(String pDateString) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        try {
            format.parse(pDateString);
        } catch (ParseException e) {
            return false;
        }
        return true;
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
