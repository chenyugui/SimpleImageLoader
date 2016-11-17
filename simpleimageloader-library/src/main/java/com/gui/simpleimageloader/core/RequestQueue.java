package com.gui.simpleimageloader.core;

import android.util.Log;

import com.gui.simpleimageloader.request.BitmapRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gui on 2016/11/9.
 * 请求队列
 */
public class RequestQueue {
    private static final String TAG = "RequestQueue";
    /**
     * 默认的线程数
     */
    private static int DEFAULT_CORE_NUMS = Runtime.getRuntime().availableProcessors() + 1;
    /**
     * 设置的线程数
     */
    private int threadCount;
    /**
     * 请求队列 [ Thread-safe ]
     */
    private BlockingQueue<BitmapRequest> mRequestQueue = new PriorityBlockingQueue<>();
    /**
     * NetworkExecutor,执行网络请求的线程
     */
    private RequestDispatcher[] mDispatchers = null;
    /**
     * 请求的序列化生成器
     */
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);

    protected RequestQueue() {
        this(DEFAULT_CORE_NUMS);
    }

    /**
     * 线程数
     *
     * @param threadCount
     */
    protected RequestQueue(int threadCount) {
        this.threadCount = threadCount;
    }

    public void start() {
        stop();
        startDispatchers();
    }

    public void startDispatchers() {
        //创建指定数量的线程并让它们不断从请求队列中获取请求去 执行加载，如果队列中没有请求了，则线程阻塞
        mDispatchers = new RequestDispatcher[threadCount];
        for (RequestDispatcher requestDispatcher : mDispatchers) {
            requestDispatcher = new RequestDispatcher(mRequestQueue);
            requestDispatcher.start();
        }
    }

    /**
     * 停止RequestDispatcher
     */

    public void stop() {
        if (mDispatchers != null && mDispatchers.length > 0) {
            for (int i = 0; i < mDispatchers.length; i++) {
                mDispatchers[i].interrupt();
            }
        }
    }

    public void addRequest(BitmapRequest bitmapRequest) {
        if (!mRequestQueue.contains(bitmapRequest)) {
            bitmapRequest.serialNum = mSerialNumGenerator.incrementAndGet();
            mRequestQueue.add(bitmapRequest);
        } else {
            Log.d(TAG, "队列中已经含有此请求 #");
        }
    }
}
