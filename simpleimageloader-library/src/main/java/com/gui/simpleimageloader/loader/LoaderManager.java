package com.gui.simpleimageloader.loader;


import com.gui.simpleimageloader.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gui on 2016/11/10.<br>
 * 图片获取者（Loader）的管理者。<br>
 * 默认注册了UrlLoader（schema为http或https）和LocalLoader（schema为file）
 */

public class LoaderManager {
    private static LoaderManager loaderManager;
    private Map<String, Loader> loaderMap = new HashMap<>();

    private LoaderManager() {
        UrlLoader urlLoader = new UrlLoader();
        registerLoader(Constants.SCHEMA_FILE, new LocalLoader());
        registerLoader(Constants.SCHEMA_HTTP, urlLoader);
        registerLoader(Constants.SCHEMA_HTTPS, urlLoader);
    }

    public static LoaderManager getInstance() {
        if (loaderManager == null) {
            synchronized (LoaderManager.class) {
                if (loaderManager == null) {
                    loaderManager = new LoaderManager();
                }
            }
        }
        return loaderManager;
    }

    public Loader getLoader(String schema) {
        return loaderMap.get(schema);
    }

    public void registerLoader(String schema, Loader loader) {
        if (!loaderMap.containsKey(schema)) {
            loaderMap.put(schema, loader);
        }
    }
}
