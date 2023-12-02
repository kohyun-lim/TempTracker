package com.example.weatherinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final List<FavoriteLocationModel> itemList;
    private final ItemClickListener itemClickListener;
    Activity context;

    public ItemAdapter(Activity context, List<FavoriteLocationModel> itemList, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemList = itemList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FavoriteLocationModel item = itemList.get(position);
        holder.tvLocation.setText(item.getLocation());
        holder.ivFavoriteHeart.setImageResource(R.drawable.favorite_selected);

        holder.btnFavorite.setOnClickListener(v -> {
            holder.ivFavoriteHeart.setImageResource(R.drawable.favourite_unselected);
            itemClickListener.onItemClick(position, item.getLocation());
        });
        // Handle item click to open the main activity
        holder.rlLocation.setOnClickListener(v -> {
            // Pass location to the next activity
            String location = item.getLocation();
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("LOCATION", location);
            context.startActivity(intent);
            context.finish();
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation;
        Button btnFavorite;
        ImageView ivFavoriteHeart;
        RelativeLayout rlLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            ivFavoriteHeart = itemView.findViewById(R.id.ivFavoriteHeart);
            rlLocation = itemView.findViewById(R.id.rlLocation);
        }
    }
}

