package com.example.letrongtin.mywallpaper.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.example.letrongtin.mywallpaper.R;

public class FavoriteViewHolder extends RecentViewHolder {

    public ImageView wallpaper;

    public FavoriteViewHolder(View itemView) {
        super(itemView);
        wallpaper = itemView.findViewById(R.id.image);
    }
}
