package com.example.letrongtin.mywallpaper.database.localdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.letrongtin.mywallpaper.database.Favorite;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface FavoriteDAO {

    @Query("SELECT * FROM favorite ORDER BY saveTime DESC LIMIT 10")
    Flowable<List<Favorite>> getAllFavorite();

    @Insert
    void insertFavorite(Favorite... favorites);

    @Update
    void updateFavorite(Favorite... favorites);

    @Delete
    void deletaFavorite(Favorite... favorites);

    @Query("DELETE FROM favorite")
    void deleteAllFavorite();

    @Query("DELETE FROM favorite WHERE imageLink = :imageLink")
    void deleteFavoriteByImageLink(String imageLink);

    @Query("SELECT * FROM favorite WHERE imageLink = :imageLink")
    Favorite getFavoriteByImageLink(String imageLink);
}
