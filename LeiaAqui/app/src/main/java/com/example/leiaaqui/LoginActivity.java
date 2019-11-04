package com.example.leiaaqui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText usuario;
    TextInputEditText senha;
    TextInputLayout usuario_layout;
    TextInputLayout senha_layout;
    Button logar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        /* Evento ao clicar no botão logar */
        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });
    }

    public void init() {
        usuario = (TextInputEditText) findViewById(R.id.et_login);
        senha = (TextInputEditText) findViewById(R.id.et_senha);
        logar = (Button) findViewById(R.id.btn_entrar);
        usuario_layout = (TextInputLayout) findViewById(R.id.login_layout);
        senha_layout = (TextInputLayout) findViewById(R.id.senha_layout);
    }

    /* Método para validar os campos */
    public void validarCampos() {
        if(usuario.getText().length() == 0) {
            usuario_layout.setError("Você inserir um usuário.");
        } else if (senha.getText().length() == 0) {
            senha_layout.setError("Você precisa inserir uma senha.");
        } else {
            startActivity(MainActivity.class);
        }
    }

    /* Método para iniciar uma activity */
    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
