package com.hqj.universityfinance.utils.images;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.hqj.universityfinance.utils.StreamCloseUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wang on 17-12-4.
 */

public class DiskCache implements ImageCache {

    private final static long DISK_CACHE_SIZE = 1024 * 1024 * 20;
    private final static int DISK_CACHE_INDEX = 0;
    private final static int IO_BUFFER_SIZE = 8 * 2014;

    private DiskLruCache mDiskLruCache;
    private DiskLruCache.Editor mEditor;
    private Context mContext;

    public DiskCache(Context context){
        mContext = context;
        File cacheFile = getDiskCachePath("bitmap");
        try {
            mDiskLruCache = DiskLruCache.open(cacheFile, getAppVersion(), 1, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
            mDiskLruCache = null;
        }
    }

    private File getDiskCachePath(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    private int getAppVersion() {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return 1;
    }

    @Override
    public Bitmap get(String url) {
        String cacheKey = hashKeyFromUrl(url);
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(cacheKey);
            if (snapshot != null) {
                in = snapshot.getInputStream(DISK_CACHE_INDEX);
                bitmap = BitmapFactory.decodeStream(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamCloseUtils.closeQuietly(in);
        }

        return bitmap;
    }

    @Override
    public void put(final String url, final Bitmap bmp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String key = hashKeyFromUrl(url);
                try {
                    mEditor = mDiskLruCache.edit(key);
                    if (mEditor != null) {
                        OutputStream os = mEditor.newOutputStream(DISK_CACHE_INDEX);
                        if (downloadImageToStream(key, os)) {
                            mEditor.commit();
                        } else {
                            mEditor.abort();
                        }
                    }
                    mDiskLruCache.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            cacheKey = bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }

        return cacheKey;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                builder.append("0");
            }
            builder.append(hex);
        }

        return builder.toString();
    }

    private boolean downloadImageToStream(String url, OutputStream outputStream) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();

            in = new BufferedInputStream(response.body().byteStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int bytes;
            while ((bytes = in.read()) != -1) {
                out.write(bytes);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamCloseUtils.closeQuietly(in);
            StreamCloseUtils.closeQuietly(out);
        }
        return false;
    }

}
