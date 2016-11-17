package com.gui.simpleimageloader.policy;


import com.gui.simpleimageloader.request.BitmapRequest;

/**
 * Created by gui on 2016/11/11.
 * 逆序加载策略,即从最后加入队列的请求进行加载
 */

public class ReversePolicy implements LoadPolicy {
    @Override
    public int compare(BitmapRequest thisBitmapRequest, BitmapRequest anotherBitmapRequest) {
        // 注意Bitmap请求要先执行最晚加入队列的请求,ImageLoader的策略
        return anotherBitmapRequest.serialNum - thisBitmapRequest.serialNum;

        //相当于：
//        if (thisBitmapRequest.serialNum > anotherBitmapRequest.serialNum) return -1;
//        else if (thisBitmapRequest.serialNum == anotherBitmapRequest.serialNum) return 0;
//        else if (thisBitmapRequest.serialNum > anotherBitmapRequest.serialNum) return 1;
    }
}
