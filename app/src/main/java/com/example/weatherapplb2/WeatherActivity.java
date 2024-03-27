package com.example.weatherapplb2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
            weatherDetailsTextView.setText(weatherDetails);

            showNotification(weatherDetails);
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
