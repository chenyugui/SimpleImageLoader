package com.gui.simpleimageloader.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.gui.simpleimageloader.loader.Loader;
import com.gui.simpleimageloader.loader.LoaderManager;
import com.gui.simpleimageloader.request.BitmapRequest;
import com.gui.simpleimageloader.util.ImageUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static com.gui.simpleimageloader.core.SimpleImageLoader.DOWNLOAD_FAIL;
import static com.gui.simpleimageloader.core.SimpleImageLoader.SHOWIMAGE;


/**
 * Created by gui on 2016/11/10.
 * 图片加载请求的执行者
 */

public class RequestDispatcher extends Thread {
    private BlockingQueue<BitmapRequest> requestQueue;
    private final String TAG = "RequestDispatcher";

    public RequestDispatcher(BlockingQueue<BitmapRequest> requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        try {
            while (!this.isInterrupted()) {
                //从请求队列中获取一个顶部的请求出来
                final BitmapRequest request = requestQueue.take();
                String uri = request.uri;
                ImageView imageView = request.imageView;
                int maxWidth = request.maxWidth;
                int maxHeight = request.maxHeight;

                boolean isSuccess = false;
                Bitmap bitmap = null;
                Bitmap failBitmap = null;
                String errMsg = null;
                // 解析图片schema
                String schema = getSchema(request.uri);
                if (schema != null) {
                    Loader loader = LoaderManager.getInstance().getLoader(schema);
                    if (loader == null) {//找不到对应的Loader
                        errMsg = "### Could not find the corresponding loader, please input the right schema or register the loader. ";
                        Log.e(TAG, "### Could not find the corresponding loader, please input the right schema or register the loader. ");
                    } else {
                        bitmap = loader.loadImage(uri, imageView, maxWidth, maxHeight);
                        if (imageView.getTag() != null && imageView.getTag().equals(uri)) {
                            if (bitmap == null) {
                                errMsg = "cannot load bitmap";
                            } else {
                                isSuccess = true;
                            }
                        }
                    }
                } else {
                    errMsg = "schema is null";
                }

                if (isSuccess) {
                    sendShowImageMsg(uri, imageView, bitmap);
                } else {
                    //显示失败的图片
                    DisplayConfig displayConfig = request.displayConfig;
                    if (displayConfig != null) {
                        int resourceID = displayConfig.resourceID;
                        Context context = displayConfig.context;
                        if (resourceID > 0 && context != null) {
                            failBitmap = ImageUtil.getSmallBitmap(context, resourceID, maxWidth, maxHeight);
                        }
                    }
                    sendFailMsg(uri, imageView, failBitmap, errMsg);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.run();
    }

    private String getSchema(String uri) {
        if (uri == null || !uri.contains("://")) {
            Log.e(TAG, "### wrong schema, image uri is : " + (uri == null ? "null" : uri));
        } else {
            return uri.split("://")[0];
        }
        return null;
    }

    private void sendFailMsg(String uri, ImageView imageView, Bitmap failBitmap, String failMsg) {
        Map<String, Object> map = new HashMap<>();
        map.put("uri", uri);
        map.put("imageView", imageView);
        map.put("failBitmap", failBitmap);
        map.put("failMsg", failMsg);
        Message msg = new Message();
        msg.what = DOWNLOAD_FAIL;
        msg.obj = map;
        SimpleImageLoader.getInstance().handler.sendMessage(msg);
    }

    private void sendShowImageMsg(String uri, ImageView imv, Bitmap bitmap) {
        Map<String, Object> map = new HashMap<>();
        map.put("uri", uri);
        map.put("bitmap", bitmap);
        map.put("imageView", imv);
        Message msg = new Message();
        msg.what = SHOWIMAGE;
        msg.obj = map;
        SimpleImageLoader.getInstance().handler.sendMessage(msg);
    }

}
