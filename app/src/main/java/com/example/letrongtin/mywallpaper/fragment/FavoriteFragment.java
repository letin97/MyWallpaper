package com.example.letrongtin.mywallpaper.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letrongtin.mywallpaper.Interface.ItemClickListener;
import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.activity.WallpaperDetail;
import com.example.letrongtin.mywallpaper.adapter.FavoriteAdapter;
import com.example.letrongtin.mywallpaper.database.Favorite;
import com.example.letrongtin.mywallpaper.database.datasource.FavoriteRepository;
import com.example.letrongtin.mywallpaper.database.localdatabase.LocalDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class FavoriteFragment extends Fragment implements ItemClickListener{

    private static FavoriteFragment instance;

    public static FavoriteFragment getInstance(Context context) {
        if (instance == null)
            instance = new FavoriteFragment(context);
        return instance;
    }

    Context context;

    RecyclerView recyclerView;
    List<Favorite> favoriteList;
    FavoriteAdapter adapter;

    // Room database
    CompositeDisposable compositeDisposable;
    FavoriteRepository favoriteRepository;

    public FavoriteFragment(Context context) {

        this.context = context;

        favoriteList = new ArrayList<>();
        adapter = new FavoriteAdapter(context, favoriteList, this);

        // Init Roomdatabase
        compositeDisposable = new CompositeDisposable();
        LocalDatabase localDatabase = LocalDatabase.getInstance(context);
        favoriteRepository = FavoriteRepository.getInstance(localDatabase.favoriteDAO());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.recycler_favorite);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

        loadFavorites();

        return view;

    }

    private void loadFavorites() {
        Disposable disposable = favoriteRepository.getAllFavorite()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> favorites) throws Exception {
                        onGetAllRecentsSuccess(favorites);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR", throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }


    private void onGetAllRecentsSuccess(List<Favorite> favorites) {
        favoriteList.clear();
        favoriteList.addAll(favorites);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(context, WallpaperDetail.class);
        intent.putExtra("imageLink", favoriteList.get(position).getImageLink());
        intent.putExtra("key", favoriteList.get(position).getKey());
        context.startActivity(intent);
    }
}
