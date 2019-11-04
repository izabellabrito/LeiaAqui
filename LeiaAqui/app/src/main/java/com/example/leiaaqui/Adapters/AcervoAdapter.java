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

import com.example.leiaaqui.Activities.Livros.CadastroLivrosActivity;
import com.example.leiaaqui.Activities.Livros.DetalhesLivroActivity;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.Activities.Livros.EmprestimoActivity;
import com.example.leiaaqui.Model.LivroModel;
import com.example.leiaaqui.R;

import java.util.List;

public class AcervoAdapter extends RecyclerView.Adapter<AcervoAdapter.AcervoViewHolder> {

    private List<LivroModel> acervo;
    private Context context;
    private LivrosDAO livrosDAO;
    private AppDataBase database;

    public AcervoAdapter(List<LivroModel> acervo, Context context) {
        this.acervo = acervo;
        this.context = context;
        database = Room.databaseBuilder(context, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        livrosDAO = database.getLivrosDAO();
    }

    @NonNull
    @Override
    public AcervoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_acervo, parent, false);
        AcervoViewHolder holder = new AcervoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AcervoViewHolder holder, final int position) {
        holder.tv_titulo.setText(acervo.get(position).getTitulo());
        holder.tv_autores.setText("Autores: " + acervo.get(position).getAutores());
        holder.tv_editora.setText("Editora: " + acervo.get(position).getEditora());

        /* Se o livro tiver 0 cópias, quer dizer que todos foram emprestados */
        if(acervo.get(position).getCopias() == 0) {
            holder.tv_copias.setText(R.string.todasCopiasEmprestadas);
        } else {
            holder.tv_copias.setText(acervo.get(position).getCopias() + " cópia(s)");
        }

        /* Evento ao clicar no botao emprestar de um item da lista */
        holder.emprestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acervo.get(position).getCopias() == 0) {
                    Toast.makeText(context, R.string.copiasInsuficientes, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, EmprestimoActivity.class);
                intent.putExtra("codigoLivro", acervo.get(position).getCodigo());
                context.startActivity(intent);
            }
        });

        /* Evento ao clicar no botao editar de um item da lista */
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CadastroLivrosActivity.class);
                intent.putExtra("edicao", true);
                intent.putExtra("codigoLivro", acervo.get(position).getCodigo());
                context.startActivity(intent);
            }
        });

        /* Evento ao clicar no botao deletar de um item da lista */
        holder.deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acervo.get(position).getCopias() == 0) {
                    Toast.makeText(context, R.string.livroEmprestado, Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.exluir_livro)
                        .setTitle(R.string.atencao);

                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        livrosDAO.delete(acervo.get(position));
                        acervo.remove(position);
                        Toast.makeText(context, R.string.livro_deletado, Toast.LENGTH_LONG).show();
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

        holder.detalhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetalhesLivroActivity.class);
                intent.putExtra("detalhes", true);
                intent.putExtra("codigoLivro", acervo.get(position).getCodigo());
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return acervo.size();
    }

    public class AcervoViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_titulo;
        private TextView tv_autores;
        private TextView tv_editora;
        private TextView tv_copias;
        Button emprestar;
        Button editar;
        Button deletar;
        Button detalhar;

        public AcervoViewHolder(@NonNull View itemView) {
            super(itemView);
             tv_titulo = itemView.findViewById(R.id.tv_titulo);
             tv_autores = itemView.findViewById(R.id.tv_acervo_autores);
             tv_editora = itemView.findViewById(R.id.tv_acervo_editora);
             tv_copias = itemView.findViewById(R.id.tv_acervo_copias);
             emprestar = itemView.findViewById(R.id.btn_emprestar);
             editar = itemView.findViewById(R.id.btn_editar_livro);
             deletar = itemView.findViewById(R.id.btn_deletar_livro);
             detalhar = itemView.findViewById(R.id.btn_detalhes_livro);
        }
    }
}


