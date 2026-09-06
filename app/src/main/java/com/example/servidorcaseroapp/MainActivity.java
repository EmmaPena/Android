package com.example.servidorcaseroapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private static final long INTERVALO_POLLING = 3000;

    private EditText etNota;
    private SearchView svBuscar;
    private ApiService apiService;
    private NotasAdapter adapter;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable pollRunnable = new Runnable() {
        @Override
        public void run() {
            // Solo hace polling si el usuario no está buscando activamente
            if (svBuscar != null && svBuscar.getQuery().toString().isEmpty()) {
                cargarNotas();
            }
            handler.postDelayed(this, INTERVALO_POLLING);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNota = findViewById(R.id.etNota);
        svBuscar = findViewById(R.id.svBuscar);
        Button btnEnviar = findViewById(R.id.btnEnviar);
        RecyclerView rvNotas = findViewById(R.id.rvNotas);

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

        // Evento de búsqueda en tiempo real
        svBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filtrar(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filtrar(newText);
                return false;
            }
        });

        // Configuración Swipe-to-Delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Nota notaABorrar = adapter.getNotaEn(position);
                eliminarNotaDelBackend(notaABorrar.getId());
            }
        }).attachToRecyclerView(rvNotas);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(pollRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(pollRunnable);
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
                }
            }

            @Override
            public void onFailure(Call<List<Nota>> call, Throwable t) {
            }
        });
    }

    private void eliminarNotaDelBackend(int id) {
        apiService.eliminarNota(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Nota eliminada", Toast.LENGTH_SHORT).show();
                    cargarNotas();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.err_http) + response.code(), Toast.LENGTH_SHORT).show();
                    cargarNotas();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.err_conexion) + t.getMessage(), Toast.LENGTH_SHORT).show();
                cargarNotas();
            }
        });
    }
}