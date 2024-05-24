package com.example.ni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class txtCss extends AppCompatActivity {
    private TextView tituloTextView, textoTextView, exemploTextView;
    private Button changeContentBtn, BtnPerguntas;
    private JSONArray txtArray;
    private int indicetxtAtual = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_css);
        tituloTextView = findViewById(R.id.tituloTextViewc);
        textoTextView = findViewById(R.id.textoTextViewc);
        exemploTextView = findViewById(R.id.exemploTextViewc);
        changeContentBtn = findViewById(R.id.changeContentButtonc);
        BtnPerguntas = findViewById(R.id.BtnPerguntasc);
        BtnPerguntas.setEnabled(false);

        changeContentBtn.setOnClickListener(view -> {
            exibirTextoAtual();
        });


        BtnPerguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(txtCss.this, basico.class);
                startActivity(intent);
            }
        });

        fetchTextosFromServer();
    }

    private void fetchTextosFromServer() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://fxxsfw-3001.csb.app/selecao-conteudo-3";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Log.e("Erro", "Falha na conexão com o servidor");
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        txtArray = jsonArray;

                        // Atualize a UI na thread principal
                        runOnUiThread(() -> {
                            exibirTextoAtual();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void exibirTextoAtual() {
        try {
            JSONObject txtObj = txtArray.getJSONObject(indicetxtAtual);
            String Texto = txtObj.getString("dsc_texto");
            String Titulo = txtObj.getString("dsc_titulo");

            textoTextView.setText(Texto);
            tituloTextView.setText(Titulo);
            indicetxtAtual++;
            if (indicetxtAtual>=10){
                BtnPerguntas.setVisibility(View.VISIBLE);
                BtnPerguntas.setEnabled(true);
                changeContentBtn.setVisibility(View.INVISIBLE);
                textoTextView.setVisibility(View.INVISIBLE);
                tituloTextView.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
