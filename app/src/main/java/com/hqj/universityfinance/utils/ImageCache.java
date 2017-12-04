package com.hqj.universityfinance.utils;

import android.graphics.Bitmap;

/**
 * Created by wang on 17-12-4.
 */

public interface ImageCache {
    public Bitmap get(String url);

    public void put(String url, Bitmap bmp);
}
