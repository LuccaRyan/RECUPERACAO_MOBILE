package com.example.listadecomprasryan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class FragmentTempo extends Fragment {

    private TextView textViewTemperatura;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout do fragmento
        View view = inflater.inflate(R.layout.fragment_tempo, container, false);

        // Inicializa o TextView onde a temperatura será exibida
        textViewTemperatura = view.findViewById(R.id.temperatura);
        fetchTemperature();

        return view;
    }
    private void fetchTemperature() {
        String baseUrl = "https://api.openweathermap.org/";
        String apiKey = "c7b74e88a70b5eb5c1b77433892fee67"; // Sua chave da API
        String city = "São Paulo"; // Nome da cidade
        String units = "metric"; // Unidade (Celsius)

        WeatherApi weatherApi = RetrofitClient.getClient(baseUrl).create(WeatherApi.class);
        Call<WeatherResponse> call = weatherApi.getWeather(city, apiKey, units);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    float temperature = response.body().main.temp;
                    textViewTemperatura.setText("Temperatura: " + temperature + "°C");
                } else {
                    textViewTemperatura.setText("Erro ao obter temperatura");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textViewTemperatura.setText("Erro: " + t.getMessage());
            }
        });
    }
}
