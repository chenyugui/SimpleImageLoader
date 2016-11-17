package com.gui.simpleimageloader.request;

import android.widget.ImageView;

import com.gui.simpleimageloader.core.DisplayConfig;
import com.gui.simpleimageloader.core.SimpleImageLoader;
import com.gui.simpleimageloader.policy.LoadPolicy;
import com.gui.simpleimageloader.policy.ReversePolicy;


public class BitmapRequest implements Comparable<BitmapRequest> {
    //序列号
    public int serialNum;
    public ImageView imageView;
    public String uri;
    public int maxWidth;
    public int maxHeight;
    public SimpleImageLoader.DisplayCallBack displayCallBack;
    public LoadPolicy loadPolicy;
    public DisplayConfig displayConfig;

    public BitmapRequest(String uri, ImageView imageView, int maxWidth, int maxHeight, LoadPolicy loadPolicy, DisplayConfig displayConfig, SimpleImageLoader.DisplayCallBack displayCallBack) {
        if (loadPolicy == null)
            loadPolicy = new ReversePolicy();
        this.uri = uri;
        this.imageView = imageView;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.loadPolicy = loadPolicy;
        this.displayConfig = displayConfig;
        this.displayCallBack = displayCallBack;
    }

    @Override
    public int compareTo(BitmapRequest another) {
        if (another == null) {
            return 0;
        }
        return loadPolicy.compare(this, another);
    }
}
