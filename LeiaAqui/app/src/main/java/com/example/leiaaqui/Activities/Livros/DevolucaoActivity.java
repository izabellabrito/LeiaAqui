package com.example.leiaaqui.Activities.Livros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.ClienteDAO;
import com.example.leiaaqui.DAO.EmprestimoDAO;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.DevolucaoRealizadaActivity;
import com.example.leiaaqui.MainActivity;
import com.example.leiaaqui.Model.ClienteModel;
import com.example.leiaaqui.Model.EmprestimoModel;
import com.example.leiaaqui.Model.LivroModel;
import com.example.leiaaqui.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DevolucaoActivity extends AppCompatActivity {

    TextView cliente;
    TextView dataRetirada;
    TextView livro;
    TextView dataDevolucao;
    Button devolver;
    Long codigoEprestimo;
    AppDataBase database;
    ClienteDAO clienteDAO;
    LivrosDAO livrosDAO;
    EmprestimoDAO emprestimoDAO;
    ClienteModel clienteSelecionado;
    LivroModel livroSelecionado;
    EmprestimoModel emprestimo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucao);

        /* Inicializar variavéis */
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        clienteDAO = database.getClienteDAO();
        livrosDAO = database.getLivrosDAO();
        emprestimoDAO = database.getEmprestimosDAO();

        /* Pegar no intent os dados da do livro a ser devolvido */
        codigoEprestimo = getIntent().getLongExtra("devolucaoId", -1);
        if(codigoEprestimo != -1) {
            carregaDados();
        } else {
            Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
        }

        /* Envento ao clicar no botão "devolver" */
        devolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devolver();
            }
        });
    }

    /* Método para inicializar variaveis */
    public void init() {
        livro = findViewById(R.id.tv_devolucao_livro);
        cliente = findViewById(R.id.tv_devolucao_cliente);
        dataRetirada = findViewById(R.id.tv_devolucao_data_retirada);
        dataDevolucao = findViewById(R.id.devolucao_data);
        devolver = findViewById(R.id.btn_confirmar_devolver);
    }

    /* Método para carregar dos dados da devolução */
    public void carregaDados() {
        try {
            emprestimo = emprestimoDAO.getEmprestimoById(codigoEprestimo);
            List este = emprestimoDAO.getEmprestimos();
            if(emprestimo != null) {
                SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String dataAtual = formataData.format(date);

                clienteSelecionado = clienteDAO.getClienteById(emprestimo.getClienteId());
                livroSelecionado = livrosDAO.getLivroByCodigo(emprestimo.getCodigoLivro());

                livro.setText("Livro: " + livroSelecionado.getTitulo());
                cliente.setText("Cliente: " + clienteSelecionado.getNome());
                dataRetirada.setText("Data de retirada: " + emprestimo.getDataRetirada());
                dataDevolucao.setText("Data de devolução: " + dataAtual);
            } else {
                Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /* Método para devolver o livro */
    public void devolver() {
        try {
            emprestimoDAO.delete(emprestimo);
            livroSelecionado.setCopias(livroSelecionado.getCopias() + 1);
            livrosDAO.update(livroSelecionado);

            startActivity(DevolucaoRealizadaActivity.class);
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), R.string.erro_devolver_livro, Toast.LENGTH_SHORT).show();
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
