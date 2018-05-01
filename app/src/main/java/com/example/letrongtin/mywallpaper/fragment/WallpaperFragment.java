package com.example.letrongtin.mywallpaper.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.adapter.WallpaperFragmentAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class WallpaperFragment extends Fragment {

    private static WallpaperFragment instance;

    public static WallpaperFragment getInstance() {
        if (instance == null)
            instance = new WallpaperFragment();
        return instance;
    }

    TabLayout tabLayout;
    ViewPager viewPager;

    public WallpaperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallpaper, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        WallpaperFragmentAdapter adapter = new WallpaperFragmentAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
