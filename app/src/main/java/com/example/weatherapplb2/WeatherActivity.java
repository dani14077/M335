package com.example.weatherapplb2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WeatherActivity extends AppCompatActivity {

    private TextView weatherDetailsTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherDetailsTextView = findViewById(R.id.weather_details_text_view);
        backButton = findViewById(R.id.back_button);

        SharedPreferences sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE);
        String weatherDetails = sharedPreferences.getString("WeatherDetails", "Weather details not available");

        if (!weatherDetails.equals("Weather details not available")) {
            String recommendation = getRecommendation(weatherDetails);

            weatherDetailsTextView.setText("Weather Details: " + weatherDetails + "\n\nRecommendation: " + recommendation);

            showNotification(recommendation);
        } else {
            weatherDetailsTextView.setText(weatherDetails);
        }

        // Set onClickListener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private String getRecommendation(String weatherDetails) {
        // Extract temperature and condition from weatherDetails
        String[] parts = weatherDetails.split(", ");
        if (parts.length < 2) {
            return "Unable to get weather recommendation. Parts: " + parts.length + ", Weather details for Zurich: " + weatherDetails;
        }

        System.out.println("Weather details: " + weatherDetails);  // Debug print

        String temperatureString = parts[1].replace("°C", "").trim();
        try {
            double temperature = Double.parseDouble(temperatureString);
            String condition = parts[0].trim();

            // Determine recommendation based on temperature and condition
            if (temperature < 5) {
                return "It's cold! Wear a warm jacket and bring an umbrella.";
            } else if (temperature < 15) {
                return "It's cool. Bring a light jacket or sweater.";
            } else {
                if (condition.contains("Rain") || condition.contains("Drizzle")) {
                    return "It might rain. Bring an umbrella.";
                } else if (condition.contains("Snow")) {
                    return "It might snow. Wear warm clothes and bring gloves.";
                } else {
                    return "It's warm and clear. Enjoy the weather!";
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Unable to get weather recommendation. Temperature: " + temperatureString + ", Weather details for Zurich: " + weatherDetails;
        }
    }


    private void showNotification(String message) {
        String channelId = "WeatherChannel";
        CharSequence channelName = "Weather Channel";

        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Daily Recommendations")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(0, builder.build());
    }
}
