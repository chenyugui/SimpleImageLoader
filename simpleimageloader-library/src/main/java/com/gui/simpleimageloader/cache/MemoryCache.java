package com.gui.simpleimageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by 47459 on 2016/10/11.
 * 内存缓存类
 */
public class MemoryCache implements ImageCache {
    private LruCache<String, Bitmap> lruCache;


    /**
     * @param percentOfMaxSize 该内存缓存对象的缓存大小占程序总内存大小的百分之几
     */
    public MemoryCache(int percentOfMaxSize) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int size = maxMemory * percentOfMaxSize / 100;
        lruCache = new LruCache<String, Bitmap>(size) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public void put(String uri, Bitmap bitmap) {
        lruCache.put(uri, bitmap);
    }

    @Override
    public Bitmap get(String uri, int maxWidth, int maxHeight) {
        return lruCache.get(uri);
    }

    @Override
    public void remove(String uri) {
        lruCache.remove(uri);
    }
}
