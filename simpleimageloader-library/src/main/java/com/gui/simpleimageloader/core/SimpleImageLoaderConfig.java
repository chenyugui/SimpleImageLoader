package com.gui.simpleimageloader.core;

import com.gui.simpleimageloader.policy.LoadPolicy;
import com.gui.simpleimageloader.policy.ReversePolicy;

/**
 * Created by gui on 2016/11/10.
 * 图片加载器的配置类
 */

public class SimpleImageLoaderConfig {
    /**
     * 线程数
     */
    public int threadCount = Runtime.getRuntime().availableProcessors() + 1;
    public LoadPolicy loadPolicy = new ReversePolicy();

    //加载策略
    //图片缓存

    public SimpleImageLoaderConfig setThreadCount(int threadCount) {
        this.threadCount = Math.max(1, threadCount);
        return this;
    }

    public SimpleImageLoaderConfig setLoadPolicy(LoadPolicy loadPolicy) {
        if (loadPolicy != null) {
            this.loadPolicy = loadPolicy;
        }
        return this;
    }
}
