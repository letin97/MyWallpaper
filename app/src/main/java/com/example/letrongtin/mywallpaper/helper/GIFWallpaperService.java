package com.example.letrongtin.mywallpaper.helper;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GIFWallpaperService extends WallpaperService {

    public static String gifAddr = "";

    @Override
    public Engine onCreateEngine() {
        return new GIFWallpaperEngine();
    }

    private class GIFWallpaperEngine extends WallpaperService.Engine {
        private final int frameDuration = 20;

        private SurfaceHolder holder;
        private Movie movie;
        private boolean visible;
        private Handler handler;
        private InputStream gifInputStream;
        private float scaleX, scaleY;

        GIFWallpaperEngine() {
            handler = new Handler();
            Thread thread = new Thread(loadGIF);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
        }

        private Runnable loadGIF = new Runnable() {
            public void run() {
                try {
                    URL gifURL = new URL(gifAddr);
                    HttpURLConnection connection = (HttpURLConnection) gifURL.openConnection();
                    gifInputStream = connection.getInputStream();
                    movie = Movie.decodeStream(gifInputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        gifInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        private Runnable drawGIF = new Runnable() {
            public void run() {
                draw();
            }
        };

        private void draw() {
            if (visible) {
                Canvas canvas = holder.lockCanvas();
                canvas.save();
                // Adjust size and position so that
                // the image looks good on your screen
                canvas.scale(scaleX, scaleY);
                movie.draw(canvas, 0, 0);
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                movie.setTime((int) (System.currentTimeMillis() % movie.duration()));

                handler.removeCallbacks(drawGIF);
                if (visible) handler.postDelayed(drawGIF, frameDuration);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            scaleX = width / (1f * movie.width());
            scaleY = height / (1f * movie.height());
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawGIF);
            } else {
                handler.removeCallbacks(drawGIF);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawGIF);
            this.visible = false;
        }
    }
}
