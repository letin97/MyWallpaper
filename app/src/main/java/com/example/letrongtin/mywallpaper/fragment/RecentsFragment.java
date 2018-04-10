package com.example.letrongtin.mywallpaper.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.adapter.RecentAdapter;
import com.example.letrongtin.mywallpaper.database.Recents;
import com.example.letrongtin.mywallpaper.database.datasource.RecentsRepository;
import com.example.letrongtin.mywallpaper.database.localdatabase.LocalDatabase;
import com.example.letrongtin.mywallpaper.database.localdatabase.RecentsDataSource;

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
public class RecentsFragment extends Fragment {

    private static RecentsFragment instance = null;

    Context context;

    RecyclerView recyclerView;
    List<Recents> recentsList;
    RecentAdapter adapter;

    // Room database
    CompositeDisposable compositeDisposable;
    RecentsRepository recentsRepository;

    public static RecentsFragment getInstance(Context context) {
        if (instance == null){
            instance = new RecentsFragment(context);
        }
        return instance;
    }


    public RecentsFragment(Context context) {

        this.context = context;

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recentsList = new ArrayList<>();
        adapter = new RecentAdapter(context, recentsList);
        recyclerView.setAdapter(adapter);
        
        loadRecents();

        return view;

    }

    private void loadRecents() {
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
}
