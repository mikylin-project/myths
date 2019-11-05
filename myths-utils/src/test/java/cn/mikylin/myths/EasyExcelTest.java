package cn.mikylin.myths;

import cn.mikylin.myths.common.CollectionUtils;
import cn.mikylin.utils.EasyExcelUtils;
import lombok.Data;
import org.junit.jupiter.api.Test;
import java.util.List;

public class EasyExcelTest {

    @Data
    public static class She {
        private String a;
        private String b;
        private String c;
        private String d;
    }

    @Test
    public void testRead() {

        String path = "d:\\123.xls";

        List<Object> rows1 = EasyExcelUtils.read(path,null,She.class);
        for(Object o : rows1) {
            System.out.println(o);
        }

        List<She> rows2 = EasyExcelUtils.read(path,She.class);
        for(She o : rows2) {
            System.out.println(o);
        }
    }

    @Test
    public void testWrite() {

        She e1 = new She();
        e1.setA("111");
        e1.setB("222");
        e1.setC("333");
        e1.setD("444");

        List<She> l = CollectionUtils.newArrayList(e1);

        String path = "d:\\1234.xls";
        EasyExcelUtils.write(path,null,l);
    }
}
