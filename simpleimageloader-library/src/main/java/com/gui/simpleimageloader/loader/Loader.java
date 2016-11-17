package com.gui.simpleimageloader.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.gui.simpleimageloader.cache.ImageCache;


/**
 * Created by gui on 2016/11/10.
 * 图片加载者
 */

public interface Loader {
    /**
     * 获取bitmap（只获取bitmap和设置bitmap到缓存里（如果缓存里没有bitmap），  不做显示bitmap的操作）
     *
     * @param uri
     * @param imageView
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    Bitmap loadImage(String uri, ImageView imageView, int maxWidth, int maxHeight);

    void setImageCache(ImageCache imageCache);
}
