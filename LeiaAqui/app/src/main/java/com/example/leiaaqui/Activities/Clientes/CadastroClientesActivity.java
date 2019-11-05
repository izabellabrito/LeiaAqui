package com.example.leiaaqui.Activities.Clientes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.CadastroFinalizadoActivity;
import com.example.leiaaqui.DAO.CategoriaLeitoresDAO;
import com.example.leiaaqui.DAO.ClienteDAO;
import com.example.leiaaqui.EdicaoRealizadaActivity;
import com.example.leiaaqui.MaskEditUtil;
import com.example.leiaaqui.Model.CategoriaLeitoresModel;
import com.example.leiaaqui.Model.ClienteModel;
import com.example.leiaaqui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CadastroClientesActivity extends AppCompatActivity {

    TextInputEditText cliente_nome;
    TextInputEditText cliente_cpf;
    TextInputEditText cliente_endereco;
    TextInputEditText cliente_dataNascimento;
    TextInputEditText cliente_email;
    TextInputEditText cliente_celular;
    Spinner cliente_categoria;
    TextInputLayout layout_nome;
    TextInputLayout layout_cpf;
    TextInputLayout layout_endereco;
    TextInputLayout layout_dataNascimento;
    TextInputLayout layout_email;
    TextInputLayout layout_celular;
    FloatingActionButton btn_finalizar;
    CategoriaLeitoresModel categoriaSelecionada;
    ClienteDAO clienteDAO;
    CategoriaLeitoresDAO categoriaLeitoresDAO;
    AppDataBase database;
    ClienteModel editado;
    boolean edicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_clientes);

        /* Inicializar variavéis */
        init();
        /* Inicializar validacao */
        initMask();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        clienteDAO = database.getClienteDAO();
        categoriaLeitoresDAO = database.getCategoriaLeitoresDAO();

        /* Inicializar os spinner */
        List<CategoriaLeitoresModel> items = categoriaLeitoresDAO.getCategoriasLeitores();
        ArrayAdapter<CategoriaLeitoresModel> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        cliente_categoria.setAdapter(adapter);

        /* Evento ao selecionar um item do spinner */
        cliente_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaSelecionada = (CategoriaLeitoresModel) ((Spinner) findViewById(R.id.cadastro_cliente_categoria)).getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        /* Envento ao clicar no botão finalizar */
        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });

        /* Verificar se no intent possuí um extra de edição, se tiver, é para editar o cliente */
        edicao = getIntent().getBooleanExtra("edicao", false);
        if(edicao) {
            carregarDados(getIntent().getLongExtra("clienteId", -1));
        }
    }

    /* Método para inicializar variaveis */
    public void init() {
        cliente_nome = (TextInputEditText) findViewById(R.id.cadastro_cliente_nome);
        cliente_cpf = (TextInputEditText) findViewById(R.id.cadastro_cliente_cpf);
        cliente_endereco = (TextInputEditText) findViewById(R.id.cadastro_cliente_endereco);
        cliente_dataNascimento = (TextInputEditText) findViewById(R.id.cadastro_cliente_dataNascimento);
        cliente_email = (TextInputEditText) findViewById(R.id.cadastro_cliente_email);
        cliente_celular = (TextInputEditText) findViewById(R.id.cadastro_cliente_celular);
        cliente_categoria = (Spinner) findViewById(R.id.cadastro_cliente_categoria);
        btn_finalizar = (FloatingActionButton) findViewById(R.id.btn_cadastro_cliente_finalizar);
        layout_celular = (TextInputLayout) findViewById(R.id.cadastro_cliente_celular_layout);
        layout_cpf = (TextInputLayout) findViewById(R.id.cadastro_cliente_cpf_layout);
        layout_dataNascimento = (TextInputLayout) findViewById(R.id.cadastro_cliente_dataNascimento_layout);
        layout_email = (TextInputLayout) findViewById(R.id.cadastro_cliente_email_layout);
        layout_endereco = (TextInputLayout) findViewById(R.id.cadastro_cliente_endereco_layout);
        layout_nome = (TextInputLayout) findViewById(R.id.cadastro_cliente_nome_layout);
    }

    /* Método para validar os campos */
    public void validarCampos() {
        if(cliente_nome.getText().length() == 0) {
            layout_nome.setError("Você precisa inserir o nome do cliente.");
        } else if (cliente_cpf.getText().length() == 0 || cliente_cpf.length() < 14) {
            layout_cpf.setError("Você precisa inserir o cpf do cliente.");
        } else if (cliente_endereco.getText().length() == 0) {
            layout_endereco.setError("Você precisa inserir o endereço do cliente.");
        } else if (cliente_dataNascimento.getText().length() == 0 || cliente_dataNascimento.getText().length() < 10 || !isValidDate(cliente_dataNascimento.getText().toString())) {
            layout_dataNascimento.setError("Você precisa a data de nascimento do cliente.");
        } else if (cliente_email.getText().length() == 0) {
            layout_email.setError("Você precisa o e-mail do cliente.");
        } else if (cliente_celular.length() == 0 || cliente_celular.length() < 16) {
            layout_celular.setError("Você precisa inserir o telefone do cliente.");
        } else if (cliente_categoria.getSelectedItem() == null) {
            Toast.makeText(getApplicationContext(), R.string.escolherCategoria, Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (edicao) {
                editar();
            } else {
                salvar();
            }
        }
    }

    /* Metodo para inicializar o mask */
    public void initMask() {
        cliente_dataNascimento.addTextChangedListener(MaskEditUtil.mask(cliente_dataNascimento, layout_dataNascimento, MaskEditUtil.FORMAT_DATE, true));
        cliente_cpf.addTextChangedListener(MaskEditUtil.mask(cliente_cpf, layout_cpf, MaskEditUtil.FORMAT_CPF, false));
        cliente_celular.addTextChangedListener(MaskEditUtil.mask(cliente_celular, layout_celular, MaskEditUtil.FORMAT_FONE, false));
    }

    /* Método para salvar o cliente */
    public void salvar() {
        try {
            ClienteModel existe = clienteDAO.getClienteByCPF(cliente_cpf.getText().toString());
            if(existe != null) {
                Toast.makeText(getApplicationContext(), R.string.cliente_existente, Toast.LENGTH_SHORT).show();
            } else {
                ClienteModel cliente = new ClienteModel();
                cliente.setNome(cliente_nome.getText().toString());
                cliente.setCpf(cliente_cpf.getText().toString());
                cliente.setDataNascimento(cliente_dataNascimento.getText().toString());
                cliente.setEndereco(cliente_endereco.getText().toString());
                cliente.setCelular(cliente_celular.getText().toString());
                cliente.setCodCategoria(categoriaSelecionada.getCodigoCategoria());
                cliente.setEmail(cliente_email.getText().toString());
                clienteDAO.insert(cliente);

                startActivity(CadastroFinalizadoActivity.class);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_cadastro_cliente, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Método para editar o cliente */
    public void editar() {
        try {
            editado.setEndereco(cliente_endereco.getText().toString());
            editado.setNome(cliente_nome.getText().toString());
            editado.setEmail(cliente_email.getText().toString());
            editado.setDataNascimento(cliente_dataNascimento.getText().toString());
            editado.setCpf(cliente_cpf.getText().toString());
            editado.setCodCategoria(categoriaSelecionada.getCodigoCategoria());
            editado.setCelular(cliente_celular.getText().toString());
            clienteDAO.update(editado);

            startActivity(EdicaoRealizadaActivity.class);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_editar_cliente, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Método para caso for para editar a categoria de leitor, procura no banco essa categoria e preenche os campos */
    public void carregarDados(Long codigo) {
        try {
            editado = clienteDAO.getClienteById(codigo);
            cliente_nome.setText(editado.getNome());
            cliente_endereco.setText(editado.getEndereco());
            cliente_celular.setText(editado.getCelular());
            cliente_cpf.setText(editado.getCpf());
            cliente_email.setText(editado.getEmail());
            cliente_dataNascimento.setText(editado.getDataNascimento());
            // todo
            //cliente_categoria.setText(editado.getCodCategoria());
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
        intent.putExtra("activity", ClientesActivity.class);
        startActivity(intent);
    }

    /* Metódo para quando apertar no botão voltar no appbar */
    @Override
    public boolean onSupportNavigateUp(){
        startActivity(ClientesActivity.class);
        return true;
    }
}
