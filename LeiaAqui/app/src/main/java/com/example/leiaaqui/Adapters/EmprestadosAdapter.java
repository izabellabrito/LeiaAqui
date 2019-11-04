package com.example.leiaaqui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.leiaaqui.Activities.Livros.DevolucaoActivity;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.ClienteDAO;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.Model.ClienteModel;
import com.example.leiaaqui.Model.EmprestimoModel;
import com.example.leiaaqui.Model.LivroModel;
import com.example.leiaaqui.R;
import java.util.List;

public class EmprestadosAdapter extends RecyclerView.Adapter<EmprestadosAdapter.EmprestadosViewHolder> {

    private List<EmprestimoModel> emprestados;
    private Context context;
    AppDataBase database;
    LivrosDAO livrosDAO;
    ClienteDAO clienteDAO;

    public EmprestadosAdapter(List<EmprestimoModel> emprestados, Context context) {
        this.emprestados = emprestados;
        this.context = context;
        database = Room.databaseBuilder(context, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        livrosDAO = database.getLivrosDAO();
        clienteDAO = database.getClienteDAO();
    }

    @NonNull
    @Override
    public EmprestadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_emprestados, parent, false);
        EmprestadosViewHolder holder = new EmprestadosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmprestadosViewHolder holder, final int position) {
        LivroModel livro = livrosDAO.getLivroByCodigo(emprestados.get(position).getCodigoLivro());
        ClienteModel cliente = clienteDAO.getClienteById(emprestados.get(position).getClienteId());

        holder.tv_titulo.setText("Livro: " + livro.getTitulo());
        holder.tv_cliente.setText("Cliente: " + cliente.getNome());
        holder.tv_retirada.setText("Data de retirada: " + emprestados.get(position).getDataRetirada());
        holder.tv_previsao.setText("Previsão de devolução: " + emprestados.get(position).getPrevisaoDevolucao());

        /* Evento ao clicar no botao devolver de um item da lista */
        holder.devolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DevolucaoActivity.class);
                intent.putExtra("devolucao", true);
                intent.putExtra("devolucaoId", emprestados.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emprestados.size();
    }

    public class EmprestadosViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_titulo;
        private TextView tv_cliente;
        private TextView tv_retirada;
        private TextView tv_previsao;
        private Button devolver;
        public EmprestadosViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_titulo = itemView.findViewById(R.id.tv_titulo_emprestado);
            tv_cliente = itemView.findViewById(R.id.tv_cliente_emprestado);
            tv_retirada = itemView.findViewById(R.id.tv_emprestado_data_retirada);
            tv_previsao = itemView.findViewById(R.id.tv_emprestimo_previsao);
            devolver = itemView.findViewById(R.id.btn_devolver);
        }
    }
}