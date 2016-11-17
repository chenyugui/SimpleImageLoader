package com.gui.simpleimageloader.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by gui on 2016/11/11.
 */

public class FileUtil {
    /**
     * 根据文件全路径提取文件名
     */
    public static String getFileNameByFilePath(String filePath) {
        String fileName = null;
        if (filePath != null && !filePath.equals("")) {
            fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        }
        return fileName;
    }

    public static String getDirByFilePath(String filePath) {
        String dir = null;
        if (filePath != null && !filePath.equals("")) {
            dir = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        }
        return dir;
    }

    /**
     * 创建文件夹
     *
     * @param dirPath
     * @return
     * @throws IOException
     */
    public static File createDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean b=dir.mkdirs();
        }
        return dir;
    }
}
