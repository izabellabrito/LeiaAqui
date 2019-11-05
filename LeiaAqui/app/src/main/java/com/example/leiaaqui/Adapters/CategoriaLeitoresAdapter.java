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

import com.example.leiaaqui.Activities.CategoriaLeitores.CadastroCategoriaLeitores;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.CategoriaLeitoresDAO;
import com.example.leiaaqui.Model.CategoriaLeitoresModel;
import com.example.leiaaqui.R;
import java.util.List;

public class CategoriaLeitoresAdapter extends RecyclerView.Adapter<CategoriaLeitoresAdapter.CategoriaLeitoresViewHolder> {

    private List<CategoriaLeitoresModel> categorias;
    private Context context;
    private CategoriaLeitoresDAO categoriaLeitoresDAO;
    private AppDataBase database;

    public CategoriaLeitoresAdapter(List<CategoriaLeitoresModel> categorias, Context context) {
        this.categorias = categorias;
        this.context = context;
        database = Room.databaseBuilder(context, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        categoriaLeitoresDAO = database.getCategoriaLeitoresDAO();
    }

    @NonNull
    @Override
    public CategoriaLeitoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_categorialeitores, parent, false);
        CategoriaLeitoresViewHolder holder = new CategoriaLeitoresViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaLeitoresViewHolder holder, final int position) {
        holder.codigo.setText("Código da categoria: " + categorias.get(position).getCodigoCategoria());
        holder.descricao.setText("Descrição da categoria: " + categorias.get(position).getDescricao());
        holder.numDias.setText("Dias que esse leitor pode emprestar um livro: " + categorias.get(position).getDiasEmprestimo());

        /* Evento ao clicar no botao deletar de um item da lista */
        holder.deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Verificar se a categoria está sendo usada */
                CategoriaLeitoresModel existe = categoriaLeitoresDAO.getCategoriaById(categorias.get(position).getCodigoCategoria());
                if(existe != null) {
                    Toast.makeText(context, R.string.deletar_categoria_usada, Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.exluir_categoria)
                        .setTitle(R.string.atencao);

                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        categoriaLeitoresDAO.delete(categorias.get(position));
                        categorias.remove(position);
                        Toast.makeText(context, R.string.categoria_deletada, Toast.LENGTH_LONG).show();
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

        /* Evento ao clicar no botao editar de um item da lista */
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CadastroCategoriaLeitores.class);
                intent.putExtra("edicao", true);
                intent.putExtra("codigoCategoria", categorias.get(position).getCodigoCategoria());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class CategoriaLeitoresViewHolder extends RecyclerView.ViewHolder {

        TextView codigo;
        TextView descricao;
        TextView numDias;
        Button editar;
        Button deletar;
        public CategoriaLeitoresViewHolder(@NonNull View itemView) {
            super(itemView);
            codigo = itemView.findViewById(R.id.tv_categoria_leitores_codigo);
            descricao = itemView.findViewById(R.id.tv_categoria_leitores_descricao);
            numDias = itemView.findViewById(R.id.tv_categoria_leitores_maximo_dias);
            editar = itemView.findViewById(R.id.btn_editar_categoria_leitores);
            deletar = itemView.findViewById(R.id.btn_deletar_categoria_leitores);
        }
    }
}