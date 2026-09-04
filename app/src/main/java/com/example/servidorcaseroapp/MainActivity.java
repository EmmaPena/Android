package com.example.servidorcaseroapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String URL_TUNEL = "https://mis-notas-api.onrender.com/";

    private EditText etNota;
    private TextView tvResultado;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNota = findViewById(R.id.etNota);
        tvResultado = findViewById(R.id.tvResultado);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_TUNEL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        btnEnviar.setOnClickListener(v -> {
            String texto = etNota.getText().toString().trim();
            if (!texto.isEmpty()) {
                guardarNotaEnLaptop(texto);
            }
        });

        // Cargar notas guardadas al abrir la app
        cargarNotas();
    }

    private void guardarNotaEnLaptop(String texto) {
        Nota nuevaNota = new Nota(texto);
        apiService.crearNota(nuevaNota).enqueue(new Callback<Nota>() {
            @Override
            public void onResponse(Call<Nota> call, Response<Nota> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "¡Nota guardada!", Toast.LENGTH_SHORT).show();
                    etNota.setText("");
                    cargarNotas();
                } else {
                    tvResultado.setText("Error HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Nota> call, Throwable t) {
                tvResultado.setText("Error al conectar: " + t.getMessage());
            }
        });
    }

    private void cargarNotas() {
        apiService.obtenerNotas().enqueue(new Callback<List<Nota>>() {
            @Override
            public void onResponse(Call<List<Nota>> call, Response<List<Nota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder builder = new StringBuilder("NOTAS GUARDADAS:\n\n");
                    for (Nota n : response.body()) {
                        builder.append("• ").append(n.getTexto()).append("\n");
                    }
                    tvResultado.setText(builder.toString());
                } else {
                    tvResultado.setText("Error al cargar: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Nota>> call, Throwable t) {
                tvResultado.setText("Error de conexión: " + t.getMessage());
            }
        });
    }
}
