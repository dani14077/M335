package com.example.weatherapplb2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherDataFetcher {

    private static final String API_KEY = "a996f11c487ccc58fac63f8ac4233682"; // Replace with your OpenWeatherMap API key

    public void fetchWeatherData(Context context, String location, WeatherDataListener listener) {
        new FetchWeatherDataTask(context, listener).execute(location);
    }

    public interface WeatherDataListener {
        void onDataReceived(String weatherDetails);
        void onError(String message);
    }

    private class FetchWeatherDataTask extends AsyncTask<String, Void, String> {

        private Context context;
        private WeatherDataListener listener;

        public FetchWeatherDataTask(Context context, WeatherDataListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String location = params[0];
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + API_KEY;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    String temperature = String.valueOf(jsonObject.getJSONObject("main").getDouble("temp") - 273.15); // Convert Kelvin to Celsius
                    String weatherDetails = "Weather details for " + jsonObject.getString("name") + ": " + description + ", " + temperature + "°C";

                    // Store data in SharedPreferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences("WeatherData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("WeatherDetails", weatherDetails);
                    editor.apply();

                    listener.onDataReceived(weatherDetails);

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError("Error parsing weather data");
                }
            } else {
                listener.onError("Error fetching weather data");
            }
        }
    }
}
