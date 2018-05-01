package com.example.letrongtin.mywallpaper.database.datasource;

import com.example.letrongtin.mywallpaper.database.Favorite;
import com.example.letrongtin.mywallpaper.database.localdatabase.FavoriteDAO;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteRepository implements FavoriteDAO{

    private FavoriteDAO favoriteDAO;

    private static FavoriteRepository instance;

    public FavoriteRepository(FavoriteDAO favoriteDAO) {
        if (instance == null)
            instance = new FavoriteRepository(favoriteDAO);
        this.favoriteDAO = favoriteDAO;
    }

    @Override
    public Flowable<List<Favorite>> getAllFavorite() {
        return favoriteDAO.getAllFavorite();
    }

    @Override
    public void insertFavorite(Favorite... favorites) {
        favoriteDAO.insertFavorite(favorites);
    }

    @Override
    public void updateFavorite(Favorite... favorites) {
        favoriteDAO.updateFavorite(favorites);
    }

    @Override
    public void deletaFavorite(Favorite... favorites) {
        favoriteDAO.deletaFavorite(favorites);
    }

    @Override
    public void deleteAllFavorite() {
        favoriteDAO.deleteAllFavorite();
    }

    @Override
    public void deleteFavoriteByImageLink(String imageLink) {
        favoriteDAO.deleteFavoriteByImageLink(imageLink);
    }

    @Override
    public Favorite getFavoriteByImageLink(String imageLink) {
        return favoriteDAO.getFavoriteByImageLink(imageLink);
    }
}
