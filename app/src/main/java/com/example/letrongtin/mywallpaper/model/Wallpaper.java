package com.example.letrongtin.mywallpaper.model;

/**
 * Created by Le Trong Tin on 3/22/2018.
 */

public class Wallpaper {

    private String categoryId;

    private String imageLink;

    private long viewCount;

    private long setCount;

    public Wallpaper() {
    }

    public Wallpaper(String categoryId, String imageLink, long viewCount, long setCount) {
        this.categoryId = categoryId;
        this.imageLink = imageLink;
        this.viewCount = viewCount;
        this.setCount = setCount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public long getSetCount() {
        return setCount;
    }

    public void setSetCount(long setCount) {
        this.setCount = setCount;
    }
}
