package cn.mikylin.utils.cache.file;

import cn.mikylin.utils.cache.common.NonBlockingPool;
import cn.mikylin.utils.cache.common.ObjectPool;
import cn.mikylin.utils.cache.common.SynchronousPool;
import cn.mikylin.utils.cache.utils.FileUtils;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * seek file operator.
 *
 * @author mikylin
 * @date 20201203
 */
public class RandomAccessFileOperator<T> implements FileOperator {

    private String filePath;

    private ObjectPool<RandomAccessFile> readers;
    private ObjectPool<RandomAccessFile> writers;

    public RandomAccessFileOperator(String filePath,Integer readerSize)
            throws FileNotFoundException {
        this.filePath = filePath;
        this.readers = new NonBlockingPool<>(readerSize, () -> create(filePath,"r"));
        this.writers = new SynchronousPool<>(create(filePath,"rw"));

    }


    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public byte[] read(int off,int len) {
        RandomAccessFile file = null;
        try {
            file = readers.getObject();
            return FileUtils.read(file, off, len);
        } finally {
            readers.returnObject(file);
        }
    }

    @Override
    public long write(byte[] bs) {
        RandomAccessFile file = null;
        try {
            return FileUtils.write(file,bs);
        } finally {
            writers.returnObject(file);
        }
    }



    private static RandomAccessFile create(String filePath,String mode) {
        try {
            return new RandomAccessFile(filePath,mode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
