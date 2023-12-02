package com.example.weatherinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private List<FavoriteLocationModel> favoriteList;
    private ItemAdapter itemAdapter;
    RecyclerView recyclerView;

    BottomNavigationView naviagtion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        naviagtion = findViewById(R.id.NavigationView);
        naviagtion.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent intentHome = new Intent(FavoriteActivity.this, HomeActivity.class);
                        startActivity(intentHome);
                        finish();
                        return true;
                    case R.id.action_search:
                        Intent intentSearch = new Intent(FavoriteActivity.this, MainActivity.class);
                        startActivity(intentSearch);
                        finish();
                        return true;
                    case R.id.action_favorite:
                        return true;
                    default:
                        return true;
                }
            }
        });


        // Assuming you have a RecyclerView with the id "recyclerView" in your activity_favorite layout
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        // Load the list from session
        favoriteList = new ArrayList<>();
        favoriteList = SessionAndCookiesManager.getFavoriteLocationsList(this);

        // Create an adapter and set it to the RecyclerView
        itemAdapter = new ItemAdapter(this, favoriteList, new ItemClickListener() {
            @Override
            public void onItemClick(int position, String location) {
                // Handle item click (e.g., remove the item)
                removeItem(position, location);
            }
        });
        recyclerView.setAdapter(itemAdapter);
    }

    private void removeItem(int position, String location) {
        if (position >= 0 && position < favoriteList.size()) {
            FavoriteLocationModel removedItem = favoriteList.get(position);
            SessionAndCookiesManager.remove(this, removedItem, location);

            // Remove the item from the list and update the adapter
            favoriteList.remove(position);
            itemAdapter.notifyItemRemoved(position);
            itemAdapter.notifyItemRangeChanged(position, favoriteList.size());

            Toast.makeText(this, "Removed from favorites: " + removedItem.getLocation(), Toast.LENGTH_SHORT).show();
        }
    }
}