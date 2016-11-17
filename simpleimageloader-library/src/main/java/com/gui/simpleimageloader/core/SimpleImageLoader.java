package com.gui.simpleimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.gui.simpleimageloader.policy.LoadPolicy;
import com.gui.simpleimageloader.request.BitmapRequest;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gui on 2016/11/10.
 * 简单的图片加载器
 */

public class SimpleImageLoader {
    private static SimpleImageLoader simpleImageLoader;
    public MyHandler handler;
    private SimpleImageLoaderConfig simpleImageLoaderConfig;
    private RequestQueue requestQueue;
    private HashMap<String, DisplayCallBack> callBackList = new HashMap<>();
    /**
     * 是否已经初始化过了
     */
    private static boolean isInit;

    public static final String TAG = "SimpleImageLoader";
    public static final int SHOWIMAGE = 1;
    public static final int DOWNLOAD_FAIL = 2;


    public static class MyHandler extends Handler {
        private WeakReference<SimpleImageLoader> weak;

        public MyHandler(SimpleImageLoader simpleImageLoader) {
            weak = new WeakReference<>(simpleImageLoader);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOWIMAGE && msg.obj != null) {
                Map<String, Object> map = (Map) msg.obj;
                String uri = (String) map.get("uri");
                Bitmap bitmap = (Bitmap) map.get("bitmap");
                ImageView imageView = (ImageView) map.get("imageView");
                imageView.setImageBitmap(bitmap);
                weak.get().sendSuccessCallBack(uri, bitmap, imageView);
            } else if (msg.what == DOWNLOAD_FAIL && msg.obj != null) {
                Map<String, Object> map = (Map) msg.obj;
                String uri = (String) map.get("uri");
                String failMsg = (String) map.get("failMsg");
                Bitmap failBitmap = null;
                if (map.get("failBitmap") != null) {
                    failBitmap = (Bitmap) map.get("failBitmap");
                }
                ImageView imageView = (ImageView) map.get("imageView");
                if (failBitmap != null)
                    imageView.setImageBitmap(failBitmap);
                weak.get().sendFailCallBack(uri, imageView, failBitmap, failMsg);
            }
            super.handleMessage(msg);
        }
    }

    private SimpleImageLoader() {

    }

    /**
     * 获取SimpleImageLoader的实例
     */
    public static SimpleImageLoader getInstance() {
        if (simpleImageLoader == null) {
            synchronized (SimpleImageLoader.class) {
                if (simpleImageLoader == null) {
                    simpleImageLoader = new SimpleImageLoader();
                }
            }
        }
        return simpleImageLoader;
    }

    /**
     * 初始化图片加载器的配置，需初始化S
     */
    public void init() {
        if (!isInit) {
            //如果用户没有设置配置类，则使用默认的配置类
            if (simpleImageLoaderConfig == null) {
                simpleImageLoaderConfig = new SimpleImageLoaderConfig();
            }
            handler = new MyHandler(this);
            //把线程数设置给队列
            int threadCount = simpleImageLoaderConfig.threadCount;
            requestQueue = new RequestQueue(threadCount);


            //(看下加载策略能不能在loader里设置而不是SimpleImageLoader)


            requestQueue.start();
            isInit = true;
        }
    }

    /**
     * @param uri 要加载的图片的uri，如果是网络图片，请带上schema为http://或https://,如果是本地图片，请带上file://，也可自定义schema
     */
    public void displayImage(String uri, ImageView imageView) {
        int maxWidth = -1;
        int maxHeight = -1;
        if (imageView.getWidth() > 0)
            maxWidth = imageView.getWidth();
        if (imageView.getHeight() > 0)
            maxHeight = imageView.getHeight();
        displayImage(uri, imageView, maxWidth, maxHeight, null, null, null);
    }

    /**
     * @param uri 要加载的图片的uri，如果是网络图片，请带上schema为http://或https://,如果是本地图片，请带上file://，也可自定义schema
     */
    public void displayImage(String uri, ImageView imageView, int maxWidth, int maxHeight) {
        displayImage(uri, imageView, maxWidth, maxHeight, null, null, null);
    }

    /**
     * @param uri 要加载的图片的uri，如果是网络图片，请带上schema为http://或https://,如果是本地图片，请带上file://，也可自定义schema
     */
    public void displayImage(String uri, ImageView imageView, int maxWidth, int maxHeight, DisplayCallBack displayCallBack) {
        displayImage(uri, imageView, maxWidth, maxHeight, null, null, displayCallBack);
    }

    /**
     * @param uri 要加载的图片的uri，如果是网络图片，请带上schema为http://或https://,如果是本地图片，请带上file://，也可自定义schema
     */
    public void displayImage(String uri, ImageView imageView, int maxWidth, int maxHeight, LoadPolicy loadPolicy, DisplayCallBack displayCallBack) {
        displayImage(uri, imageView, maxWidth, maxHeight, loadPolicy, null, displayCallBack);
    }

    /**
     * @param uri 要加载的图片的uri，如果是网络图片，请带上schema为http://或https://,如果是本地图片，请带上file://，也可自定义schema
     */
    public void displayImage(String uri, ImageView imageView, int maxWidth, int maxHeight, LoadPolicy loadPolicy, DisplayConfig displayConfig, DisplayCallBack displayCallBack) {
        if (uri == null || uri.equals("") || !uri.contains(".")) {
            Log.e(TAG, "uri err");
            return;
        }
        if (displayCallBack != null) {
            callBackList.put(uri, displayCallBack);
        }
        if (loadPolicy == null)
            loadPolicy = simpleImageLoaderConfig.loadPolicy;
        //提交给线程池处理
        imageView.setTag(uri);
        BitmapRequest bitmapRequest = new BitmapRequest(uri, imageView, maxWidth, maxHeight, loadPolicy, displayConfig, displayCallBack);
        requestQueue.addRequest(bitmapRequest);
    }

    public void setSimpleImageLoaderConfig(SimpleImageLoaderConfig simpleImageLoaderConfig) {
        this.simpleImageLoaderConfig = simpleImageLoaderConfig;
    }


    private void sendSuccessCallBack(String uri, Bitmap bitmap, ImageView imageView) {
        DisplayCallBack callBack = callBackList.get(uri);
        if (callBack != null)
            callBack.onSuccess(uri, bitmap, imageView);
    }

    private void sendFailCallBack(String uri, ImageView imageView, Bitmap failBitmap, String failMsg) {
        DisplayCallBack callBack = callBackList.get(uri);
        if (callBack != null)
            callBack.onFail(uri, imageView, failBitmap, failMsg);
    }

    /**
     * 图片显示回调
     */
    public interface DisplayCallBack {
        void onSuccess(String uri, Bitmap bitmap, ImageView imageView);

        void onFail(String uri, ImageView imageView, Bitmap failBitmap, String failMsg);
    }
}
