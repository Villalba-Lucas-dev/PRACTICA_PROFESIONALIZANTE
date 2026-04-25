package com.example.tesis;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GymApiService {

    // Le indicamos que es un método POST y la ruta exacta del backend
    @POST("/api/auth/registro")
    Call<Void> registrarCliente(@Body Cliente nuevoCliente);

    @POST("/api/auth/login")
    Call<Void> loginCliente(@Body LoginRequest loginRequest);
}