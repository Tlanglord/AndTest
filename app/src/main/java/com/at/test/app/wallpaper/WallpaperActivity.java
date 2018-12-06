package com.at.test.app.wallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.at.test.R;

public class WallpaperActivity extends Activity {
    private static final String TAG = "WallpaperActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WallpaperManager wallpaperManager = (WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT > 24) {
            Log.v(TAG, wallpaperManager.isSetWallpaperAllowed() + "");
            Log.v(TAG, wallpaperManager.isWallpaperSupported() + "");

//            try {
//                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
//                Log.v(TAG, "setBitmap");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}
