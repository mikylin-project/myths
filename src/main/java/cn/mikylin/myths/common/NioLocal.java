package cn.mikylin.myths.common;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * summarized by nio jdk7 Files
 * @author mikylin
 * @date 20190701
 */
public final class NioLocal {

    /**
     * read the file to byte[].
     * @Param filePath  file path in local computer
     */
    public static byte[] fileToBytes(String filePath){

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
        }catch (IOException e){
            throw new RuntimeException("get file size fail");
        }

        return fileToBytes(path);
    }

    /**
     * read the file to byte[].
     * @Param file  the file
     */
    public static byte[] fileToBytes(File file){
        return fileToBytes(file.toPath());
    }

    /**
     * read the file to byte[].
     * @Param path  Path for file path
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
     * @Param filePath  file path in local computer you want to wrtie
     * @Param bytes  file context to be writer
     * @Param isDeleteIfExists  if the file in file path is exists,is delete origin and write the new one?
     */
    public static void newLocalFile(String pathForFile,byte[] bytes,boolean isDeleteIfExists){

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
        }catch (IOException e){
            throw new RuntimeException("file write failed");
        }
    }

    /**
     * get the outputstream by file path.
     * @Param filePath  file path in local computer you want to wrtie
     * @Param isDeleteIfExists  if the file in file path is exists,is delete origin and write the new one?
     */
    public static OutputStream outputStream(String pathForFile,boolean isDeleteIfExists) throws IOException {
        Path path = getFilePath(pathForFile,isDeleteIfExists);
        return Files.newOutputStream(path);
    }

    /**
     * check and build the path.
     * @Param filePath  file path in local computer you want to wrtie
     * @Param isDeleteIfExists  if the file in file path is exists,is delete origin and write the new one?
     */
    private static Path getFilePath(String pathForFile,boolean isDeleteIfExists){

        //验证传入的文件路径不能为空
        if(StringUtils.isBlank(pathForFile))
            throw new RuntimeException("path can not be blank");

        //获取路径封装对象 file path
        Path filePath = FileSystems.getDefault().getPath(pathForFile);

        //该路径不能是一个目录
        if(Files.isDirectory(filePath))
            throw new RuntimeException("directory is not file");

        //获取目录路径封装对象 dir path
        Path dirPath = filePath.getParent();

        //如果目录不存在，在此处创建目录
        if(Files.notExists(dirPath)) {
            //此处创建目录
            try {
                Files.createDirectories(dirPath);
            }catch (IOException e){
                throw new RuntimeException("directory create failed");
            }
        } else {
            //目录是存在的
            //需要判断是否有同名文件存在于指定路径下，并且已经指定了参数要删除该文件重新写入
            //则此处删除该文件，反之则抛出异常
            if(Files.exists(filePath)){
                if(isDeleteIfExists){
                    try{
                        Files.deleteIfExists(filePath);
                    }catch (IOException e){
                        throw new RuntimeException("file delete failed");
                    }
                }else{
                    throw new RuntimeException("file is exits in the file path");
                }
            }
        }
        return filePath;
    }

}
