package com.example.leiaaqui.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.leiaaqui.Activities.Clientes.CadastroClientesActivity;
import com.example.leiaaqui.Activities.Clientes.DetalhesClienteActivity;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.ClienteDAO;
import com.example.leiaaqui.DAO.EmprestimoDAO;
import com.example.leiaaqui.Model.ClienteModel;
import com.example.leiaaqui.Model.EmprestimoModel;
import com.example.leiaaqui.R;
import java.util.List;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClientesViewHolder> {

    private List<ClienteModel> clientes;
    private Context context;
    private ClienteDAO clienteDAO;
    private EmprestimoDAO emprestimoDAO;
    private AppDataBase database;

    public ClientesAdapter(List<ClienteModel> clientes, Context context) {
        this.clientes = clientes;
        this.context = context;
        database = Room.databaseBuilder(context, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        clienteDAO = database.getClienteDAO();
        emprestimoDAO = database.getEmprestimosDAO();
    }

    @NonNull
    @Override
    public ClientesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_cliente, parent, false);
        ClientesViewHolder holder = new ClientesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientesViewHolder holder, final int position) {
        holder.cpf.setText("CPF: " + clientes.get(position).getCpf());
        holder.nome.setText("Nome: " + clientes.get(position).getNome());
        holder.categoria.setText("Categoria: " + clientes.get(position).getCodCategoria());

        /* Evento ao clicar no botao deletar de um item da lista */
        holder.deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Verificar se tem algum livro emprestado para esse cliente */
                EmprestimoModel emprestado = emprestimoDAO.getEmprestimoByCliente(clientes.get(position).getId());
                if(emprestado != null) {
                    Toast.makeText(context, R.string.deletar_cliente_usado, Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.exluir_cliente)
                        .setTitle(R.string.atencao);

                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clienteDAO.delete(clientes.get(position));
                        clientes.remove(position);
                        Toast.makeText(context, R.string.cliente_deletado, Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CadastroClientesActivity.class);
                intent.putExtra("edicao", true);
                intent.putExtra("clienteId", clientes.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.detalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetalhesClienteActivity.class);
                intent.putExtra("detalhes", true);
                intent.putExtra("clienteId", clientes.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public class ClientesViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView cpf;
        TextView categoria;
        Button editar;
        Button deletar;
        Button detalhes;
        public ClientesViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tv_cliente_nome);
            cpf = itemView.findViewById(R.id.tv_cliente_cpf);
            categoria = itemView.findViewById(R.id.tv_cliente_categoria);
            editar = itemView.findViewById(R.id.btn_editar_cliente);
            deletar = itemView.findViewById(R.id.btn_deletar_cliente);
            detalhes = itemView.findViewById(R.id.btn_detalhes_cliente);
        }
    }
}
