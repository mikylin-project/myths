package cn.mikylin.utils.cache.file;

import cn.mikylin.utils.cache.common.NonBlockingPool;
import cn.mikylin.utils.cache.common.ObjectPool;
import cn.mikylin.utils.cache.common.OnePool;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

/**
 * seek file operator.
 *
 * @author mikylin
 * @date 20201203
 */
public class RandomAccessFileOperator implements FileOperator {

    private String filePath;

    private ObjectPool<RandomAccessFile> readers;
    private ObjectPool<RandomAccessFile> writers;

    public RandomAccessFileOperator(String filePath) {
        this(filePath,20);
    }

    public RandomAccessFileOperator(String filePath,Integer readerSize) {
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.filePath = filePath;
        this.readers = new NonBlockingPool<>(readerSize, () -> create(filePath,"r"));
        this.writers = new OnePool<>(create(filePath,"rw"));

    }


    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public byte[] read(long off,int len) {
        RandomAccessFile file = null;
        try {
            file = readers.getObject();
            return read(file, off, len);
        } finally {
            readers.returnObject(file);
        }
    }

    @Override
    public long write(byte[] bs) {
        RandomAccessFile file = null;
        try {
            file = writers.getObject();
            return writeInEnd(file,bs);
        } finally {
            writers.returnObject(file);
        }
    }



    private static RandomAccessFile create(String filePath,String mode) {
        try {
            RandomAccessFile r = new RandomAccessFile(filePath,mode);

            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





    private static byte[] read(RandomAccessFile file,long off,int len) {
        byte[] bs = new byte[len];
        try {
            file.seek(off);
            file.read(bs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bs;
    }

    private static long writeInEnd(RandomAccessFile file,byte[] bs) {
        try {
            long begin = file.length();
            file.seek(begin);
            file.write(bs);
            return begin;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
