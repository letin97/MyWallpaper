package com.example.letrongtin.mywallpaper.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.letrongtin.mywallpaper.fragment.CategoryFragment;
import com.example.letrongtin.mywallpaper.fragment.TrendingFragment;
import com.example.letrongtin.mywallpaper.fragment.RecentsFragment;

/**
 * Created by Le Trong Tin on 3/21/2018.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private Context context;

    public MyFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return CategoryFragment.getInstance();
            case 1:
                return TrendingFragment.getInstance();
            case 2:
                return RecentsFragment.getInstance(context);
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
                return "Category";
            case 1:
                return "Trending";
            case 2:
                return "Recent";
        }
        return "";
    }
}
