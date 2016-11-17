package com.gui.simpleimageloader.loader;

import android.graphics.Bitmap;

import com.gui.simpleimageloader.util.ImageUtil;

import java.io.File;

/**
 * Created by gui on 2016/11/10.
 * 本地图片的获取者
 */

public class LocalLoader extends AbsLoader {
    @Override
    Bitmap onLoadImage(String uri, int maxWidth, int maxHeight) {
        if (uri == null)
            return null;
        String fileLocalPath;
        if (uri.contains("file://")) {
            fileLocalPath = uri.split("file://")[1];
        } else {
            fileLocalPath = uri;
        }
        if (fileLocalPath == null || fileLocalPath.equals(""))
            return null;
        File file = new File(fileLocalPath);
        if (!file.exists()) {
            return null;
        }
        return ImageUtil.getSmallBitmap(fileLocalPath, maxWidth, maxHeight);
    }
}
