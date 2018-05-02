package com.example.letrongtin.mywallpaper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.letrongtin.mywallpaper.fragment.NewFragment;
import com.example.letrongtin.mywallpaper.fragment.PopularFragment;
import com.example.letrongtin.mywallpaper.fragment.TrendingFragment;

/**
 * Created by Le Trong Tin on 3/21/2018.
 */

public class WallpaperFragmentAdapter extends FragmentPagerAdapter {

    private Context context;

    public WallpaperFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return NewFragment.getInstance();
            case 1:
                return TrendingFragment.getInstance();
            case 2:
                return PopularFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "New";
            case 1:
                return "Trending";
            case 2:
                return "Popular";
        }
        return "";
    }
}
