package com.example.leiaaqui.Activities.CategoriaLivros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.CadastroFinalizadoActivity;
import com.example.leiaaqui.DAO.CategoriaLivrosDAO;
import com.example.leiaaqui.EdicaoRealizadaActivity;
import com.example.leiaaqui.Model.CategoriaLivrosModel;
import com.example.leiaaqui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;

public class CadastroCategoriaLivros extends AppCompatActivity {

    TextInputEditText categoria_livro_codigo;
    TextInputEditText categoria_livro_descricao;
    TextInputEditText categoria_livro_dias;
    TextInputEditText categoria_livro_multa;
    TextInputLayout layout_codigo;
    TextInputLayout layout_descricao;
    TextInputLayout layout_dias;
    TextInputLayout layout_multa;
    FloatingActionButton btn_finalizar;
    CategoriaLivrosDAO categoriaLivrosDAO;
    AppDataBase database;
    CategoriaLivrosModel editado;
    boolean edicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_categoria_livros);

        /* Inicializar variavéis */
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Método para inicializar variaveis */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        categoriaLivrosDAO = database.getCategoriaLivrosDAO();

        /* Envento ao clicar no botão finalizar */
        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });

        /* Verificar se no intent possuí um extra de edição, se tiver, é para editar a categoria de livro */
        edicao = getIntent().getBooleanExtra("edicao", false);
        if(edicao) {
            carregarDados(getIntent().getStringExtra("codigoCategoria"));
        }
    }

    /* Método para inicializar variaveis */
    public void init() {
        categoria_livro_codigo = (TextInputEditText) findViewById(R.id.cadastro_categoria_livros_codigo);
        categoria_livro_descricao = (TextInputEditText) findViewById(R.id.cadastro_categoria_livros_descricao);
        categoria_livro_dias = (TextInputEditText) findViewById(R.id.cadastro_categoria_livros_dias);
        categoria_livro_multa = (TextInputEditText) findViewById(R.id.cadastro_categoria_livros_multa);
        layout_codigo = (TextInputLayout) findViewById(R.id.cadastro_categoria_livros_codigo_layout);
        layout_descricao = (TextInputLayout) findViewById(R.id.cadastro_categoria_livros_descricao_layout);
        layout_dias = (TextInputLayout) findViewById(R.id.cadastro_categoria_livros_dias_layout);
        layout_multa = (TextInputLayout) findViewById(R.id.cadastro_categoria_livros_multa_layout);
        btn_finalizar = (FloatingActionButton) findViewById(R.id.btn_cadastro_categoria_livros_finalizado);
    }

    /* Método para validar os campos */
    public void validarCampos() {
        if(categoria_livro_codigo.getText().length() == 0) {
            layout_codigo.setError("Você precisa inserir a categoria");
        } else if (categoria_livro_descricao.getText().length() == 0) {
            layout_descricao.setError("Você precisa inserir a descrição da categoria.");
        } else if (categoria_livro_dias.getText().length() == 0) {
            layout_dias.setError("Você precisa inserir o número de dias.");
        } else if (categoria_livro_multa.getText().length() == 0) {
            layout_multa.setError("Você precisa inserir a multa.");
        } else {
            if(edicao) {
                editar();
            } else {
                salvar();
            }
        }
    }

    /* Método para salvar a categoria de livros */
    public void salvar() {
        try {
            CategoriaLivrosModel existe = categoriaLivrosDAO.getCategoriaByCodigo(categoria_livro_codigo.getText().toString());
            if(existe != null) {
                Toast.makeText(getApplicationContext(), R.string.categoria_existente, Toast.LENGTH_SHORT).show();
            } else {
                CategoriaLivrosModel categoria = new CategoriaLivrosModel();
                categoria.setCodCategoria(categoria_livro_codigo.getText().toString());
                categoria.setDescricaoCategoria(categoria_livro_descricao.getText().toString());
                categoria.setMultaAtraso(categoria_livro_multa.getText().toString());
                categoria.setNumDiasEmprestimo(categoria_livro_dias.getText().toString());
                categoriaLivrosDAO.insert(categoria);

                startActivity(CadastroFinalizadoActivity.class);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_cadastro_categoria_livros, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Método para editar a categoria de livros */
    public void editar() {
        try {
            editado.setCodCategoria(categoria_livro_codigo.getText().toString());
            editado.setMultaAtraso(categoria_livro_multa.getText().toString());
            editado.setDescricaoCategoria(categoria_livro_descricao.getText().toString());
            editado.setNumDiasEmprestimo(categoria_livro_dias.getText().toString());
            categoriaLivrosDAO.update(editado);

            startActivity(EdicaoRealizadaActivity.class);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_editar_categoria_livros, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Método para caso for para editar a categoria de leitor, procura no banco essa categoria e preenche os campos */
    public void carregarDados(String codigo) {
        try {
            editado = categoriaLivrosDAO.getCategoriaByCodigo(codigo);
            if(editado != null) {
                categoria_livro_codigo.setText(editado.getCodCategoria());
                categoria_livro_descricao.setText(editado.getDescricaoCategoria());
                categoria_livro_multa.setText(editado.getMultaAtraso());
                categoria_livro_dias.setText(editado.getNumDiasEmprestimo());
            } else {
                Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Metódo para iniciar uma activity */
    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.putExtra("activity", CategoriaLivrosActivity.class);
        startActivity(intent);
    }

    /* Metódo para quando apertar no botão voltar no appbar */
    @Override
    public boolean onSupportNavigateUp(){
        startActivity(CategoriaLivrosActivity.class);
        return true;
    }
}
