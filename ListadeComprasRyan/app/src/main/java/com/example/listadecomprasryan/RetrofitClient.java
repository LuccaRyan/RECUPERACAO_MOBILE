package com.example.listadecomprasryan;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl) // URL base para as requisições
                    .addConverterFactory(GsonConverterFactory.create()) // Converte JSON em objetos Java
                    .build();
        }
        return retrofit;
    }
}
