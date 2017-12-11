package com.hqj.universityfinance.utils.images;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.io.IOException;
import java.net.Authenticator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wang on 17-12-4.
 */

public class MyImageLoader{

    private static MyImageLoader sInstance;
    private ImageCache mImageCache = new MemoryCache();

    private ExecutorService mThreadPool =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    public static MyImageLoader getInstance() {
        if (sInstance == null) {
            synchronized (MyImageLoader.class) {
                sInstance = new MyImageLoader();
            }
        }
        return sInstance;
    }

    public void displayImage(ImageView imageView, String url) {
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            displayImageByHttp(imageView, url);
        }
    }

    private void displayImageByHttp(final ImageView imageView, final String url) {
        imageView.setTag(url);
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap != null) {
                    mImageCache.put(url, bitmap);
                    updateImage(imageView, bitmap);
                }
            }
        });
    }

    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            bitmap = BitmapFactory.decodeStream(response.body().byteStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void updateImage(final ImageView imageView, final Bitmap bitmap) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
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
