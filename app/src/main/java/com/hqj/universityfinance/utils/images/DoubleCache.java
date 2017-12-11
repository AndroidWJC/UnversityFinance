package com.hqj.universityfinance.utils.images;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by wang on 17-12-4.
 */

public class DoubleCache implements ImageCache {

    ImageCache mMemoryCache = null;
    ImageCache mDiskCache = null;

    public DoubleCache(Context context) {
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(context);
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap == null) {
            bitmap = mDiskCache.get(url);
        }
        return bitmap;
    }

    @Override
    public void put(String url, Bitmap bmp) {
        mMemoryCache.put(url, bmp);
        mDiskCache.put(url, bmp);
    }
}
