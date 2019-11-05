package com.example.leiaaqui.Fragments.Acervo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.leiaaqui.Adapters.AcervoAdapter;
import com.example.leiaaqui.AppDataBase;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.Model.LivroModel;
import com.example.leiaaqui.R;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AcervoFragment extends Fragment {

    RecyclerView recycler;
    AcervoAdapter adapter;
    List<LivroModel> acervo = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    LivrosDAO livrosDAO;
    AppDataBase database;
    View root;
    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acervo, container, false);

        /* Inicializar variavéis */
        init();

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(getContext(), AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        livrosDAO = database.getLivrosDAO();

        /* Obter os livros */
        obterLivros();

        return root;
    }

    /* Método para inicializar variaveis */
    public void init() {
        recycler = (RecyclerView) root.findViewById(R.id.rv_acervo);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        textView = root.findViewById(R.id.text_acervo);
        textView.setVisibility(View.VISIBLE);
    }

    /* Obtem os livros */
    public void obterLivros() {
        try {
            acervo = livrosDAO.getLivros();
            if(acervo.size() > 0) {
                adapter = new AcervoAdapter(acervo, getContext());
                recycler.setAdapter(adapter);
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
            }
        } catch(Exception e) {
            Toast.makeText(getContext(), R.string.erro_obter_dados, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}