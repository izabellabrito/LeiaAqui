package com.example.leiaaqui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EdicaoRealizadaActivity extends AppCompatActivity {

    TextView tv_realizado;
    Button btn_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao_realizada);

        /* Inicializar variavéis */
        init();

        /* Envento ao clicar no botão done */
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.class);
            }
        });
    }

    /* Método para inicializar variaveis */
    public void init() {
        tv_realizado = (TextView) findViewById(R.id.tv_edicao_realizado);
        btn_done = (Button) findViewById(R.id.btn_edicao_realizada);
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
