package com.example.letrongtin.mywallpaper.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.adapter.MyFragmentAdapter;
import com.example.letrongtin.mywallpaper.common.Common;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    TabLayout tabLayout;

    DrawerLayout drawer;
    NavigationView navigationView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
        }


        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check sign in



        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .build(), Common.SIGN_IN_REQUEST_CODE);
        }
        else {
            Snackbar.make(drawer, new StringBuilder("Welcome")
                    .append(FirebaseAuth.getInstance().getCurrentUser().getEmail()), Snackbar.LENGTH_LONG).show();
        }

        loadUserInformation();
    }

    private void loadUserInformation() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            View headLayout = navigationView.getHeaderView(0);
            TextView txtEmail = headLayout.findViewById(R.id.txtEmail);
            txtEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if (requestCode == Common.SIGN_IN_REQUEST_CODE ){
            //IdpResponse response = IdpResponse.fromResultIntent(data);
            //if (resultCode == RESULT_OK)
                    Snackbar.make(drawer, new StringBuilder("Welcome")
                        .append(FirebaseAuth.getInstance().getCurrentUser().getEmail()), Snackbar.LENGTH_LONG).show();

            Log.d("TAG", "FireBaseAuthActivity onActivityResult: " +FirebaseAuth.getInstance().getCurrentUser().getEmail());
//            if (ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
//            }


//            viewPager = findViewById(R.id.viewPager);
//            tabLayout = findViewById(R.id.tabLayout);
//
//            MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), this);
//            viewPager.setAdapter(adapter);
//            tabLayout.setupWithViewPager(viewPager);

        //}

        //Snackbar.make(drawer, "Fall ", Snackbar.LENGTH_LONG).show();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view_up) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
