package com.example.letrongtin.mywallpaper.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letrongtin.mywallpaper.Interface.ItemClickListener;
import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.activity.ListWallpaper;
import com.example.letrongtin.mywallpaper.common.Common;
import com.example.letrongtin.mywallpaper.model.Category;
import com.example.letrongtin.mywallpaper.viewholder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference categoryBackground;

    //FirebaseUI adapter
    FirebaseRecyclerOptions<Category> options;
    FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter;

    RecyclerView recyclerView;


    private static CategoryFragment instance = null;

    public static CategoryFragment getInstance() {
        if (instance == null){
            instance = new CategoryFragment();
        }
        return instance;
    }

    public CategoryFragment() {

        database = FirebaseDatabase.getInstance();
        categoryBackground = database.getReference(Common.STR_CATEGORY_BACKGROUND);

        options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(categoryBackground, Category.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(final CategoryViewHolder holder, int position, final Category model) {
                Picasso.get()
                        .load(model.getImageLink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.background_image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                // Try
                                Picasso.get()
                                        .load(model.getImageLink())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.background_image, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Log.e("ERROR","Couldn't image");
                                            }
                                        });
                            }
                        });

                holder.category_name.setText(model.getName());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Common.CATEGORY_ID_SELECTED = adapter.getRef(position).getKey();
                        Common.CATEGORY_SELECTED = model.getName();
                        Intent listWallpaperIntent = new Intent(getActivity(), ListWallpaper.class);
                        startActivity(listWallpaperIntent);
                    }
                });
            }

            @Override
            public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.category_item, parent, false);
                return new CategoryViewHolder(itemView);
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.recycler_category);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        setCategory();
        return view;
    }

    private void setCategory() {
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
