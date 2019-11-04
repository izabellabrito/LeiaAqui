package com.example.leiaaqui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.leiaaqui.Activities.CategoriaLeitores.CategoriaLeitoresActivity;
import com.example.leiaaqui.Activities.CategoriaLivros.CategoriaLivrosActivity;
import com.example.leiaaqui.Activities.Clientes.ClientesActivity;
import com.example.leiaaqui.Activities.Livros.LivrosActivity;
import com.example.leiaaqui.DAO.CategoriaLeitoresDAO;
import com.example.leiaaqui.DAO.CategoriaLivrosDAO;
import com.example.leiaaqui.DAO.ClienteDAO;
import com.example.leiaaqui.DAO.LivrosDAO;
import com.example.leiaaqui.Model.CategoriaLeitoresModel;
import com.example.leiaaqui.Model.CategoriaLivrosModel;
import com.example.leiaaqui.Model.LivroModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;
    BottomNavigationView navView;
    NavigationView navDrawer;
    DrawerLayout drawer;
    LivrosDAO livrosDAO;
    ClienteDAO clienteDAO;
    CategoriaLeitoresDAO categoriaLeitoresDAO;
    CategoriaLivrosDAO categoriaLivrosDAO;
    AppDataBase database;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Inicializar variavéis */
        init();

        /* Inicializar banco de dados */
        database = Room.databaseBuilder(this, AppDataBase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        initDatabase();

        /* Configuração dos navbar e drawer */
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_acervo, R.id.navigation_emprestados)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        /* Evento ao selecionar um item do drawer (menu) */
        navDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_cadClientes:
                        startActivity(ClientesActivity.class);
                        break;
                    case R.id.menu_cadLivros:
                        startActivity(LivrosActivity.class);
                        break;
                    case R.id.menu_cadCategoriaLeitores:
                        startActivity(CategoriaLeitoresActivity.class);
                        break;
                    case R.id.menu_cadCategoriaLivros:
                        startActivity(CategoriaLivrosActivity.class);
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    /* Método para inicializar variaveis */
    public void init() {
        navView = findViewById(R.id.nav_view);
        navDrawer = findViewById(R.id.nav_drawer);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /* Método para inicializar o banco de dados */
    public void initDatabase() {
        clienteDAO = database.getClienteDAO();
        livrosDAO = database.getLivrosDAO();
        categoriaLeitoresDAO = database.getCategoriaLeitoresDAO();
        categoriaLivrosDAO = database.getCategoriaLivrosDAO();

        clienteDAO.getClientes();
        livrosDAO.getLivros();
        categoriaLeitoresDAO.getCategoriasLeitores();
        categoriaLivrosDAO.getCategoriaLivros();
    }

    /* Metódo para iniciar uma activity */
    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}

