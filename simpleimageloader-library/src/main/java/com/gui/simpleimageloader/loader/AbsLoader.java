package com.gui.simpleimageloader.loader;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.gui.simpleimageloader.cache.ImageCache;
import com.gui.simpleimageloader.cache.MemoryCache;


/**
 * Created by gui on 2016/11/10.
 * 图片获取者抽象类
 */

abstract class AbsLoader implements Loader {
    private ImageCache imageCache;

    @Override
    public Bitmap loadImage(String uri, ImageView imageView, int maxWidth, int maxHeight) {
        if (imageCache == null)
            imageCache = new MemoryCache(10);
        Bitmap bitmap = imageCache.get(uri, maxWidth, maxHeight);
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                Log.d("TAGTAG", "------图片缓存存在," + getClass().getName());
                return bitmap;
            }
            //bitmap已被回收
            imageCache.remove(uri);
        }
        Log.d("TAGTAG", "------图片缓存不存在," + getClass().getName());
        bitmap = onLoadImage(uri, maxWidth, maxHeight);
        if (bitmap != null) {
            imageCache.put(uri, bitmap);
        }
        return bitmap;
    }

    @Override
    public void setImageCache(ImageCache imageCache) {
        this.imageCache = imageCache;
    }

    /**
     * 获取图片
     */
    abstract Bitmap onLoadImage(String uri, int maxWidth, int maxHeight);
}
