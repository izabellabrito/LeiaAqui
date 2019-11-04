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

import com.example.leiaaqui.Activities.CategoriaLivros.CadastroCategoriaLivros;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.CategoriaLivrosDAO;
import com.example.leiaaqui.Model.CategoriaLivrosModel;
import com.example.leiaaqui.R;

import java.util.List;

public class CategoriaLivrosAdapter extends RecyclerView.Adapter<CategoriaLivrosAdapter.CategoriaLivrosViewHolder> {

    private List<CategoriaLivrosModel> categorias;
    private Context context;
    private CategoriaLivrosDAO categoriaLivrosDAO;
    private AppDataBase database;

    public CategoriaLivrosAdapter(List<CategoriaLivrosModel> categorias, Context context) {
        this.categorias = categorias;
        this.context = context;
        database = Room.databaseBuilder(context, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        categoriaLivrosDAO = database.getCategoriaLivrosDAO();
    }

    @NonNull
    @Override
    public CategoriaLivrosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_categorialivros, parent, false);
        CategoriaLivrosViewHolder holder = new CategoriaLivrosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaLivrosViewHolder holder, final int position) {
        holder.codigo.setText("Código da categoria: " + categorias.get(position).getCodCategoria());
        holder.descricao.setText("Descrição da categoria: " + categorias.get(position).getDescricaoCategoria());
        holder.multa.setText("Multa por atraso: " + categorias.get(position).getMultaAtraso());
        holder.numMaximoDias.setText("Número de dias que esse livro pode ficar emprestado: " + categorias.get(position).getNumDiasEmprestimo());

        /* Evento ao clicar no botao deletar de um item da lista */
        holder.deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Verificar se a categoria está sendo usada */
                CategoriaLivrosModel existe = categoriaLivrosDAO.getCategoriaByCodigo(categorias.get(position).getCodCategoria());
                if(existe != null) {
                    Toast.makeText(context, R.string.deletar_categoria_usada, Toast.LENGTH_SHORT).show();
                    return;
                }
                
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.exluir_categoria)
                        .setTitle(R.string.atencao);

                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        categoriaLivrosDAO.delete(categorias.get(position));
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
                Intent intent = new Intent(context, CadastroCategoriaLivros.class);
                intent.putExtra("edicao", true);
                intent.putExtra("codigoCategoria", categorias.get(position).getCodCategoria());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class CategoriaLivrosViewHolder extends RecyclerView.ViewHolder {

        TextView codigo;
        TextView descricao;
        TextView multa;
        TextView numMaximoDias;
        Button editar;
        Button deletar;
        public CategoriaLivrosViewHolder(@NonNull View itemView) {
            super(itemView);
            codigo = itemView.findViewById(R.id.tv_categoria_livro_codigo);
            descricao = itemView.findViewById(R.id.tv_categoria_livro_descricao);
            multa = itemView.findViewById(R.id.tv_categoria_livros_multa);
            numMaximoDias = itemView.findViewById(R.id.tv_categoria_livros_maximo_dias);
            editar = itemView.findViewById(R.id.btn_editar_categoria_livros);
            deletar = itemView.findViewById(R.id.btn_deletar_categoria_livros);
        }
    }
}