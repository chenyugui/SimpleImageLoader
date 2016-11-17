package com.gui.simpleimageloader.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Gui on 2016/1/10.
 * 图片工具
 */
public class ImageUtil {
    /**
     * 把bitmap切成正方形
     */
    public static Bitmap cutBitmapToSquare(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width == height) {
            return bitmap;
        }
        int xTopLeft = 0;
        int yTopLeft = 0;
        int size;
        if (width > height) {
            size = height;
            xTopLeft = (width - height) / 2;
        } else {
            size = width;
            yTopLeft = (height - width) / 2;
        }
        Bitmap bmResult = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, size, size);
        bitmap.recycle();
        return bmResult;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
    }

    /**
     * 描述:  根据输入流 获取 按比例压缩了的图片
     *
     * @param maxWidth  图片的最大宽度， 如果不压缩填小于0的值
     * @param maxHeight 图片的最大高度， 如果不压缩填小于0的值
     * @return 压缩后的bitmap
     */
    public static Bitmap getSmallBitmap(InputStream inputStream, int maxWidth, int maxHeight) {
        if (inputStream == null) {
            return null;
        }
        Bitmap bm = null;
        ByteArrayOutputStream baos = null;
        InputStream stream1 = null;
        InputStream stream2 = null;
        try {
            if (maxWidth <= 0 || maxHeight <= 0) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return bitmap;
            }
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            stream1 = new ByteArrayInputStream(baos.toByteArray());
            stream2 = new ByteArrayInputStream(baos.toByteArray());
            //解析图片时需要用到的参数都封装在这个对象里了
            BitmapFactory.Options opts = new BitmapFactory.Options();
            //为true的话，不为像素申请内存，只为获取图片宽高
            opts.inJustDecodeBounds = true;
            //因为为true，所以返回的Bitmap为null
            BitmapFactory.decodeStream(stream1, null, opts);

            //获取图片宽高
            float imgWid = opts.outWidth;
            float imgHei = opts.outHeight;
            //计算缩放比例
            float scale = 1;
            if ((imgWid > maxWidth && maxWidth > 0) || (imgHei > maxHeight && maxHeight > 0)) {
                float scaleWid = maxWidth <= 0 ? 1 : (imgWid / maxWidth);
                float scaleHei = maxHeight <= 0 ? 1 : (imgHei / maxHeight);
                if (scaleWid >= scaleHei) {
                    scale = scaleWid;
                } else if (scaleHei > scaleWid) {
                    scale = scaleHei;
                }
            }
            //设置缩放比例
            float yushu = scale % 1;
            scale = scale - yushu;
            if (yushu > 0.3)
                scale = scale + 1;
            opts.inSampleSize = (int) scale;
            //重新设为false，这才返回的bitmap才不会为NULL
            opts.inJustDecodeBounds = false;
            //注意这里的路径要跟上面的路径一致
            try {
                bm = BitmapFactory.decodeStream(stream2, null, opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (baos != null)
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (stream1 != null)
                try {
                    stream1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (stream2 != null)
                try {
                    stream2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return bm;
    }

    /**
     * 描述:  根据输入流 获取 按比例压缩了的图片
     *
     * @param maxWidth  图片的最大宽度， 如果不压缩填小于0的值
     * @param maxHeight 图片的最大高度， 如果不压缩填小于0的值
     * @return 压缩后的bitmap
     */
    public static Bitmap getSmallBitmap(String filePath, int maxWidth, int maxHeight) {
        if (filePath == null || filePath.equals("")) {
            return null;
        }
        if (maxWidth <= 0 || maxHeight <= 0) {
            return BitmapFactory.decodeFile(filePath);
        }

        Bitmap bm = null;
        //解析图片时需要用到的参数都封装在这个对象里了
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //为true的话，不为像素申请内存，只为获取图片宽高
        opts.inJustDecodeBounds = true;
        //因为为true，所以返回的Bitmap为null
        BitmapFactory.decodeFile(filePath, opts);
        //获取图片宽高
        float imgWid = opts.outWidth;
        float imgHei = opts.outHeight;
        //计算缩放比例
        float scale = 1;
        if ((imgWid > maxWidth && maxWidth > 0) || (imgHei > maxHeight && maxHeight > 0)) {
            float scaleWid = maxWidth <= 0 ? 1 : (imgWid / maxWidth);
            float scaleHei = maxHeight <= 0 ? 1 : (imgHei / maxHeight);
            if (scaleWid >= scaleHei) {
                scale = scaleWid;
            } else if (scaleHei > scaleWid) {
                scale = scaleHei;
            }
        }
        //设置缩放比例
        float yushu = scale % 1;
        scale = scale - yushu;
        if (yushu > 0.3)
            scale = scale + 1;
        opts.inSampleSize = (int) scale;
        //重新设为false，这才返回的bitmap才不会为NULL
        opts.inJustDecodeBounds = false;
        //注意这里的路径要跟上面的路径一致
        try {
            bm = BitmapFactory.decodeFile(filePath, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 描述:  从资源文件 获取 按比例压缩了的图片
     *
     * @param maxWidth  图片的最大宽度， 如果不压缩填小于0的值
     * @param maxHeight 图片的最大高度， 如果不压缩填小于0的值
     * @return 压缩后的bitmap
     */
    public static Bitmap getSmallBitmap(Context context, int resourceID, int maxWidth, int maxHeight) {
        if (resourceID <= 0) {
            return null;
        }
        if (maxWidth <= 0 || maxHeight <= 0) {
            return BitmapFactory.decodeResource(context.getResources(), resourceID);
        }

        Bitmap bm = null;
        //解析图片时需要用到的参数都封装在这个对象里了
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //为true的话，不为像素申请内存，只为获取图片宽高
        opts.inJustDecodeBounds = true;
        //因为为true，所以返回的Bitmap为null
        BitmapFactory.decodeResource(context.getResources(), resourceID, opts);
        //获取图片宽高
        float imgWid = opts.outWidth;
        float imgHei = opts.outHeight;
        //计算缩放比例
        float scale = 1;
        if ((imgWid > maxWidth && maxWidth > 0) || (imgHei > maxHeight && maxHeight > 0)) {
            float scaleWid = maxWidth <= 0 ? 1 : (imgWid / maxWidth);
            float scaleHei = maxHeight <= 0 ? 1 : (imgHei / maxHeight);
            if (scaleWid >= scaleHei) {
                scale = scaleWid;
            } else if (scaleHei > scaleWid) {
                scale = scaleHei;
            }
        }
        //设置缩放比例
        float yushu = scale % 1;
        scale = scale - yushu;
        if (yushu > 0.3)
            scale = scale + 1;
        opts.inSampleSize = (int) scale;
        //重新设为false，这才返回的bitmap才不会为NULL
        opts.inJustDecodeBounds = false;
        //注意这里的路径要跟上面的路径一致
        try {
            bm = BitmapFactory.decodeResource(context.getResources(), resourceID, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 保存bitmap到本地
     */
    public static boolean copyBitmapToSDCard(String dirPath, String fileName, Bitmap bitmap) {
        boolean b = false;
        FileOutputStream out;
        try {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dirPath, fileName);
            out = new FileOutputStream(file);
            if (bitmap != null && !bitmap.isRecycled()) {
                b = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } else {
                return false;
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
}
