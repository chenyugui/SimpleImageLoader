package com.gui.simpleimageloader.policy;


import com.gui.simpleimageloader.request.BitmapRequest;

/**
 * Created by gui on 2016/11/11.
 * 加载策略接口
 */

public interface LoadPolicy {
    int compare(BitmapRequest bitmapRequest, BitmapRequest bitmapRequest2);
}
