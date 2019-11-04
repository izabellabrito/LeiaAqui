package com.example.leiaaqui.Activities.Clientes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leiaaqui.Activities.Livros.LivrosActivity;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.CategoriaLeitoresDAO;
import com.example.leiaaqui.DAO.CategoriaLivrosDAO;
import com.example.leiaaqui.DAO.ClienteDAO;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.Model.CategoriaLeitoresModel;
import com.example.leiaaqui.Model.CategoriaLivrosModel;
import com.example.leiaaqui.Model.ClienteModel;
import com.example.leiaaqui.Model.LivroModel;
import com.example.leiaaqui.R;

public class DetalhesClienteActivity extends AppCompatActivity {

    TextView nome;
    TextView cpf;
    TextView endereco;
    TextView celular;
    TextView email;
    TextView dataNascimento;
    TextView codCategoria;
    TextView categoria;
    AppDataBase database;
    ClienteDAO clienteDAO;
    CategoriaLeitoresDAO categoriaLeitoresDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_cliente);

        /* Inicializar variavéis */
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        clienteDAO = database.getClienteDAO();
        categoriaLeitoresDAO = database.getCategoriaLeitoresDAO();

        /* Verificar se no intent possuí um extra de edição, se tiver, é para editar o cliente */
        boolean detalhes = getIntent().getBooleanExtra("detalhes", false);
        if(detalhes) {
            carregarDados(getIntent().getLongExtra("clienteId", -1));
        }
    }

    /* Método para inicializar as variavéis */
    public void init() {
        nome = (TextView) findViewById(R.id.detalhe_cliente_nome);
        cpf = (TextView) findViewById(R.id.detalhe_cliente_cpf);
        endereco = (TextView) findViewById(R.id.detalhe_cliente_endereco);
        celular = (TextView) findViewById(R.id.detalhe_cliente_celular);
        email = (TextView) findViewById(R.id.detalhe_cliente_email);
        dataNascimento = (TextView) findViewById(R.id.detalhe_cliente_data_nascimento);
        codCategoria = (TextView) findViewById(R.id.detalhe_cliente_codigo_categoria);
        categoria = (TextView) findViewById(R.id.detalhe_cliente_categoria);
    }

    /* Método para obter os dados e preencher os campos */
    public void carregarDados(Long cod) {
        try {
            ClienteModel cliente = clienteDAO.getClienteById(cod);
            if (cliente != null) {
                CategoriaLeitoresModel cat = categoriaLeitoresDAO.getCategoriaById(cliente.getCodCategoria());
                nome.setText("Nome: " + cliente.getNome());
                cpf.setText("CPF: " + cliente.getCpf());
                endereco.setText("Endereço: " + cliente.getEndereco());
                celular.setText("Celular: " + cliente.getCelular());
                email.setText("E-mail: " + cliente.getEmail());
                dataNascimento.setText("Data de nascimento: " + cliente.getDataNascimento());
                codCategoria.setText("Código da categoria: " + cliente.getCodCategoria());
                categoria.setText("Descrição da categoria: " + cat.getDescricao());
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
        startActivity(ClientesActivity.class);
        return true;
    }
}
