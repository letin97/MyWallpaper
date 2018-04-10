package com.example.letrongtin.mywallpaper.database.datasource;

import com.example.letrongtin.mywallpaper.database.Recents;
import com.example.letrongtin.mywallpaper.database.localdatabase.RecentsDataSource;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Le Trong Tin on 3/23/2018.
 */

public class RecentsRepository implements IRecentsDataSource {

    private RecentsDataSource recentsDataSource;

    private static RecentsRepository instance;

    public RecentsRepository(RecentsDataSource recentsDataSource) {
        this.recentsDataSource = recentsDataSource;
    }

    public static RecentsRepository getInstance(RecentsDataSource recentsDataSource) {
        if (instance == null)
            instance = new RecentsRepository(recentsDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return recentsDataSource.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        recentsDataSource.insertRecents(recents);
    }

    @Override
    public void updateRecents(Recents... recents) {
        recentsDataSource.updateRecents(recents);
    }

    @Override
    public void deletaRecents(Recents... recents) {
        recentsDataSource.deletaRecents(recents);
    }

    @Override
    public void deleteAllRecents() {
        recentsDataSource.deleteAllRecents();
    }
}
