package com.example.leiaaqui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DevolucaoRealizadaActivity extends AppCompatActivity {

    TextView tv_realizado;
    Button btn_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucao_realizada);

        /* Inicializar variavéis */
        init();

        /* Envento ao clicar no botão done */
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Recebo um intent extra com a activity que realizou o cadastro */
                //Bundle extras = getIntent().getExtras();
                //if(extras != null) {
                //    Class activity = (Class<Activity>)extras.getSerializable("activity");
                //    startActivity(activity);
                startActivity(MainActivity.class);
            }
        });
    }

    /* Método para inicializar variaveis */
    public void init() {
        tv_realizado = (TextView) findViewById(R.id.tv_devolucao_realizada);
        btn_done = (Button) findViewById(R.id.btn_devolucao_realizada);
    }

    /* Método ao apertar o botão nativo voltar */
    public void onBackPressed() {
        startActivity(MainActivity.class);
        return;
    }

    /* Metódo para iniciar uma activity */
    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}