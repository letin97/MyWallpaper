package com.example.letrongtin.mywallpaper.database.localdatabase;

import com.example.letrongtin.mywallpaper.database.Recents;
import com.example.letrongtin.mywallpaper.database.datasource.IRecentsDataSource;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Le Trong Tin on 3/23/2018.
 */

public class RecentsDataSource implements IRecentsDataSource {

    private RecentsDAO recentsDAO;

    private static RecentsDataSource instance;

    public RecentsDataSource(RecentsDAO recentsDAO) {
        this.recentsDAO = recentsDAO;
    }

    public static RecentsDataSource getInstance(RecentsDAO recentsDAO) {
        if (instance == null)
            instance = new RecentsDataSource(recentsDAO);
        return instance;
    }

    @Override
    public Flowable<List<Recents>> getAllRecents() {
        return recentsDAO.getAllRecents();
    }

    @Override
    public void insertRecents(Recents... recents) {
        recentsDAO.insertRecents(recents);
    }

    @Override
    public void updateRecents(Recents... recents) {
        recentsDAO.updateRecents(recents);
    }

    @Override
    public void deletaRecents(Recents... recents) {
        recentsDAO.deletaRecents(recents);
    }

    @Override
    public void deleteAllRecents() {
        recentsDAO.deleteAllRecents();
    }
}
