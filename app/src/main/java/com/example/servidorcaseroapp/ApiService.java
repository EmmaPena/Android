package com.example.servidorcaseroapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("notas")
    Call<List<Nota>> obtenerNotas();

    @POST("notas")
    Call<Nota> crearNota(@Body Nota nota);
}
