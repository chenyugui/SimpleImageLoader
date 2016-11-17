package com.gui.simpleimageloader.cache;

import android.graphics.Bitmap;

/**
 * Created by 47459 on 2016/10/12.
 * 图片缓存接口
 */
public interface ImageCache {
    void put(String uri, Bitmap bitmap);

    Bitmap get(String uri, int maxWidth, int maxHeight);

    void remove(String uri);
}
