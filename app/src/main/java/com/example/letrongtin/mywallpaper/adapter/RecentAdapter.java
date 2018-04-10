package com.example.letrongtin.mywallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letrongtin.mywallpaper.Interface.ItemClickListener;
import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.activity.WallpaperDetail;
import com.example.letrongtin.mywallpaper.common.Common;
import com.example.letrongtin.mywallpaper.database.Recents;
import com.example.letrongtin.mywallpaper.model.Wallpaper;
import com.example.letrongtin.mywallpaper.viewholder.ListWallpaperViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Le Trong Tin on 3/23/2018.
 */

public class RecentAdapter extends RecyclerView.Adapter<ListWallpaperViewHolder> {

    Context context;
    List<Recents> listRecents;

    public RecentAdapter(Context context, List<Recents> listRecents) {
        this.context = context;
        this.listRecents = listRecents;
    }

    @Override
    public ListWallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item, parent, false);
        int height = parent.getMeasuredHeight()/2;
        view.setMinimumHeight(height);
        return new ListWallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListWallpaperViewHolder holder, final int position) {
        Picasso.get()
                .load(listRecents.get(position).getImageLink())
                .into(holder.wallpaper, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(listRecents.get(position).getImageLink())
                                .error(R.drawable.ic_terrain_black_24dp)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(holder.wallpaper, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.d("ERROR","Couldn't load wallpaper");
                                    }
                                });
                    }
                });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, WallpaperDetail.class);
                Wallpaper wallpaper = new Wallpaper(listRecents.get(position).getCategoryId(),
                        listRecents.get(position).getImageLink());
                Common.WALLPAPER_SELECTED = wallpaper;
                Common.WALLPAPER_SELECTED_KEY = listRecents.get(position).getKey();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRecents.size();
    }
}
