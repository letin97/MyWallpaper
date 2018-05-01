package com.example.letrongtin.mywallpaper.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letrongtin.mywallpaper.R;

public class RecentViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgRecent;
    public TextView nameRecent, timeRecent;
    public ImageButton btnDeleteRecent;

    public RecentViewHolder(View itemView) {
        super(itemView);
        imgRecent = itemView.findViewById(R.id.img_recent);
        nameRecent = itemView.findViewById(R.id.name_recent);
        timeRecent = itemView.findViewById(R.id.time_recent);
        btnDeleteRecent = itemView.findViewById(R.id.btn_delete_recent);
    }
}
