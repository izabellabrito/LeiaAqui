package com.example.leiaaqui.Fragments.Emprestados;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.leiaaqui.Adapters.EmprestadosAdapter;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.CategoriaLivrosDAO;
import com.example.leiaaqui.DAO.EmprestimoDAO;
import com.example.leiaaqui.Model.CategoriaLivrosModel;
import com.example.leiaaqui.Model.EmprestimoModel;
import com.example.leiaaqui.R;

import java.util.ArrayList;
import java.util.List;

public class EmprestadosFragment extends Fragment {

    RecyclerView recycler;
    Spinner search;
    View root;
    TextView textView;
    EmprestadosAdapter adapter;
    List<EmprestimoModel> emprestados = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    AppDataBase database;
    EmprestimoDAO emprestimoDAO;
    CategoriaLivrosDAO categoriaLivrosDAO;
    Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_emprestados, container, false);

        /* Inicializar variavéis */
        init();

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(getContext(), AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        emprestimoDAO = database.getEmprestimosDAO();
        categoriaLivrosDAO = database.getCategoriaLivrosDAO();

        /* Filtrar os empréstimos pela categoria selecionada */
        SharedPreferences prefs = getContext().getSharedPreferences(
                "filtroEmprestados", Context.MODE_PRIVATE);
        String codigo = prefs.getString("codigoFiltro", "-1");
        if(!codigo.equals("-1")) {
            filtrar(codigo);
        }

        /* Iniciando o spinner de busca */
        final List<CategoriaLivrosModel> items = categoriaLivrosDAO.getCategoriaLivros();
        ArrayAdapter<CategoriaLivrosModel> adapter =
                new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, items);
        Spinner navigationSpinner = new Spinner(((AppCompatActivity)getActivity()).getSupportActionBar().getThemedContext());
        navigationSpinner.setAdapter(adapter);
        toolbar.addView(navigationSpinner, 0);

        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrar(items.get(position).getCodCategoria());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        return root;
    }

    /* Método para inicializar variaveis */
    public void init() {
        toolbar = root.findViewById(R.id.toolbar);
        recycler = (RecyclerView) root.findViewById(R.id.rv_emprestados);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        textView = root.findViewById(R.id.tv_emprestados);
        textView.setVisibility(View.VISIBLE);
    }

    /* Método para retornar os livros emprestados com base na categoria selecionada */
    public void filtrar(String codigo) {
        try {
            emprestados = emprestimoDAO.getEmprestimoByCategoria(codigo);
            adapter = new EmprestadosAdapter(emprestados, getContext());
            recycler.setAdapter(adapter);
            if(emprestados != null) {
                if(emprestados.size() > 0) {
                    textView.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}