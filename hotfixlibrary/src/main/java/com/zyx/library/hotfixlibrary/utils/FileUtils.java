package com.zyx.library.hotfixlibrary.utils;

import java.io.*;

/**
 * @author pielan
 * @date 7/3/19 上午11:23
 * @dec
 */
public class FileUtils {

    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {

        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        //新建文件输出流并对他进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuffer = new BufferedOutputStream(output);

        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuffer.write(b, 0, len);
        }

        // 刷新此缓冲的输出流
        outBuffer.flush();

        // 关闭源
        inBuff.close();
        outBuffer.close();
        output.close();
        input.close();

    }
}
