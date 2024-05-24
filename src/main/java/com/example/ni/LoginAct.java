package com.example.ni;

import android.content.Intent;
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

public class LoginAct extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        client = new OkHttpClient();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginUser(username, password);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAct.this, Cadastro.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser(String username, String password) {
        try {
            JSONObject json = new JSONObject();
            json.put("dsc_nome_user", username);
            json.put("senha", password);

            Log.d("Login", "JSON: " + json.toString());

            RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json.toString());
            Request request = new Request.Builder()
                    .url("https://fxxsfw-3001.csb.app/login")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.e("Login", "Erro no login: " + e.getMessage());
                    runOnUiThread(() -> Toast.makeText(LoginAct.this, "Erro no login: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        Log.d("Login", "Login bem-sucedido! Resposta: " + responseBody);
                        runOnUiThread(() -> Toast.makeText(LoginAct.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show());
                        Intent intent = new Intent(LoginAct.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("Login", "Credenciais inválidas: " + responseBody);
                        runOnUiThread(() -> Toast.makeText(LoginAct.this, "Credenciais inválidas: " + responseBody, Toast.LENGTH_SHORT).show());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Login", "Erro ao criar JSON: " + e.getMessage());
            Toast.makeText(this, "Erro ao criar JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }}