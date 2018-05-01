package com.example.letrongtin.mywallpaper.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letrongtin.mywallpaper.Interface.ItemRecentAdapterClickListener;
import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.activity.WallpaperDetail;
import com.example.letrongtin.mywallpaper.adapter.RecentAdapter;
import com.example.letrongtin.mywallpaper.database.Recents;
import com.example.letrongtin.mywallpaper.database.datasource.RecentsRepository;
import com.example.letrongtin.mywallpaper.database.localdatabase.LocalDatabase;
import com.example.letrongtin.mywallpaper.database.localdatabase.RecentsDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */

@SuppressLint("ValidFragment")
public class RecentsFragment extends Fragment implements ItemRecentAdapterClickListener{

    private static RecentsFragment instance;

    public static RecentsFragment getInstance(Context context) {
        if (instance == null){
            instance = new RecentsFragment(context);
        }
        return instance;
    }

    Context context;

    RecyclerView recyclerView;
    List<Recents> recentsList;
    RecentAdapter adapter;

    // Room database
    CompositeDisposable compositeDisposable;
    RecentsRepository recentsRepository;

    public RecentsFragment(Context context) {

        this.context = context;

        recentsList = new ArrayList<>();
        adapter = new RecentAdapter(context, recentsList, this);

        // Init Roomdatabase
        compositeDisposable = new CompositeDisposable();
        LocalDatabase localDatabase = LocalDatabase.getInstance(context);
        recentsRepository = RecentsRepository.getInstance(RecentsDataSource.getInstance(localDatabase.recentsDAO()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recents, container, false);
        recyclerView = view.findViewById(R.id.recycler_recent);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
        
        loadRecent();

        return view;

    }

    private void loadRecent() {
        Disposable disposable = recentsRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Recents>>() {
                    @Override
                    public void accept(List<Recents> recents) throws Exception {
                        onGetAllRecentsSuccess(recents);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR", throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void deleteRecent(final int position) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                recentsRepository.deletaRecents(recentsList.get(position));
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ERROR", throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
        compositeDisposable.add(disposable);
    }

    private void onGetAllRecentsSuccess(List<Recents> recents) {
        recentsList.clear();
        recentsList.addAll(recents);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(context, WallpaperDetail.class);
        intent.putExtra("imageLink", recentsList.get(position).getImageLink());
        intent.putExtra("key", recentsList.get(position).getKey());
        context.startActivity(intent);
    }

    @Override
    public void onButtonDeleteClick(View view, int position) {
        deleteRecent(position);
    }
}
