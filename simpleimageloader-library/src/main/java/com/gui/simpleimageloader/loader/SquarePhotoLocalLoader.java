package com.gui.simpleimageloader.loader;

import android.graphics.Bitmap;

import com.gui.simpleimageloader.util.Constants;
import com.gui.simpleimageloader.util.ImageUtil;

import java.io.File;


/**
 * 正方形图片的加载者
 */
public class SquarePhotoLocalLoader extends AbsLoader {
    private String changeUri(String uri) {
        String newUri;
        if (uri.contains(Constants.SCHEMA_SQUARE_LOCAL + "://"))
            newUri = uri.split(Constants.SCHEMA_SQUARE_LOCAL + "://")[1];
        else
            newUri = uri;
        return newUri;
    }


    @Override
    Bitmap onLoadImage(String uri, int maxWidth, int maxHeight) {
        if (uri == null)
            return null;
        String fileLocalPath = changeUri(uri);
        if (fileLocalPath == null || fileLocalPath.equals(""))
            return null;
        File file = new File(fileLocalPath);
        if (!file.exists()) {
            return null;
        }
        Bitmap bitmap= ImageUtil.getSmallBitmap(fileLocalPath, maxWidth, maxHeight);
        if (bitmap != null && bitmap.getWidth() != bitmap.getHeight()) {
            return ImageUtil.cutBitmapToSquare(bitmap);
        }
        return bitmap;

    }
}
