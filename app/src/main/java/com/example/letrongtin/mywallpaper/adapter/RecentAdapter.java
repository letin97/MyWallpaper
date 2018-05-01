package com.example.letrongtin.mywallpaper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letrongtin.mywallpaper.Interface.ItemRecentAdapterClickListener;
import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.database.Recents;
import com.example.letrongtin.mywallpaper.viewholder.RecentViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Le Trong Tin on 3/23/2018.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentViewHolder> {

    private Context context;
    private List<Recents> listRecents;
    private ItemRecentAdapterClickListener listener;

    public RecentAdapter(Context context, List<Recents> listRecents, ItemRecentAdapterClickListener listener) {
        this.context = context;
        this.listRecents = listRecents;
        this.listener = listener;
    }


    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_item, parent, false);
        return new RecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecentViewHolder holder, final int position) {
        Picasso.get()
                .load(listRecents.get(position).getImageLink())
                .fit()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.imgRecent, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(listRecents.get(position).getImageLink())
                                .fit()
                                .error(R.drawable.ic_terrain_black_24dp)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(holder.imgRecent, new Callback() {
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
        holder.timeRecent.setText(listRecents.get(position).getSaveTime());

        holder.imgRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });

        holder.btnDeleteRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonDeleteClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRecents.size();
    }
}
