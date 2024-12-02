package com.example.listadecomprasryan;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(
            @Query("q") String city,        // Nome da cidade
            @Query("appid") String apiKey, // Sua chave da API
            @Query("units") String units   // Unidade (metric para Celsius)
    );
}