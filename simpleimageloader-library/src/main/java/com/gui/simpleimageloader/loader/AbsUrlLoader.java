package com.gui.simpleimageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gui.simpleimageloader.util.ImageUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


abstract class AbsUrlLoader extends AbsLoader {
    @Override
    Bitmap onLoadImage(String uri, int maxWidth, int maxHeight) {
        Bitmap bitmap = null;
        try {
            String newUri = changeUri(uri);
            if (newUri == null)
                return null;
            URL url = new URL(newUri);
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

    abstract String changeUri(String uri);
}
