package com.example.letrongtin.mywallpaper.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.common.Common;
import com.example.letrongtin.mywallpaper.fragment.CategoryFragment;
import com.example.letrongtin.mywallpaper.fragment.ExploreFragment;
import com.example.letrongtin.mywallpaper.fragment.FavoriteFragment;
import com.example.letrongtin.mywallpaper.fragment.RecentsFragment;
import com.example.letrongtin.mywallpaper.fragment.WallpaperFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;

    private static final String TAG_WALLPAPER = "Wallpaper";
    private static final String TAG_CATEGORY = "Category";
    private static final String TAG_EXPLORE = "Explore";
    private static final String TAG_FAVORITE = "Favorite";
    private static final String TAG_RECENT = "Recent";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Init
        getSupportActionBar().setTitle(TAG_WALLPAPER);
        navigationView.getMenu().getItem(0).setChecked(true);
        loadFragment(new WallpaperFragment());


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Common.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "You need accept this permission to download image ", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.nav_wallpaper:
                toolbar.setTitle(TAG_WALLPAPER);
                fragment = WallpaperFragment.getInstance();
                break;
            case R.id.nav_category:
                toolbar.setTitle(TAG_CATEGORY);
                fragment = CategoryFragment.getInstance();
                break;
            case R.id.nav_explore:
                toolbar.setTitle(TAG_EXPLORE);
                fragment = ExploreFragment.getInstance();
                break;
            case R.id.nav_favorite:
                toolbar.setTitle(TAG_FAVORITE);
                fragment = FavoriteFragment.getInstance(HomeActivity.this);
                break;
            case R.id.nav_recent:
                toolbar.setTitle(TAG_RECENT);
                fragment = RecentsFragment.getInstance(HomeActivity.this);
                break;
            default:
                toolbar.setTitle(TAG_WALLPAPER);
                fragment = WallpaperFragment.getInstance();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
            return true;
        }
        return false;
    }
}
