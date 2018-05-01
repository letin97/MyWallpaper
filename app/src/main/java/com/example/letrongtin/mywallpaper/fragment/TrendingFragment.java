package com.example.letrongtin.mywallpaper.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letrongtin.mywallpaper.Interface.ItemClickListener;
import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.activity.WallpaperDetail;
import com.example.letrongtin.mywallpaper.common.Common;
import com.example.letrongtin.mywallpaper.model.Wallpaper;
import com.example.letrongtin.mywallpaper.viewholder.ListWallpaperViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {

    private static TrendingFragment instance = null;

    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference wallpaper;

    FirebaseRecyclerOptions<Wallpaper> options;
    FirebaseRecyclerAdapter<Wallpaper, ListWallpaperViewHolder> adapter;

    public static TrendingFragment getInstance() {
        if (instance == null){
            instance = new TrendingFragment();
        }
        return instance;
    }


    public TrendingFragment() {

        // dâtbase
        database = FirebaseDatabase.getInstance();
        wallpaper = database.getReference(Common.STR_WALLPAPER);

        Query query = wallpaper.orderByChild("viewCount")
                .limitToLast(10);

        options = new FirebaseRecyclerOptions.Builder<Wallpaper>()
                .setQuery(query, Wallpaper.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Wallpaper, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final ListWallpaperViewHolder holder, int position, final Wallpaper model) {
                Picasso.get()
                        .load(model.getImageLink())
                        .fit()
                        .into(holder.wallpaper, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get()
                                        .load(model.getImageLink())
                                        .fit()
                                        .error(R.drawable.ic_terrain_black_24dp)
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
                        Intent intent = new Intent(getActivity(), WallpaperDetail.class);
                        //intent.putExtra()
                        startActivity(intent);
                    }
                });
            }

            @Override
            public ListWallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item, parent, false);
                int height = parent.getMeasuredHeight()/2;
                view.setMinimumHeight(height);
                return new ListWallpaperViewHolder(view);
            }
        };

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        recyclerView = view.findViewById(R.id.recycler_trending);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadTrending();
        return view;
    }

    private void loadTrending() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        if (adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

}
