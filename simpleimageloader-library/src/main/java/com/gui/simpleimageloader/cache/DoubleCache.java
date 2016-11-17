package com.gui.simpleimageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by gui on 2016/11/3.
 * 图片双缓存类（内存缓存和本地缓存，  内存缓存优先取）<br>
 */

public class DoubleCache implements ImageCache {
    protected MemoryCache memoryCache;
    private DiskCache diskCache;

    public DoubleCache(int percentOfMaxSize, Context context) {
        memoryCache = new MemoryCache(percentOfMaxSize);
        diskCache = new DiskCache(context);
    }

    /**
     * 保存图片到内存缓存和本地缓存
     */
    @Override
    public void put(String uri, Bitmap bitmap) {
        //存放到内存缓存
        memoryCache.put(uri, bitmap);
        diskCache.put(uri, bitmap);
    }

    @Override
    public Bitmap get(String uri, int maxWidth, int maxHeight) {
        if (uri == null || uri.equals(""))
            return null;
        Bitmap bitmap = memoryCache.get(uri, maxWidth, maxHeight);
        if (bitmap != null) {//内存缓存存在
            return bitmap;
        }
        bitmap = diskCache.get(uri, maxWidth, maxHeight);
        if (bitmap != null) {
            memoryCache.put(uri, bitmap);
        }
        return bitmap;
    }

    @Override
    public void remove(String uri) {
        memoryCache.remove(uri);
    }

    /**
     * 设置图片本地存储的文件夹路径
     */
    public void setDirPath(String dirPath) {
        diskCache.setDirPath(dirPath);
    }
}
