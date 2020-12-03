package cn.mikylin.utils.cache.file;

/**
 * file operator.
 *
 * @author mikylin
 * @date 20201203
 */
public interface FileOperator {

    String getFilePath();

    byte[] read(int index);

    int writeObject(Object o);
}
