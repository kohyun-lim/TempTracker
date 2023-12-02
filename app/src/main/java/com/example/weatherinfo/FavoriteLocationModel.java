package com.example.weatherinfo;

public class FavoriteLocationModel {

    private String location;
    private boolean isFavorite;

    public FavoriteLocationModel(String location, boolean isFavorite) {
        this.location = location;
        this.isFavorite = isFavorite;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}