package cn.mikylin.myths.litjson.read;

import cn.mikylin.myths.litjson.OptionBox;
import cn.mikylin.myths.litjson.util.JsonType;

import java.util.List;
import java.util.Objects;

/**
 * Json String reader
 * @author mikylin
 */
public class JReader {

    // chars
    private ReadCharBuffer buffer;
    // read json string to a object
    private ReadObjectBuilder objectBuilder;
    // read json string to a array
    private ReadArrayBuilder arrayBuilder;

    public JReader(String json,
                   Class clz,
                   boolean isArray,
                   ReadManager manager){

        Objects.requireNonNull(json, "json can not be null");

        buffer = new ReadCharBuffer(json.toCharArray());

        if(isArray){
            arrayBuilder = new ReadArrayBuilder(buffer,clz,null,manager);
        } else{
            int buildType;
            if(Objects.isNull(clz))
                buildType = JsonType.TYPE_MAP;
            else
                buildType = JsonType.TYPE_OBJECT;

            objectBuilder = new ReadObjectBuilder(buffer,null,clz,buildType,manager);
        }
    }

    /**
     * json string to map
     */
    public JReader(String json){
        this(json,
            null,
            false,
            new ReadManager(OptionBox.OptionBoxBuilder.builder().over()));
    }

    /**
     * json string to java bean
     */
    public JReader(String json,Class clazz){
        this(json,
            clazz,
            false,
            new ReadManager(OptionBox.OptionBoxBuilder.builder().over()));
    }

    /**
     * json string to java bean list
     */
    public JReader(String json,Class clazz,boolean isArray){
        this(json,
            clazz,
            isArray,
            new ReadManager(OptionBox.OptionBoxBuilder.builder().over()));
    }

    /**
     * push object
     */
    public Object toObj(){
        if(Objects.nonNull(objectBuilder))
            return objectBuilder.getObject();
        return null;
    }

    /**
     * push array
     */
    public List toArray(){
        if(Objects.nonNull(arrayBuilder))
            return arrayBuilder.getArray();
        return null;
    }
}