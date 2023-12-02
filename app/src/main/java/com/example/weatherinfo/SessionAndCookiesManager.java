package com.example.weatherinfo;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SessionAndCookiesManager {

    private static final String SESSION_AND_COOKIES = "temp_tracker_session";

    public static void saveFavoriteLocationsList(Context context, List<FavoriteLocationModel> itemList) {
        SharedPreferences prefs = context.getSharedPreferences(SESSION_AND_COOKIES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(itemList);
        editor.putString("favorite_list", json);
        editor.apply();
    }

    public static List<FavoriteLocationModel> getFavoriteLocationsList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SESSION_AND_COOKIES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("favorite_list", null);
        Type type = new TypeToken<List<FavoriteLocationModel>>() {}.getType();
        return gson.fromJson(json, type) != null ? gson.fromJson(json, type) : emptyList();
    }

    public static void add(Context context, FavoriteLocationModel item) {
        List<FavoriteLocationModel> itemList = getFavoriteLocationsList(context);
        itemList.add(item);
        saveFavoriteLocationsList(context, itemList);
    }

    public static void remove(Context context, FavoriteLocationModel item, String location) {
        List<FavoriteLocationModel> itemList = getFavoriteLocationsList(context);
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getLocation().equals(location)) {
                itemList.remove(i);
                saveFavoriteLocationsList(context, itemList);
                return;
            }
        }
    }

    private static List<FavoriteLocationModel> emptyList() {
        return new ArrayList<>();
    }
}