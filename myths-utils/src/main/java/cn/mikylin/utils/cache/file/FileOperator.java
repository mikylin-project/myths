package cn.mikylin.utils.cache.file;

/**
 * file operator.
 *
 * @author mikylin
 * @date 20201203
 */
public interface FileOperator {

    String getFilePath();

    byte[] read(long off,int len);

    long write(byte[] o);
}
