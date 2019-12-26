package cn.mikylin.utils;

import cn.mikylin.myths.common.CollectionUtils;
import cn.mikylin.myths.common.lang.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.sun.istack.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * EasyExcel 的封装类
 *
 * @author mikylin
 * @date 20191105
 */
public class EasyExcelUtils {

    /**
     * 从本地的 excel 中读取数据
     *
     * @param filePath 文件路径
     * @return 读取的行数据
     */
    public static List<Object> read(@NotNull String filePath) {
        return read(filePath, null,null);
    }

    /**
     * 从本地的 excel 中读取数据
     *
     * @param filePath 文件路径
     * @param header 头部的标题
     * @return 读取的行数据
     */
    public static <T> List<T> read(@NotNull String filePath, @NotNull Class<T> header) {

        Objects.requireNonNull(filePath,"header class can not be null.");

        List<Object> objs = read(filePath, null, header);
        List<T> list = CollectionUtils.newArrayList();
        objs.forEach(o -> list.add((T)o));
        return list;
    }

    /**
     * 从本地的 excel 中读取数据
     *
     * @param filePath 文件路径
     * @param sheet 文件簿编号
     * @param header 头部的标题
     * @return 读取的行数据
     */
    public static List<Object> read(@NotNull String filePath,ReadSheet sheet,Class header) {

        StringUtils.requireNotBlank(filePath);

        return EasyExcel.read(filePath,header,null)
                        .sheet(sheet != null ? sheet.getSheetNo() : 0)
                        .doReadSync();
    }

    /**
     * 写入本地 excel
     *
     * @param filePath 文件路径
     * @param sheet 文件簿编号
     * @param data 要写入的数据
     */
    public static <T> void write(@NotNull String filePath,WriteSheet sheet,@NotNull List<T> data) {

        CollectionUtils.requireNotBlank(data);

        EasyExcel.write(filePath)
                .sheet(sheet != null ? sheet.getSheetNo() : 0)
                .head(data.get(0).getClass())
                .doWrite(data);
    }
}