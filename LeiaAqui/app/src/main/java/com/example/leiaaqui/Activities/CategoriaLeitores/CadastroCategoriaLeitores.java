package com.example.leiaaqui.Activities.CategoriaLeitores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.CadastroFinalizadoActivity;
import com.example.leiaaqui.DAO.CategoriaLeitoresDAO;
import com.example.leiaaqui.Activities.Livros.LivrosActivity;
import com.example.leiaaqui.EdicaoRealizadaActivity;
import com.example.leiaaqui.Model.CategoriaLeitoresModel;
import com.example.leiaaqui.Model.CategoriaLivrosModel;
import com.example.leiaaqui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class CadastroCategoriaLeitores extends AppCompatActivity {

    TextInputEditText categoria_leitor_codigo;
    TextInputEditText categoria_leitor_descricao;
    TextInputEditText categoria_leitor_dias;
    TextInputLayout layout_codigo;
    TextInputLayout layout_descricao;
    TextInputLayout layout_dias;
    FloatingActionButton btn_finalizar;
    CategoriaLeitoresDAO categoriaLeitoresDAO;
    AppDataBase database;
    CategoriaLeitoresModel editado;
    boolean edicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_categoria_leitores);

        /* Inicializar variavéis */
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        categoriaLeitoresDAO = database.getCategoriaLeitoresDAO();

        /* Envento ao clicar no botão finalizar */
        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });

        /* Verificar se no intent possuí um extra de edição, se tiver, é para editar a categoria de leitor */
        edicao = getIntent().getBooleanExtra("edicao", false);
        if(edicao) {
            carregarDados(getIntent().getStringExtra("codigoCategoria"));
        }
    }

    /* Método para inicializar variaveis */
    public void init() {
        categoria_leitor_codigo = (TextInputEditText) findViewById(R.id.cadastro_categoria_leitor_codigo);
        categoria_leitor_descricao = (TextInputEditText) findViewById(R.id.cadastro_categoria_leitor_descricao);
        categoria_leitor_dias = (TextInputEditText) findViewById(R.id.cadastro_categoria_leitor_dias);
        layout_codigo = (TextInputLayout) findViewById(R.id.cadastro_categoria_leitor_codigo_layout);
        layout_descricao = (TextInputLayout) findViewById(R.id.cadastro_categoria_leitor_descricao_layout);
        layout_dias = (TextInputLayout) findViewById(R.id.cadastro_categoria_leitor_dias_layout);
        btn_finalizar = (FloatingActionButton) findViewById(R.id.btn_cadastro_categoria_leitores_finalizado);
    }

    /* Método para validar os campos */
    public void validarCampos() {
        if(categoria_leitor_codigo.getText().length() == 0) {
            layout_codigo.setError("Você precisa inserir a categoria");
        } else if (categoria_leitor_descricao.getText().length() == 0) {
            layout_descricao.setError("Você precisa inserir a descrição da categoria.");
        } else if (categoria_leitor_dias.getText().length() == 0) {
            layout_dias.setError("Você precisa inserir o número de dias.");
        } else {
            if(edicao) {
                editar();
            } else {
                salvar();
            }
        }
    }

    /* Método para salvar a categoria de leitor */
    public void salvar() {
        try {
            CategoriaLeitoresModel existe = categoriaLeitoresDAO.getCategoriaById(categoria_leitor_codigo.getText().toString());
            if(existe != null) {
                Toast.makeText(getApplicationContext(), R.string.categoria_existente, Toast.LENGTH_SHORT).show();
            } else {
                CategoriaLeitoresModel categoria = new CategoriaLeitoresModel();
                categoria.setCodigoCategoria(categoria_leitor_codigo.getText().toString());
                categoria.setDescricao(categoria_leitor_descricao.getText().toString());
                categoria.setDiasEmprestimo(categoria_leitor_dias.getText().toString());
                categoriaLeitoresDAO.insert(categoria);

                startActivity(CadastroFinalizadoActivity.class);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_cadastro_categoria_leitores, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Método para editar a categoria de leitor */
    public void editar() {
        try {
            editado.setCodigoCategoria(categoria_leitor_codigo.getText().toString());
            editado.setDescricao(categoria_leitor_descricao.getText().toString());
            editado.setDiasEmprestimo(categoria_leitor_dias.getText().toString());
            categoriaLeitoresDAO.update(editado);

            startActivity(EdicaoRealizadaActivity.class);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_editar_categoria_leitores, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Método para caso for para editar a categoria de leitor, procura no banco essa categoria e preenche os campos */
    public void carregarDados(String codigo) {
        try {
            editado = categoriaLeitoresDAO.getCategoriaById(codigo);
            if(editado != null) {
                categoria_leitor_codigo.setText(editado.getCodigoCategoria());
                categoria_leitor_descricao.setText(editado.getDescricao());
                categoria_leitor_dias.setText(editado.getDiasEmprestimo());
            } else {
                Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Metódo para iniciar uma activity */
    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.putExtra("activity", CategoriaLeitoresActivity.class);
        startActivity(intent);
    }

    /* Metódo para quando apertar no botão voltar no appbar */
    @Override
    public boolean onSupportNavigateUp(){
        startActivity(CategoriaLeitoresActivity.class);
        return true;
    }
}
