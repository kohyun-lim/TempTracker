package com.example.weatherinfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androdocs.httprequest.HttpRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    String CITY;
    String API = "b0eafcdb33218071192f7471c1bdbb2f";
    EditText etCity;
    BottomNavigationView naviagtion;
    TextView city, country, time, temp, forecast, humidity, min_temp, max_temp, sunrises, sunsets, pressure, windSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        {
            naviagtion = findViewById(R.id.NavigationView);
            etCity = (EditText) findViewById(R.id.Your_city);

            city = (TextView) findViewById(R.id.city);
            country = (TextView) findViewById(R.id.country);
            time = (TextView) findViewById(R.id.time);
            temp = (TextView) findViewById(R.id.temp);
            forecast = (TextView) findViewById(R.id.forecast);
            humidity = (TextView) findViewById(R.id.humidity);
            min_temp = (TextView) findViewById(R.id.min_temp);
            max_temp = (TextView) findViewById(R.id.max_temp);
            sunrises = (TextView) findViewById(R.id.sunrises);
            sunsets = (TextView) findViewById(R.id.sunsets);
            pressure = (TextView) findViewById(R.id.pressure);
            windSpeed = (TextView) findViewById(R.id.wind_speed);

        }
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Location permissions are granted, proceed with location retrieval
            getLocationAndWeather();
        }

        naviagtion.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // 이미 현재 탭이 선택된 경우 무시
                        if (item.isChecked()) {
                            return false;
                        }
                        // 다른 동작 추가 가능
                        return true;
                    case R.id.action_search:
                        Intent intentSearch = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intentSearch);
                        finish(); // Optional: finish current activity after starting new one
                        return true;
                    case R.id.action_favorite:
                        Intent intentFavorite = new Intent(HomeActivity.this, FavoriteActivity.class);
                        startActivity(intentFavorite);
                        finish();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    class weatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject sys = jsonObj.getJSONObject("sys");


                // CALL VALUE IN API :
                String city_name = jsonObj.getString("name");
                String countryname = sys.getString("country");
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Last Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temperature = main.getString("temp");
                String cast = weather.getString("description");
                String humi_dity = main.getString("humidity");
                String temp_min = main.getString("temp_min");
                String temp_max = main.getString("temp_max");
                String pre = main.getString("pressure");
                String windspeed = wind.getString("speed");
                Long rise = sys.getLong("sunrise");
                String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
                Long set = sys.getLong("sunset");
                String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));


                // SET ALL VALUES IN TEXTBOX :
                city.setText(city_name);
                country.setText(countryname);
                time.setText(updatedAtText);
                temp.setText(temperature + "°C");
                forecast.setText(cast);
                humidity.setText(humi_dity);
                min_temp.setText(temp_min);
                max_temp.setText(temp_max);
                sunrises.setText(sunrise);
                sunsets.setText(sunset);
                pressure.setText(pre);
                windSpeed.setText(windspeed);

            } catch (Exception e) {

                Toast.makeText(HomeActivity.this, "Error:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed with location retrieval
                getLocationAndWeather();
            } else {
                Toast.makeText(this, "Location permission denied. Weather information may not be accurate.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLocationAndWeather() {
        // Use LocationManager to get the user's location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check if the app has the necessary location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Get the user's current latitude and longitude
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Now you can use these coordinates to get the weather information
                    // You may use reverse geocoding to get the city name based on the coordinates
                    // For simplicity, let's use a placeholder city
                    CITY = "vancouver";
                    new weatherTask().execute();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                    // Handle the case where the location provider is disabled
                    Toast.makeText(HomeActivity.this, "Location provider is disabled. Weather information may not be accurate.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Handle the case where location permissions are not granted
            Toast.makeText(this, "Location permission is not granted. Weather information may not be accurate.", Toast.LENGTH_SHORT).show();
        }
    }


}