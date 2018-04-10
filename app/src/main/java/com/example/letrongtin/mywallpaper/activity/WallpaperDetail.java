package com.example.letrongtin.mywallpaper.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.HashMap;
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

public class WallpaperDetail extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fabWallpaper, fabDownload;
    CoordinatorLayout rootLayout;
    ImageView image;


    FloatingActionMenu mainFloating;
    com.github.clans.fab.FloatingActionButton fabShare;
    // Facebook
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    // Room Database
    CompositeDisposable compositeDisposable;
    RecentsRepository recentsRepository;


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


        // Init Fackebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);



        // Init Roomdatabase
        compositeDisposable = new CompositeDisposable();
        LocalDatabase localDatabase = LocalDatabase.getInstance(this);
        recentsRepository = RecentsRepository.getInstance(RecentsDataSource.getInstance(localDatabase.recentsDAO()));




        rootLayout = findViewById(R.id.rootLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setTitle(Common.CATEGORY_SELECTED);

        image = findViewById(R.id.imageThumb);
        Picasso.get()
                .load(Common.WALLPAPER_SELECTED.getImageLink())
                .into(image);



        mainFloating = findViewById(R.id.menu);
        fabShare = findViewById(R.id.fb_share);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AAA", "OK");
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
                        .load(Common.WALLPAPER_SELECTED.getImageLink())
                        .into(facebookConvertBitmap);
            }
        });


        // add to recent
        addWallpaperToRecents();

        fabWallpaper = findViewById(R.id.fabWallpaper);
        fabWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get()
                        .load(Common.WALLPAPER_SELECTED.getImageLink())
                        .into(target);
            }
        });

        fabDownload = findViewById(R.id.fabDowload);
        fabDownload.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(WallpaperDetail.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
                } else {
                    AlertDialog alertDialog = new SpotsDialog(WallpaperDetail.this);
                    alertDialog.show();
                    alertDialog.setMessage("Please waiting...");
                    String filename = UUID.randomUUID().toString()+".png";

                    Picasso.get()
                            .load(Common.WALLPAPER_SELECTED.getImageLink())
                            .into(new SaveImageHelper(getApplicationContext(),
                                    alertDialog,
                                    getContentResolver(),
                                    filename,
                                    "My Wallpaper"));
                }
            }
        });



        // view count
        increaseViewCount();
    }

    private void increaseViewCount() {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPERS)
                .child(Common.WALLPAPER_SELECTED_KEY)
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
                                    .getReference(Common.STR_WALLPAPERS)
                                    .child(Common.WALLPAPER_SELECTED_KEY)
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
                                    .getReference(Common.STR_WALLPAPERS)
                                    .child(Common.WALLPAPER_SELECTED_KEY)
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

    private void addWallpaperToRecents() {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Recents recents = new Recents(Common.WALLPAPER_SELECTED.getImageLink(),
                        Common.WALLPAPER_SELECTED.getCategoryId(),
                        String.valueOf(System.currentTimeMillis()),
                        Common.WALLPAPER_SELECTED_KEY);
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
                        .load(Common.WALLPAPER_SELECTED.getImageLink())
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
}
