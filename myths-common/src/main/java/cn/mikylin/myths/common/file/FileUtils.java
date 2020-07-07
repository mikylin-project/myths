package cn.mikylin.myths.common.file;

import cn.mikylin.myths.common.lang.ArrayUtils;
import cn.mikylin.myths.common.lang.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * file utils for java.io.File.
 *
 * @author mikylin
 * @date 20200707
 */
public final class FileUtils {

    /**
     * clean the directory.
     *
     * @param path  dir path
     * @return is success ?
     */
    public static boolean cleanDir(String path) {

        StringUtils.requireNotBlank(path);

        File file = new File(path);
        if(!file.isDirectory() || !file.exists())
            return false;

        String[] content = file.list(); //取得当前目录下所有文件和文件夹
        if(ArrayUtils.isBlank(content))
            return true;

        for (String name : content) {
            File temp = new File(path,name);

            //判断是否是目录
            if (temp.isDirectory())
                cleanDir(temp.getAbsolutePath()); //递归调用，删除目录里的内容
            temp.delete();
        }
        return true;
    }

    /**
     * delete the file.
     *
     * @param f  file
     * @return is success ?
     */
    public static boolean deleteFile(File f) {
        return (f != null && f.isFile() && f.exists()) ? f.delete() : false;
    }

    public static boolean deleteFile(String filePath) {
        if(StringUtils.isBlank(filePath))
            return false;
        return deleteFile(new File(filePath));
    }


    /**
     * 检查目录是否存在，不存在则创建
     */
    public static boolean checkFolder(File dirFile) {

        if(dirFile.exists())
            return true;
        if(!dirFile.canRead())
            return false;

        try {
            dirFile.mkdirs();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean rename(String path,String oldName,String newName) {

        if(StringUtils.isBlank(path)
                || StringUtils.isBlank(oldName)
                || StringUtils.isBlank(newName))
            return false;

        if(!path.endsWith(File.separator))
            path = path + File.separator;

        File f = new File(path + oldName);
        if (f.exists() && f.isFile()) {
            File newF = new File(path + newName);
            f.renameTo(newF);
            return true;
        }
        return false;
    }

    public static void copy(String srcPathStr,String desPathStr) {
        try (FileInputStream fis = new FileInputStream(srcPathStr);
             FileOutputStream fos = new FileOutputStream(desPathStr)) {
            byte[] datas = new byte[1024 * 8];
            int len; //创建长度
            while((len = fis.read(datas)) != -1)
                fos.write(datas,0,len);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
