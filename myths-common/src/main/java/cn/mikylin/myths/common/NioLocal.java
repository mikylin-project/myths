package cn.mikylin.myths.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * summarized by nio jdk7 Files
 *
 * @author mikylin
 * @date 20190701
 */
public final class NioLocal {

    /**
     * read the file to byte[].
     *
     * @param filePath  file path in local computer
     * @throws RuntimeException
     * @return bytes
     */
    public static byte[] fileToBytes(String filePath) {

        //验证传入的文件路径不能为空
        if(StringUtils.isBlank(filePath))
            throw new NullPointerException("path can not be blank");

        //获取路径封装对象 path
        Path path = getFilePath(filePath,false);

        //是否可以读取
        if(!Files.isReadable(path))
            throw new RuntimeException("file can not be read");

        //文件大小不能超过虚拟机空闲内存的大小，不然会 OOM
        try {
            if(Files.size(path) >= Runtime.getRuntime().freeMemory())
                throw new RuntimeException("file is too big to read for jvm's memory");
        } catch (IOException e) {
            throw new RuntimeException("get file size fail");
        }

        return fileToBytes(path);
    }

    /**
     * read the file to byte[].
     *
     * @param file  the file
     * @throws NullPointerException
     * @return bytes
     */
    public static byte[] fileToBytes(File file) {
        return fileToBytes(file.toPath());
    }

    /**
     * read the file to byte[].
     *
     * @param path  Path for file path
     * @throws NullPointerException
     * @return bytes
     */
    private static byte[] fileToBytes(Path path) {
        //读取文件
        try {
            return Files.readAllBytes(path);
        }catch (IOException e){
            throw new RuntimeException("file read failed");
        }
    }

    /**
     * write the byte[] to a new local file.
     * can not be writing continue.
     *
     * @param pathForFile  file path in local computer you want to wrtie.
     * @param bytes  file context ready to write.
     * @param isDeleteIfExists  if the file in file path is exists,
     *                          is delete origin and write the new one?
     * @throws RuntimeException
     * @throws NullPointerException
     */
    public static void createFile(String pathForFile,byte[] bytes,boolean isDeleteIfExists) {

        //验证传入的文件路径和要被写入的 byte[] 不能为空
        if(StringUtils.isBlank(pathForFile) || ArrayUtils.isBlank(bytes))
            throw new NullPointerException("file name or bytes can not be blank");

        Path filePath = getFilePath(pathForFile,isDeleteIfExists);

        //创建文件
        try {
            Files.createFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException("file create failed");
        }

        //写入文件
        try {
            Files.write(filePath,bytes);
        } catch (IOException e) {
            throw new RuntimeException("file write failed");
        }
    }

    /**
     * get the outputstream by file path.
     *
     * @param pathForFile  file path in local computer you want to wrtie
     * @param isDeleteIfExists  if the file in file path is exists,
     *                          is delete origin and write the new one?
     * @throws RuntimeException
     * @return out put stream
     */
    public static OutputStream outputStream(String pathForFile,boolean isDeleteIfExists)
            throws IOException {
        Path path = getFilePath(pathForFile,isDeleteIfExists);
        return Files.newOutputStream(path);
    }

    /**
     * get the inputstream by file path.
     *
     * @param pathForFile  file path in local computer you want to read
     * @throws RuntimeException
     * @return input stream
     */
    public static InputStream inputStream(String pathForFile) {

        Path path = getFilePath(pathForFile);
        try{
            return Files.newInputStream(path);
        }catch (IOException e){
            throw new RuntimeException("get file input stream exception.");
        }
    }

    /**
     * check and build the path.
     *
     * @param pathForFile  file path in local computer you want to wrtie
     * @param isDeleteIfExists  if the file in file path is exists,
     *                          is delete origin and write the new one?
     * @throws RuntimeException
     * @return path
     */
    public static Path getFilePath(String pathForFile,boolean isDeleteIfExists) {

        Path filePath = path(pathForFile);

        //该路径不能是一个目录
        if(Files.isDirectory(filePath))
            throw new RuntimeException("directory is not file.");

        //获取目录路径封装对象 dir path
        Path dirPath = filePath.getParent();

        //如果目录不存在，在此处创建目录
        if(Files.notExists(dirPath)) {
            //此处创建目录
            try {
                Files.createDirectories(dirPath);
            }catch (IOException e){
                throw new RuntimeException("directory create failed.");
            }
        } else {
            //目录是存在的
            //需要判断是否有同名文件存在于指定路径下，并且已经指定了参数要删除该文件重新写入
            //则此处删除该文件，反之则抛出异常
            if(Files.exists(filePath) && isDeleteIfExists)
                try{
                    Files.deleteIfExists(filePath);
                }catch (IOException e){
                    throw new RuntimeException("file delete failed.");
                }
        }
        return filePath;
    }

    public static Path getFilePath(String pathForFile) {
        return getFilePath(pathForFile,false);
    }

    /**
     * get the dir by string path
     *
     * @param pathForDir  os string path
     * @param isDeleteIfExists  is delete the dir if it is exist?
     * @throws RuntimeException
     * @return path
     */
    public static Path getDirPath(String pathForDir,boolean isDeleteIfExists) {

        Path dirPath = path(pathForDir);

        if(!Files.exists(dirPath))
            throw new RuntimeException("directory must be exist.");

        // 必须是一个目录
        if(!Files.isDirectory(dirPath))
            throw new RuntimeException("must be a directory.");

        if(!Files.isReadable(dirPath))
            throw new RuntimeException("directory must can read.");

        if(isDeleteIfExists)
            try{
                Files.deleteIfExists(dirPath);
            }catch (IOException e){
                throw new RuntimeException("dir delete failed.");
            }

        return dirPath;
    }

    /**
     * get the dir by string path
     *
     * @param pathForDir  os string path
     * @throws RuntimeException
     * @return path
     */
    public static Path getDirPath(String pathForDir) {
        return getDirPath(pathForDir,false);
    }

    /**
     * get the path object
     *
     * @param path  os string path
     * @throws RuntimeException
     * @return path
     */
    private static Path path(String path) {
        //验证传入的文件路径不能为空
        StringUtils.requireNotBlank(path,"path can not be blank.");
        //获取路径封装对象 path
        return Paths.get(path);
    }

}
