package com.example.leiaaqui;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashSreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override public void run() {
                startActivity(LoginActivity.class);
            }
        }, 2000);
    }

    /* Metódo para iniciar uma activity */
    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
