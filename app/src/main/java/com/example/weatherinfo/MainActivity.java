package com.example.weatherinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String CITY;
    String API = "b0eafcdb33218071192f7471c1bdbb2f";
    Boolean isFavorite = false;
    ImageView search, ivFavoriteHeart;
    Button btnFavourite;
    EditText etCity;
    BottomNavigationView naviagtion;
    private List<FavoriteLocationModel> favoriteList;
    TextView city, country, time, temp, forecast, humidity, min_temp, max_temp, sunrises, sunsets, pressure, windSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent() != null){
            CITY = getIntent().getStringExtra("LOCATION");
        }
        {
            etCity = (EditText) findViewById(R.id.Your_city);
            search = (ImageView) findViewById(R.id.search);
            ivFavoriteHeart = (ImageView) findViewById(R.id.ivFavoriteHeart);
            naviagtion = findViewById(R.id.NavigationView);


            // CALL ALL ANSWERS :
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
            btnFavourite = (Button) findViewById(R.id.btnFavourite);

            // Load the list from session
            favoriteList = new ArrayList<>();
            favoriteList = SessionAndCookiesManager.getFavoriteLocationsList(this);

            if (!TextUtils.isEmpty(CITY)){
                etCity.setText(CITY);
                searchAndSetCityData();
            }

            // CLICK ON SEARCH BUTTON :
            search.setOnClickListener(v -> {
                searchAndSetCityData();
            });

            btnFavourite.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(CITY)) {
                    isFavorite = !isFavorite;
                    updateFavoriteUI();

                    // Add or remove the CITY from favorites in session
                    if (isFavorite) {
                        // Add CITY to favorites
                        FavoriteLocationModel newFavorite = new FavoriteLocationModel(CITY, true);
                        SessionAndCookiesManager.add(this, newFavorite);
                        showToast("Added to favorites: " + CITY);
                    } else {
                        // Remove CITY from favorites
                        FavoriteLocationModel cityToRemove = findFavoriteByCity(CITY);
                        if (cityToRemove != null) {
                            SessionAndCookiesManager.remove(this, cityToRemove, CITY);
                            showToast("Removed from favorites: " + CITY);
                        }
                    }
                } else {
                    showToast("Please first search location.");
                }
            });
        }

        naviagtion.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intentHome);
                        finish();
                        return true;
                    case R.id.action_search:
                        // 이미 현재 탭이 선택된 경우 무시
                        if (item.isChecked()) {
                            return false;
                        }
                        // 다른 동작 추가 가능
                        return true;
                    case R.id.action_favorite:
                        Intent intentFavorite = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(intentFavorite);
                        finish();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void searchAndSetCityData() {
        CITY = etCity.getText().toString().trim();
        if (!TextUtils.isEmpty(CITY)) {
            isFavorite = isCityInFavorites(CITY);
            updateFavoriteUI();
            new weatherTask().execute();
        } else {
            showToast("Please enter a location.");
        }
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

                Toast.makeText(MainActivity.this, "Error:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isCityInFavorites(String city) {
        for (FavoriteLocationModel favorite : favoriteList) {
            if (favorite.getLocation().equalsIgnoreCase(city.trim())) {
                return true;
            }
        }
        return false;
    }

    private void updateFavoriteUI() {
        if (isFavorite) {
            ivFavoriteHeart.setImageResource(R.drawable.favorite_selected);
        } else {
            ivFavoriteHeart.setImageResource(R.drawable.favourite_unselected);
        }
    }

    private FavoriteLocationModel findFavoriteByCity(String city) {
        for (FavoriteLocationModel favorite : favoriteList) {
            if (favorite.getLocation().equalsIgnoreCase(city)) {
                return favorite;
            }
        }
        return null;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
