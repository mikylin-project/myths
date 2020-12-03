package cn.mikylin.utils.cache.utils;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtils {

    public static byte[] read(RandomAccessFile file,int off,int len) {
        byte[] bs = new byte[len];
        try {
            file.read(bs,off,len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bs;
    }

    public static long write(RandomAccessFile file,byte[] bs) {
        try {
            file.write(bs);
            return file.length();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
