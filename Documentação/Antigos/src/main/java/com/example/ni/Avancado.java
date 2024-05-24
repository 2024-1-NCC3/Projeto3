package com.example.ni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Avancado extends AppCompatActivity {
    private TextView textViewPergunta, Acertos, TextsobB,TXTpnd;
    private LinearLayout frameLayout, LayoutBasico;
    private RadioGroup radioGroupRespostas;
    private Button buttonProximaPergunta, BtnBasico;
    private JSONArray perguntasArray;
    private int indicePerguntaAtual = 0;
    private int numeroAcertos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avancado);

        textViewPergunta = findViewById(R.id.textViewPerguntaA);
        TXTpnd= findViewById(R.id.TXTpndA);
        radioGroupRespostas = findViewById(R.id.radioGroupRespostasA);
        buttonProximaPergunta = findViewById(R.id.buttonProximaPerguntaA);
        Acertos = findViewById(R.id.AcertosA);
        TextsobB = findViewById(R.id.TextsobA);


        buttonProximaPergunta.setOnClickListener(view -> {
            verificarResposta();
            exibirProximaPergunta();
        });

        Button btnVoltarParaMain = findViewById(R.id.btnVoltarParaMainA);
        btnVoltarParaMain.setOnClickListener(v -> {
            Intent intent = new Intent(Avancado.this, MainActivity.class);
            startActivity(intent);
        });

        buttonProximaPergunta.setEnabled(false);
        radioGroupRespostas.setOnCheckedChangeListener((group, checkedId) -> buttonProximaPergunta.setEnabled(true));

        // Buscar perguntas do servidor
        fetchPerguntasFromServer();
    }

    private void fetchPerguntasFromServer() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://fxxsfw-3001.csb.app/selecao-conteudo-3";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Log.e("Erro", "Falha na conexão com o servidor"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        runOnUiThread(() -> {
                            perguntasArray = jsonArray;
                            exibirPerguntaAtual();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void exibirPerguntaAtual() {
        try {
            JSONObject perguntaObj = perguntasArray.getJSONObject(indicePerguntaAtual);
            String pergunta = perguntaObj.getString("dsc_pergunta");
            JSONArray opcoesResposta = new JSONArray();
            opcoesResposta.put(perguntaObj.getString("alternativa_a"));
            opcoesResposta.put(perguntaObj.getString("alternativa_b"));
            opcoesResposta.put(perguntaObj.getString("alternativa_c"));
            opcoesResposta.put(perguntaObj.getString("alternativa_d"));

            textViewPergunta.setText(pergunta);

            radioGroupRespostas.removeAllViews();
            for (int i = 0; i < opcoesResposta.length(); i++) {
                String opcao = opcoesResposta.getString(i);
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(opcao);
                radioGroupRespostas.addView(radioButton);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void verificarResposta() {
        try {
            JSONObject perguntaObj = perguntasArray.getJSONObject(indicePerguntaAtual);
            String respostaCorreta = perguntaObj.getString("resposta_correta");

            int radioButtonID = radioGroupRespostas.getCheckedRadioButtonId();
            View radioButton = radioGroupRespostas.findViewById(radioButtonID);
            int selectedOptionIndex = radioGroupRespostas.indexOfChild(radioButton);

            // Convert respostaCorreta to index
            int respostaCorretaIndex = getRespostaCorretaIndex(respostaCorreta);

            if (selectedOptionIndex == respostaCorretaIndex) {
                numeroAcertos++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Acertos.setText("Acertos: " + numeroAcertos);
    }

    private int getRespostaCorretaIndex(String respostaCorreta) {
        switch (respostaCorreta) {
            case "a":
                return 0;
            case "b":
                return 1;
            case "c":
                return 2;
            case "d":
                return 3;
            default:
                return -1;
        }
    }

    private void exibirProximaPergunta() {
        if (indicePerguntaAtual <= perguntasArray.length()) {
            indicePerguntaAtual++;
            TXTpnd.setVisibility(View.INVISIBLE);
            TextsobB.setVisibility(View.INVISIBLE);
            if(indicePerguntaAtual>0){
                exibirPerguntaAtual();}
        } else {
            // Fim das perguntas
            buttonProximaPergunta.setEnabled(false);
            buttonProximaPergunta.setVisibility(View.INVISIBLE);
            textViewPergunta.setText("Fim das perguntas. Você acertou " + numeroAcertos + " de " + perguntasArray.length());
        }
        buttonProximaPergunta.setEnabled(false);
    }
}