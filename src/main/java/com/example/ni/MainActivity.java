package com.example.ni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button btnIrParaBasico = findViewById(R.id.btnIrParaBasico);
        Button BtnInter = findViewById(R.id.BtnInter);
        Button BtnAvan = findViewById(R.id.BtnAvan);
        Button BtnCss = findViewById(R.id.BtnCss);
        btnIrParaBasico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, basicotxt.class);
                startActivity(intent);
            }
        });
        BtnInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Intermediariotxt.class);
                startActivity(intent);
            }
        });
        BtnAvan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Avancadotxt.class);
                startActivity(intent);
            }
        });
        BtnCss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, txtCss.class);
                startActivity(intent);
            }
        });

    }


}