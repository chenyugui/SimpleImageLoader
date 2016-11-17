package com.gui.simpleimageloader.policy;


import com.gui.simpleimageloader.request.BitmapRequest;

/**
 * Created by gui on 2016/11/11.
 * 顺序加载策略
 */

public class SerialPolicy implements LoadPolicy {
    @Override
    public int compare(BitmapRequest thisBitmapRequest, BitmapRequest anotherBitmapRequest) {
        // 那么按照添加到队列的序列号顺序来执行
        return thisBitmapRequest.serialNum - anotherBitmapRequest.serialNum;


        //相当于：
//        if (thisBitmapRequest.serialNum < anotherBitmapRequest.serialNum) return -1;
//        else if (thisBitmapRequest.serialNum == anotherBitmapRequest.serialNum) return 0;
//        else if (thisBitmapRequest.serialNum > anotherBitmapRequest.serialNum) return 1;
    }
}
