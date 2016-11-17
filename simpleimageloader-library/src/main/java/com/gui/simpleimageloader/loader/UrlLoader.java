package com.gui.simpleimageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gui.simpleimageloader.util.ImageUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by gui on 2016/11/10.
 * 网络图片获取者（schema为http:// 或 https:// ）
 */

public class UrlLoader extends AbsLoader {
    /**
     * 下载网络图片
     */
    @Override
    Bitmap onLoadImage(String uri, int maxWidth, int maxHeight) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();
            if (maxWidth == -1 || maxHeight == -1) {
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } else {
                bitmap = ImageUtil.getSmallBitmap(inputStream, maxWidth, maxHeight);
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
