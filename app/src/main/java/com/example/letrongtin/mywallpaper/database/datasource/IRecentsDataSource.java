package com.example.letrongtin.mywallpaper.database.datasource;

import com.example.letrongtin.mywallpaper.database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Le Trong Tin on 3/23/2018.
 */

public interface IRecentsDataSource {

    Flowable<List<Recents>> getAllRecents();

    void insertRecents(Recents... recents);

    void updateRecents(Recents... recents);

    void deletaRecents(Recents... recents);

    void deleteAllRecents();
}
