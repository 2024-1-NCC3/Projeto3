package com.example.ni;

import androidx.appcompat.app.AppCompatActivity;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class basicotxt extends AppCompatActivity {

    private TextView tituloTextView,Textotitf;
    private TextView textoTextView;
    private int indicetxtAtual=0;
    private JSONArray txtArray;
    private TextView exemploTextView;
    private Button changeContentBtn,BtnPerguntas;
    private JSONArray txtBasicoArray;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicotxt);
        tituloTextView = findViewById(R.id.tituloTextView);
        textoTextView = findViewById(R.id.textoTextView);
        exemploTextView = findViewById(R.id.exemploTextView);
        changeContentBtn = findViewById(R.id.changeContentButton);
        BtnPerguntas=findViewById(R.id.BtnPerguntas);
        BtnPerguntas.setEnabled(false);
        Textotitf=findViewById(R.id.Textotitf);
        changeContentBtn.setOnClickListener(view -> {
            exibirProximoTexto();
            Textotitf.setVisibility(View.GONE);
        });

        BtnPerguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(basicotxt.this, basico.class);
                startActivity(intent);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://7gng9t-3000.csb.app/tudo");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                    String response = scanner.hasNext() ? scanner.next() : "";

                    // Parse JSON response
                    JSONObject jsonObject = new JSONObject(response);
                    txtArray = jsonObject.getJSONArray("TxtBasico");

                    // Update UI on the main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            exibirtxtAtual();
                        }
                    });

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String loadJSONFromAsset(Context context, String filename) {
        String json;
        try {

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

        // Button click listener

    public void exibirtxtAtual() {
        try {
            JSONObject txtObj = txtArray.getJSONObject(indicetxtAtual);
            String Texto = txtObj.getString("Texto");
            String Titulo = txtObj.getString("Titulo");
            String exemplo = txtObj.getString("exemplo");

            textoTextView.setText(Texto);
            tituloTextView.setText(Titulo);
            exemploTextView.setText(exemplo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void exibirProximoTexto() {
        indicetxtAtual++;
        changeContentBtn.setText("Proxima Pergunta");

        if (indicetxtAtual < txtArray.length()) {
            exibirtxtAtual();
        } else {
            tituloTextView.setText("Você leu todo o mateial nesscessário, vá para as perguntas!");
            textoTextView.setVisibility(View.INVISIBLE);
            exemploTextView.setVisibility(View.INVISIBLE);
            changeContentBtn.setVisibility(View.INVISIBLE);
            BtnPerguntas.setVisibility(View.VISIBLE);
            BtnPerguntas.setEnabled(true);

        }
    }
}