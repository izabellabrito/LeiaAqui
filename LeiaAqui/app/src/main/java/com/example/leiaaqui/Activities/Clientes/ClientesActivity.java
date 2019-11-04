package com.example.leiaaqui.Activities.Clientes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leiaaqui.Adapters.ClientesAdapter;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.ClienteDAO;
import com.example.leiaaqui.MainActivity;
import com.example.leiaaqui.Model.ClienteModel;
import com.example.leiaaqui.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ClientesActivity extends AppCompatActivity {

    TextView nenhumCliente;
    RecyclerView recycler;
    ClientesAdapter adapter;
    List<ClienteModel> clientes = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton btn_adicionar;
    ClienteDAO clienteDAO;
    AppDataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        /* Inicializar variavéis */
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        clienteDAO = database.getClienteDAO();

        /* Obtem clientes */
        obterClientes();

        /* Envento ao clicar no botão editar */
        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CadastroClientesActivity.class);
            }
        });
    }

    /* Método para inicializar variaveis */
    public void init() {
        nenhumCliente = (TextView) findViewById(R.id.tv_nenhum_cliente);
        btn_adicionar = (FloatingActionButton) findViewById(R.id.btn_adicionar_cliente);
        recycler = (RecyclerView) findViewById(R.id.rv_clientes);
        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
    }

    /* Método para obter os clientes */
    public void obterClientes() {
        try {
            clientes = clienteDAO.getClientes();
            if(clientes != null && clientes.size() > 0) {
                adapter = new ClientesAdapter(clientes, this);
                recycler.setAdapter(adapter);
                nenhumCliente.setVisibility(View.GONE);
            } else {
                nenhumCliente.setVisibility(View.VISIBLE);
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
