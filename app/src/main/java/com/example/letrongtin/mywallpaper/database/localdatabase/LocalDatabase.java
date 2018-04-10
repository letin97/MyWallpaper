package com.example.letrongtin.mywallpaper.database.localdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.letrongtin.mywallpaper.database.Recents;

import static com.example.letrongtin.mywallpaper.database.localdatabase.LocalDatabase.DATABASE_VERSION;

/**
 * Created by Le Trong Tin on 3/23/2018.
 */

@Database(entities = {Recents.class}, version = DATABASE_VERSION)
public abstract class LocalDatabase extends RoomDatabase {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "MyWallpaper";

    public abstract RecentsDAO recentsDAO();

    public static  LocalDatabase instance;

    public static LocalDatabase getInstance(Context context) {

        if (instance == null ) {
            instance = Room.databaseBuilder(context, LocalDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
