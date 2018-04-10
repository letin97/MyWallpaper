package com.example.letrongtin.mywallpaper.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.letrongtin.mywallpaper.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },1000);

    }
}
