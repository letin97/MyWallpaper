package com.example.letrongtin.mywallpaper.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;


/**
 * Created by Le Trong Tin on 3/23/2018.
 */

@Entity(tableName = "favorite", primaryKeys = {"imageLink"})
public class Favorite {

    @ColumnInfo(name = "imageLink")
    @NonNull
    private String imageLink;

    @ColumnInfo(name = "saveTime")
    private String saveTime;

    @ColumnInfo(name = "key")
    private String key;

    public Favorite(@NonNull String imageLink, String saveTime, String key) {
        this.imageLink = imageLink;
        this.saveTime = saveTime;
        this.key = key;
    }

    @NonNull
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(@NonNull String imageLink) {
        this.imageLink = imageLink;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
