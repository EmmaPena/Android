package com.example.servidorcaseroapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String URL_TUNEL = "https://mis-notas-api.onrender.com/";

    private EditText etNota;
    private ApiService apiService;
    private NotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNota = findViewById(R.id.etNota);
        Button btnEnviar = findViewById(R.id.btnEnviar);
        RecyclerView rvNotas = findViewById(R.id.rvNotas);

        // Configuración del RecyclerView
        rvNotas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotasAdapter();
        rvNotas.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_TUNEL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        btnEnviar.setOnClickListener(v -> {
            String texto = etNota.getText().toString().trim();
            if (!texto.isEmpty()) {
                guardarNotaEnLaptop(texto);
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.err_campo_vacio), Toast.LENGTH_SHORT).show();
            }
        });

        cargarNotas();
    }

    private void guardarNotaEnLaptop(String texto) {
        Nota nuevaNota = new Nota(texto);
        apiService.crearNota(nuevaNota).enqueue(new Callback<Nota>() {
            @Override
            public void onResponse(Call<Nota> call, Response<Nota> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, getString(R.string.msg_exito), Toast.LENGTH_SHORT).show();
                    etNota.setText("");
                    cargarNotas();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.err_http) + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Nota> call, Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.err_conexion) + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarNotas() {
        apiService.obtenerNotas().enqueue(new Callback<List<Nota>>() {
            @Override
            public void onResponse(Call<List<Nota>> call, Response<List<Nota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setNotas(response.body());
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.err_http) + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Nota>> call, Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.err_conexion) + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}