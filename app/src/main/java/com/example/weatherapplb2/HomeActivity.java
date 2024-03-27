package com.example.weatherapplb2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements WeatherDataFetcher.WeatherDataListener {

    private EditText locationEditText;
    private Button getRecommendationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Base_Theme_WeatherAppLB2);
        setContentView(R.layout.activity_home);

        locationEditText = findViewById(R.id.location_edit_text);
        getRecommendationsButton = findViewById(R.id.get_recommendations_button);

        getRecommendationsButton.setOnClickListener(v -> {
            String location = locationEditText.getText().toString();

            if (!location.isEmpty()) {
                WeatherDataFetcher weatherDataFetcher = new WeatherDataFetcher();
                weatherDataFetcher.fetchWeatherData(HomeActivity.this, location, HomeActivity.this);
            } else {
                Toast.makeText(HomeActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDataReceived(String weatherDetails) {
        Intent intent = new Intent(HomeActivity.this, WeatherActivity.class);
        startActivity(intent);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}
