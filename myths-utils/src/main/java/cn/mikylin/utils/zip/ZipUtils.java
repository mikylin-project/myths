package cn.mikylin.utils.zip;

import cn.mikylin.myths.common.ArrayUtils;
import cn.mikylin.myths.common.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.zeroturnaround.zip.ZipUtil;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ZipUtils {


    /**
     * 将当前文件替换掉 zip 包中的文件
     * @param path              zip 文件路径
     * @param file              需替换的文件名称
     * @param content             替换的内容
     * @param deleteOld         是否删除原来的 zip 文件
     * @throws IOException      IOException
     */
    public static void replaceZip(String path, String file, byte[] content, boolean deleteOld) throws IOException {
        if (StringUtils.isBlank(path))
            throw new IOException("zip 包路径不能为空");
        else if (StringUtils.isBlank(file))
            throw new IOException("文件路劲更不能为空");
        else if (ArrayUtils.isBlank(content))
            throw new IOException("被替换的Zip文件内容不能为空");


        File zip = new File(path);

        // 替换文件
        File newZip = new File(zip.getParent() + File.separator + System.currentTimeMillis() + ".zip");
        boolean r = ZipUtil.replaceEntry(zip, file, content, newZip);
        log.info("zip 文件" + (r ? "替换成功" : "替换失败"));

        if (r && deleteOld) {
            boolean isdeleted = zip.delete();
            log.info(isdeleted ? "原文件已被删除" : "源文件删除失败");
        }
    }

    /**
     * 读取文件内容
     * @param path          zip 文件路径
     * @param file          需读取文件路径的
     * @return              文件内容
     */
    public static String readZip(String path, String file) {
        return new String(ZipUtil.unpackEntry(new File(path), file));
    }

}