package com.example.ni;

import androidx.appcompat.app.AppCompatActivity;
//importações banco externo
import java.io.IOException;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class basico extends AppCompatActivity {
    private TextView textViewPergunta, Acertos, TextsobB;
    private LinearLayout frameLayout, LayoutBasico;
    private RadioGroup radioGroupRespostas;
    private Button buttonProximaPergunta, BtnBasico;
    private JSONArray perguntasArray;
    private int indicePerguntaAtual = 0;
    private int numeroAcertos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_basico);
        textViewPergunta = findViewById(R.id.textViewPergunta);
        radioGroupRespostas = findViewById(R.id.radioGroupRespostas);
        buttonProximaPergunta = findViewById(R.id.buttonProximaPergunta);
        Acertos = findViewById(R.id.Acertos);
        TextsobB = findViewById(R.id.TextsobB);
        buttonProximaPergunta.setOnClickListener(view -> {
            verificarResposta();
            exibirProximaPergunta();
        });
        Button btnVoltarParaMain = findViewById(R.id.btnVoltarParaMain);
        btnVoltarParaMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(basico.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buttonProximaPergunta.setEnabled(false);
        radioGroupRespostas.setOnCheckedChangeListener((group, checkedId) -> buttonProximaPergunta.setEnabled(true));

        try {
            String jsonContent = loadJSONFromAsset(this, "Perguntas.json");
            JSONObject jsonObject = new JSONObject(jsonContent);
            perguntasArray = jsonObject.getJSONArray("PerguntasB");

            exibirPerguntaAtual();
        } catch (Exception e) {
            e.printStackTrace();
        }


// Dentro do método verificarResposta(), após incrementar o número de acertos
// Atualiza a exibição do número de acertos

        try {
            // Carrega o conteúdo do arquivo JSON da pasta assets
            String jsonContent = loadJSONFromAsset(this, "Perguntas.json");

            // Converte a string JSON em um JSONObject
            JSONObject jsonObject = new JSONObject(jsonContent);

            // Obtem o array de perguntas do JSONObject
            JSONArray perguntasArray = jsonObject.getJSONArray("Perguntas");

            // Itera sobre as perguntas no JSONArray
            for (int i = 0; i < perguntasArray.length(); i++) {
                JSONObject perguntaObj = perguntasArray.getJSONObject(i);

                // Extrai os campos da pergunta
                String pergunta = perguntaObj.getString("pergunta");
                JSONArray opcoesResposta = perguntaObj.getJSONArray("opcoes_resposta");
                String respostaCorreta = perguntaObj.getString("resposta_correta");
                String explicacao = perguntaObj.getString("explicacao");
                String nivelDificuldade = perguntaObj.getString("nivel_dificuldade");

                // Exibe os campos da pergunta
                Log.d("Pergunta", pergunta);
                Log.d("Opções de Resposta", opcoesResposta.toString());
                Log.d("Resposta Correta", respostaCorreta);
                Log.d("Explicação", explicacao);
                Log.d("Nível de Dificuldade", nivelDificuldade);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset(Context context, String filename) {
        String json;
        try {
            // Lê o arquivo JSON da pasta assets
            InputStream inputStream = context.getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public void exibirPerguntaAtual() {
        try {
            JSONObject perguntaObj = perguntasArray.getJSONObject(indicePerguntaAtual);
            String pergunta = perguntaObj.getString("pergunta");
            JSONArray opcoesResposta = perguntaObj.getJSONArray("opcoes_resposta");

            textViewPergunta.setText(pergunta);

            radioGroupRespostas.removeAllViews();
            for (int i = 0; i < opcoesResposta.length(); i++) {
                String opcao = opcoesResposta.getString(i);
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(opcao);
                radioGroupRespostas.addView(radioButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verificarResposta() {
        try {
            JSONObject perguntaObj = perguntasArray.getJSONObject(indicePerguntaAtual);
            String respostaCorreta = perguntaObj.getString("resposta_correta");
            int respostaCorretaIndex = Integer.parseInt(respostaCorreta) - 1; // Convertendo para índice do array

            int radioButtonID = radioGroupRespostas.getCheckedRadioButtonId();
            View radioButton = radioGroupRespostas.findViewById(radioButtonID);
            int selectedOptionIndex = radioGroupRespostas.indexOfChild(radioButton);

            if (selectedOptionIndex == respostaCorretaIndex) {
                numeroAcertos++;
            }

            Acertos.setText("Acertos: " + numeroAcertos + "/" + (perguntasArray.length() - 1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void exibirProximaPergunta() {
        indicePerguntaAtual++;
        Acertos.setVisibility(View.VISIBLE);
        buttonProximaPergunta.setText("Proxima Pergunta");

        if (indicePerguntaAtual < perguntasArray.length()) {
            exibirPerguntaAtual();
        } else {
            textViewPergunta.setText("Você respondeu todas as perguntas por Agora, mais perguntas logo mais!");
            radioGroupRespostas.setVisibility(View.INVISIBLE);
            TextsobB.setVisibility(View.INVISIBLE);
            buttonProximaPergunta.setVisibility(View.INVISIBLE);

        }
    }

}
