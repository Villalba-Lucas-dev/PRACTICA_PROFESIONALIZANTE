package com.example.tesis;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // Si usás tu celular físico conectado por cable, acá va la IP de tu PC (ej: 192.168.0.15)
    // Si usás el emulador de Android Studio, dejá 10.0.2.2
    private static final String BASE_URL = "http://10.0.2.2:8080";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Traduce a JSON
                    .build();
        }
        return retrofit;
    }
}