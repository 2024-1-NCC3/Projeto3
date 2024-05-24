package com.example.ni;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Cadastro extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;

    private OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);

        client = new OkHttpClient();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                registerUser(username, password);
            }
        });
    }

    private void registerUser(String username, String password) {
        try {
            JSONObject json = new JSONObject();
            json.put("dsc_nome_user", username);
            json.put("senha", password);

            Log.d("Cadastro", "JSON: " + json.toString());

            RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json.toString());
            Request request = new Request.Builder()
                    .url("https://fxxsfw-3001.csb.app/cadastro") // Certifique-se que esta URL está correta
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.e("Cadastro", "Erro no cadastro: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(Cadastro.this, "Erro no cadastro: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        Log.d("Cadastro", "Usuário cadastrado com sucesso! Resposta: " + responseBody);
                        runOnUiThread(() -> Toast.makeText(Cadastro.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show());
                    } else {
                        Log.e("Cadastro", "Erro no cadastro: " + responseBody);
                        runOnUiThread(() -> Toast.makeText(Cadastro.this, "Erro no cadastro: " + responseBody, Toast.LENGTH_SHORT).show());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Cadastro", "Erro ao criar JSON: " + e.getMessage());
            Toast.makeText(this, "Erro ao criar JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

