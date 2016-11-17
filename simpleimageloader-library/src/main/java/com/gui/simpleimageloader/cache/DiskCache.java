package com.gui.simpleimageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.gui.simpleimageloader.util.FileUtil;
import com.gui.simpleimageloader.util.ImageUtil;

import java.io.File;

/**
 * Created by gui on 2016/11/3.
 * 本地缓存类<br>
 * 保存到本地时，路径为dirPath变量的值（未设置的话文件夹则为SD卡下的cache文件夹）+url的文件名
 */

public class DiskCache implements ImageCache {
    private Context context;
    private String dirPath;


    public DiskCache(Context context) {
        this.context = context;
    }

    @Override
    public void put(String uri, Bitmap bitmap) {
        String fileName = FileUtil.getFileNameByFilePath(uri);
        if (dirPath == null || dirPath.equals("")) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                dirPath = context.getExternalCacheDir().getPath();
            } else {
                dirPath = context.getCacheDir().getPath();
            }
        }
        ImageUtil.copyBitmapToSDCard(dirPath, fileName, bitmap);
    }

    @Override
    public Bitmap get(String uri, int maxWidth, int maxHeight) {
        if (uri == null || uri.equals(""))
            return null;
        String filename = FileUtil.getFileNameByFilePath(uri);
        if (dirPath == null || dirPath.equals("")) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                dirPath = context.getExternalCacheDir().getPath();
            } else {
                dirPath = context.getCacheDir().getPath();
            }
        }
        if (dirPath.length() > 0 && !dirPath.endsWith("/")) {
            dirPath = dirPath + "/";
        }
        File file = new File(dirPath + filename);
        if (!file.exists())
            return null;
        return ImageUtil.getSmallBitmap(dirPath + filename, maxWidth, maxHeight);
    }

    @Override
    public void remove(String uri) {

    }

    /**
     * 设置图片本地存储的文件夹路径
     */
    public void setDirPath(String dirPath) {
        FileUtil.createDir(dirPath);
        this.dirPath = dirPath;
    }
}
