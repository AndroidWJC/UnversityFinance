package com.hqj.universityfinance.utils;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

/**
 * Created by wang on 17-12-4.
 */

public class ImageLoader{

    private static ImageLoader sInstance;
    private ImageCache mImageCache = new MemoryCache();

    private ImageLoader() {

    }

    public static ImageLoader getInstance() {
        if (sInstance == null) {
            synchronized (ImageLoader.class) {
                sInstance = new ImageLoader();
            }
        }
        return sInstance;
    }

    public void setImageCache(ImageCache imageCache) {
        mImageCache = imageCache;
    }

    public static Bitmap decodeBitmapFromResourceInSampleSize(Resources res, int resId,
                                                              int finalWidth, int finalHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, finalWidth, finalHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                            int finalWidth, int finalHeight) {
        int inSampleSize = 1;
        final int bitmapWidth = options.outWidth;
        final int bitmapHeight = options.outHeight;

        if (bitmapHeight > finalHeight || bitmapWidth > finalWidth) {
            final int halfHeight = bitmapHeight / 2;
            final int halfWeight = bitmapWidth / 2;

            while ((halfHeight / inSampleSize) >= finalHeight
                    && (halfWeight / inSampleSize) >= finalWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
