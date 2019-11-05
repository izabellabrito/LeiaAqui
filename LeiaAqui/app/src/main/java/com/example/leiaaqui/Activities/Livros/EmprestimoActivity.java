package com.example.leiaaqui.Activities.Livros;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.CategoriaLeitoresDAO;
import com.example.leiaaqui.DAO.CategoriaLivrosDAO;
import com.example.leiaaqui.DAO.ClienteDAO;
import com.example.leiaaqui.DAO.EmprestimoDAO;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.EmprestimoRealizadoActivity;
import com.example.leiaaqui.MainActivity;
import com.example.leiaaqui.MaskEditUtil;
import com.example.leiaaqui.Model.CategoriaLeitoresModel;
import com.example.leiaaqui.Model.CategoriaLivrosModel;
import com.example.leiaaqui.Model.ClienteModel;
import com.example.leiaaqui.Model.EmprestimoModel;
import com.example.leiaaqui.Model.LivroModel;
import com.example.leiaaqui.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmprestimoActivity extends AppCompatActivity {

    TextView livro;
    TextView data;
    TextView diasEmprestimo;
    TextView multa;
    Spinner categoria;
    Spinner cliente;
    EditText previsao_devolucao;
    TextInputLayout previsao_devolucao_layout;
    Button emprestar;
    TextView errorSpinnerCategoria;
    TextView errorSpinnerCliente;
    RecyclerView recycler;
    CategoriaLeitoresModel categoriaSelecionada;
    ClienteModel clienteSelecionado;
    LivrosDAO livrosDAO;
    EmprestimoDAO emprestimoDAO;
    ClienteDAO clienteDAO;
    CategoriaLeitoresDAO categoriaLeitoresDAO;
    CategoriaLivrosDAO categoriaLivrosDAO;
    AppDataBase database;
    LivroModel livroSelecionado;
    String codigoLivro;
    List<CategoriaLeitoresModel> categorias;
    List<ClienteModel> clientes;
    ArrayAdapter<CategoriaLeitoresModel> adapterCategorias;
    ArrayAdapter<ClienteModel> adapterClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprestimo);

        /* Inicializar variavéis */
        init();
        initMask();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        livrosDAO = database.getLivrosDAO();
        emprestimoDAO = database.getEmprestimosDAO();
        clienteDAO = database.getClienteDAO();
        categoriaLeitoresDAO = database.getCategoriaLeitoresDAO();
        categoriaLivrosDAO = database.getCategoriaLivrosDAO();
        categorias = categoriaLeitoresDAO.getCategoriasLeitores();
        clientes = clienteDAO.getClientes();

        /* Inicializar os spinner */
        adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
        categoria.setAdapter(adapterCategorias);
        adapterClientes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, clientes);
        cliente.setAdapter(adapterClientes);

        /* Evento para quando selecionar um item do spinner */
        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaSelecionada = (CategoriaLeitoresModel) ((Spinner) findViewById(R.id.spinner_emprestimo_cliente_categoria)).getSelectedItem();
                clientes = clienteDAO.getClientesByCategoria(categoriaSelecionada.getCodigoCategoria());
                adapterClientes = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, clientes);
                cliente.setAdapter(adapterClientes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        /* Evento para quando selecionar um item do spinner */
        cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clienteSelecionado = (ClienteModel) ((Spinner) findViewById(R.id.spinner_emprestimo_cliente_nome)).getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        /* Pegar no intent os dados da do livro a ser emprestado */
        codigoLivro = getIntent().getStringExtra("codigoLivro");
        if(codigoLivro != null) {
            carregaDados();
        } else {
            Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
        }

        /* Envento ao clicar no botão "emprestar" */
        emprestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });
    }

    /* Método para inicializar variaveis */
    public void init() {
        livro = (TextView) findViewById(R.id.tv_emprestimo_livro);
        data = (TextView) findViewById(R.id.tv_emprestimo_data);
        diasEmprestimo = (TextView) findViewById(R.id.tv_emprestimo_previsao);
        multa = (TextView) findViewById(R.id.tv_emprestimo_multa);
        categoria = (Spinner) findViewById(R.id.spinner_emprestimo_cliente_categoria);
        cliente = (Spinner) findViewById(R.id.spinner_emprestimo_cliente_nome);
        emprestar = (Button) findViewById(R.id.btn_emprestar);
        errorSpinnerCategoria = (TextView) categoria.getSelectedView();
        errorSpinnerCliente = (TextView) cliente.getSelectedView();
        recycler = (RecyclerView) findViewById(R.id.rv_livros);
        previsao_devolucao = (EditText) findViewById(R.id.et_emprestimo_previsao);
        previsao_devolucao_layout = (TextInputLayout) findViewById(R.id.emprestivo_previsao_layout);
    }

    /* Metodo para inicializar o mask */
    public void initMask() {
        previsao_devolucao.addTextChangedListener(MaskEditUtil.mask(previsao_devolucao, previsao_devolucao_layout, MaskEditUtil.FORMAT_DATE, true));
    }

    /* Método para validar os campos */
    public void validarCampos() {
        if (cliente.getSelectedItem() == null) {
            Toast.makeText(getApplicationContext(), R.string.escolherCliente, Toast.LENGTH_SHORT).show();
        } else if(previsao_devolucao.getText().length() == 0 || previsao_devolucao.getText().length() < 10 || !isValidDate(previsao_devolucao.getText().toString())) {
            previsao_devolucao_layout.setError("Você precisa inserir a previsão de devolução do livro.");
        } else {
            emprestarLivro();
        }
    }

    /* Método para carregar dos dados do empréstimo */
    public void carregaDados() {
        try {
            livroSelecionado = livrosDAO.getLivroByCodigo(codigoLivro);
            if(livro != null) {
                SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String dataAtual = formataData.format(date);

                CategoriaLivrosModel categoriaLivro = categoriaLivrosDAO.getCategoriaByCodigo(livroSelecionado.getCodCategoria());

                livro.setText("Livro selecionado: " + livroSelecionado.getTitulo());
                data.setText("Data de retirada: " + dataAtual);
                diasEmprestimo.setText("Dias que esse livro pode ficar emprestado: " + categoriaLivro.getNumDiasEmprestimo());
                multa.setText("Multa por atraso: " + categoriaLivro.getMultaAtraso());
            } else {
                Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Método para emprestar o livro */
    public void emprestarLivro() {
        try {
            if (livroSelecionado != null ) {
                SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String dataAtual = formataData.format(date);

                livroSelecionado.setCopias(livroSelecionado.getCopias() - 1);
                livrosDAO.update(livroSelecionado);

                EmprestimoModel emprestimo = new EmprestimoModel();
                emprestimo.setClienteId(clienteSelecionado.getId());
                emprestimo.setCodigoLivro(livroSelecionado.getCodigo());
                emprestimo.setPrevisaoDevolucao(previsao_devolucao.getText().toString());
                emprestimo.setDataRetirada(dataAtual);
                emprestimoDAO.insert(emprestimo);

                startActivity(EmprestimoRealizadoActivity.class);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_emprestar_livro, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Metodo para validar a data */
    public static boolean isValidDate(String pDateString) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);

        try {
            Date date = format.parse(pDateString);
            if(new Date().before(date)) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false;
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
