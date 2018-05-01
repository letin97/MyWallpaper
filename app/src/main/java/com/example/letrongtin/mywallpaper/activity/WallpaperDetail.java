package com.example.letrongtin.mywallpaper.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.letrongtin.mywallpaper.R;
import com.example.letrongtin.mywallpaper.common.Common;
import com.example.letrongtin.mywallpaper.database.Recents;
import com.example.letrongtin.mywallpaper.database.datasource.RecentsRepository;
import com.example.letrongtin.mywallpaper.database.localdatabase.LocalDatabase;
import com.example.letrongtin.mywallpaper.database.localdatabase.RecentsDataSource;
import com.example.letrongtin.mywallpaper.helper.SaveImageHelper;
import com.example.letrongtin.mywallpaper.model.Wallpaper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WallpaperDetail extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    RelativeLayout rootLayout;
    ImageView image;
    BottomNavigationView menuBottom;

    // Facebook
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    // Room Database
    CompositeDisposable compositeDisposable;
    RecentsRepository recentsRepository;

    String imageLink, key;


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                wallpaperManager.setBitmap(bitmap);
                Snackbar.make(rootLayout, "Wallpaper was set",Snackbar.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private Target facebookConvertBitmap = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap).build();

            if (ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                shareDialog.show(content);

            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        // Init Fackebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        // Init Roomdatabase
        compositeDisposable = new CompositeDisposable();
        LocalDatabase localDatabase = LocalDatabase.getInstance(this);
        recentsRepository = RecentsRepository.getInstance(RecentsDataSource.getInstance(localDatabase.recentsDAO()));


        rootLayout = findViewById(R.id.root_layout);
        image = findViewById(R.id.imageThumb);
        menuBottom = findViewById(R.id.menu_bottom);

        if (getIntent()!=null){
            Intent intent = getIntent();
            imageLink = intent.getStringExtra("imageLink");
            key = intent.getStringExtra("key");
        }

        Picasso.get()
                .load(imageLink)
                .into(image);

        menuBottom.setOnNavigationItemSelectedListener(this);

        // add to recent
        //addWallpaperToRecents();

        // view count
        increaseViewCount();
    }

    private void increaseViewCount() {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // set count
                        if (dataSnapshot.hasChild("viewCount")){
                            Wallpaper wallpaper = dataSnapshot.getValue(Wallpaper.class);
                            long count = wallpaper.getViewCount() + 1;

                            //update
                            Map<String,Object> update_view = new HashMap<>();
                            update_view.put("viewCount", count);

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(key)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(WallpaperDetail.this, "Cannot update view count", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            Map<String,Object> update_view = new HashMap<>();
                            update_view.put("viewCount", 1L);

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(key)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(WallpaperDetail.this, "Cannot set default view count", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void addWallpaperToRecent() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy", Locale.CHINA);
                String time = simpleDateFormat.format(new Date());
                Recents recents = new Recents(imageLink, time, key);
                recentsRepository.insertRecents(recents);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ERROR", throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Common.PERMISSION_REQUEST_CODE:
                AlertDialog alertDialog = new SpotsDialog(WallpaperDetail.this);
                alertDialog.show();
                alertDialog.setMessage("Please waiting...");

                // UUID (Universally Unique IDentifier)
                String filename = UUID.randomUUID().toString()+".png";

                Picasso.get()
                        .load(imageLink)
                        .into(new SaveImageHelper(getApplicationContext(),
                                alertDialog,
                                getContentResolver(),
                                filename,
                                "My Wallpaper"));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Picasso.get().cancelRequest(target);
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareToFaceBook(){

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(WallpaperDetail.this, "Share successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(WallpaperDetail.this, "Share cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(WallpaperDetail.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Picasso.get()
                .load(imageLink)
                .into(facebookConvertBitmap);
    }

    private void setWallpaper(){
        Picasso.get()
                .load(imageLink)
                .into(target);
        addWallpaperToRecent();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void downloadWallpaper(){
        if (ActivityCompat.checkSelfPermission(WallpaperDetail.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
        } else {
            AlertDialog alertDialog = new SpotsDialog(WallpaperDetail.this);
            alertDialog.show();
            alertDialog.setMessage("Please waiting...");
            String filename = UUID.randomUUID().toString()+".png";

            Picasso.get()
                    .load(imageLink)
                    .into(new SaveImageHelper(getApplicationContext(),
                            alertDialog,
                            getContentResolver(),
                            filename,
                            "My Wallpaper"));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                shareToFaceBook();
                break;
            case R.id.action_wallpaper:
                setWallpaper();
                break;
            case R.id.action_download:
                downloadWallpaper();
                break;
        }
        return false;
    }
}
