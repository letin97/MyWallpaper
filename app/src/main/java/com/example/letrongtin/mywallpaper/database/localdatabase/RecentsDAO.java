package com.example.letrongtin.mywallpaper.database.localdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.letrongtin.mywallpaper.database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Le Trong Tin on 3/23/2018.
 */

@Dao
public interface RecentsDAO {

    @Query("SELECT * FROM recents ORDER BY saveTime DESC LIMIT 10")
    Flowable<List<Recents>> getAllRecents();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecents(Recents... recents);

    @Update
    void updateRecents(Recents... recents);

    @Delete
    void deletaRecents(Recents... recents);

    @Query("DELETE FROM recents")
    void deleteAllRecents();

}
