package cn.mikylin.myths.litjson;

import cn.mikylin.myths.litjson.read.JReader;
import cn.mikylin.myths.litjson.read.ReadManager;
import cn.mikylin.myths.litjson.write.JWriter;
import cn.mikylin.myths.litjson.write.WriteManager;
import java.util.List;
import java.util.Objects;

/**
 * bootstrap for jwriter and jreader
 * @author mikylin
 */
public final class JSONBootstrap {

    private static ReadManager readManager;
    private static WriteManager writeManager;

    public static <T> String write(T t){
        if(Objects.isNull(readManager))
            writeManager = initSimpleWriteManage();
        return new JWriter(t,writeManager).toJson();
    }

    public static <T> T read(String json,Class<T> type){
        if(null == readManager)
            readManager = initSimpleReadManage();
        Object obj = new JReader(json,type,false,readManager).toObj();
        return (T)obj;
    }

    public static <T> List<T> readArray(String json, Class<T> type){
        if(null == readManager)
            readManager = initSimpleReadManage();
        List l = new JReader(json,type,true,readManager).toArray();
        return (List<T>)l;
    }

    private static ReadManager initSimpleReadManage() {
        return new ReadManager(OptionBox.OptionBoxBuilder.builder().over());
    }

    private static WriteManager initSimpleWriteManage() {
        return new WriteManager(OptionBox.OptionBoxBuilder.builder().over());
    }
}
