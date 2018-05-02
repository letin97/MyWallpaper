package com.example.letrongtin.mywallpaper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letrongtin.mywallpaper.Interface.ItemClickListener;
import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.database.Favorite;
import com.example.letrongtin.mywallpaper.viewholder.FavoriteViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder>{

    private Context context;
    private List<Favorite> favoriteList;
    private ItemClickListener listener;

    public FavoriteAdapter(Context context, List<Favorite> favoriteList, ItemClickListener listener) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.listener = listener;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoriteViewHolder holder, final int position) {
        Picasso.get()
                .load(favoriteList.get(position).getImageLink())
                .fit()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.wallpaper, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(favoriteList.get(position).getImageLink())
                                .fit()
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
        holder.wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }
}
