package cn.mikylin.myths.litjson.write;

import cn.mikylin.myths.litjson.OptionBox;
import cn.mikylin.myths.litjson.handler.DateTypeHandler;
import cn.mikylin.myths.litjson.handler.StringTypeHandler;
import cn.mikylin.myths.litjson.util.Invokes;
import cn.mikylin.myths.litjson.util.JsonType;
import java.io.StringWriter;
import java.util.*;

/**
 * writer type manager
 * @author mikylin
 */
public class WriteManager {

    private OptionBox option;

    public WriteManager(OptionBox option){
        this.option = option;
    }

    protected void markBaseType(Object value, WriteObjectBuilder definition, int type){
        definition.setType(type);
        definition.setValue(value);
    }

    /**
     * type sorting
     */
    protected void typeSeletor(Object o,Class clazz,WriteObjectBuilder builder){
        if(o instanceof Collection)
            analyticCollection(o,builder);
        else if(o instanceof Map)
            analyticMap(o,builder);
        else if(o instanceof String)
            markBaseType(o,builder, JsonType.TYPE_STRING);
        else if(o instanceof Integer || clazz == int.class)
            markBaseType(o,builder,JsonType.TYPE_INT);
        else if(o instanceof Long || clazz == long.class)
            markBaseType(o,builder, JsonType.TYPE_LONG);
        else if(o instanceof Float || clazz == float.class)
            markBaseType(o,builder,JsonType.TYPE_FLOAT);
        else if(o instanceof Double || clazz == double.class)
            markBaseType(o,builder,JsonType.TYPE_DOUBLE);
        else if(o instanceof Date)
            markBaseType(o,builder,JsonType.TYPE_DATE);
        else if(o instanceof Object)
            analyticBean(o,builder);
    }





    /**
     * analytic collection
     */
    protected void analyticCollection(Object co, WriteObjectBuilder builder){

        builder.setType(JsonType.TYPE_COLLECTION);
        Collection col = (Collection)co;
        WriteObjectBuilder[] unders = new WriteObjectBuilder[col.size()];
        builder.setUnders(unders);
        builder.setValue(null);

        int index = 0;
        for(Object o : col){
            boolean tail = false;
            if(unders.length - 1 == index)
                tail = true;
            unders[index] = new WriteObjectBuilder(null,o,this,builder.getStair() + 1,tail,JsonType.TYPE_COLLECTION);
            index ++;
        }
    }

    /**
     * analytic map
     */
    protected void analyticMap(Object map, WriteObjectBuilder builder){
        builder.setType(JsonType.TYPE_OBJECT);
        Set<Map.Entry<String,Object>> es = ((Map)map).entrySet();
        WriteObjectBuilder[] unders = new WriteObjectBuilder[es.size()];
        builder.setUnders(unders);
        builder.setValue(null);

        int index = 0;
        for(Map.Entry<String,Object> entry : es){
            boolean tail = false;
            if(unders.length - 1 == index)
                tail = true;
            unders[index] = new WriteObjectBuilder(entry.getKey(),entry.getValue(),
                                        this,builder.getStair() + 1,tail,JsonType.TYPE_OBJECT);
            index ++;
        }
    }

    /**
     * analytic java bean
     */
    protected void analyticBean(Object bean, WriteObjectBuilder builder) {
        Map<String,Object> map = Invokes.beanToMap(bean);
        analyticMap(map,builder);
    }


    /**
     * write string
     */
    protected void writerString(WriteObjectBuilder builder, StringWriter writer){
        checkOrigin(builder,writer);
        StringTypeHandler stringTypeHandler = (StringTypeHandler) option.typeHandlers.get(String.class);
        writer.append(option.quotationMarks)
                .append(stringTypeHandler.write(builder.getValue().toString()))
                .append(option.quotationMarks);
        checkTail(builder,writer);
    }

    /**
     * write number
     */
    protected void writerNumber(WriteObjectBuilder builder, StringWriter writer){
        checkOrigin(builder,writer);
        StringTypeHandler stringTypeHandler = (StringTypeHandler) option.typeHandlers.get(String.class);
        writer.append(stringTypeHandler.write(builder.getValue().toString()));
        checkTail(builder,writer);
    }

    /**
     * write date
     */
    protected void writerDate(WriteObjectBuilder builder, StringWriter writer){
        checkOrigin(builder,writer);
        DateTypeHandler dateTypeHandler = (DateTypeHandler) option.typeHandlers.get(Date.class);
        writer.append(dateTypeHandler.write((Date)builder.getValue()));
        checkTail(builder,writer);
    }

    /**
     * write null
     */
    protected void writerNull(WriteObjectBuilder builder, StringWriter writer){
        checkOrigin(builder,writer);
        writer.append("null");
        checkTail(builder,writer);
    }


    /**
     * check the origin
     */
    protected void checkOrigin(WriteObjectBuilder handler, StringWriter writer){
        if(!handler.isOrigin() && handler.getParentType() != JsonType.TYPE_COLLECTION){
            writer.append(option.quotationMarks)
                    .append(handler.getKey())
                    .append(option.quotationMarks)
                    .append(option.colon);
        }
    }

    /**
     * check the tail
     */
    protected void checkTail(WriteObjectBuilder handler, StringWriter writer){
        if(!handler.isTail())
            writer.append(option.comma);
    }


    protected void distribute(WriteObjectBuilder builder,StringWriter writer){
        int type = builder.getType();

        if(type == JsonType.TYPE_COLLECTION || type == JsonType.TYPE_OBJECT){
            switch (type){
                case JsonType.TYPE_OBJECT :
                    writeObject(builder,writer);
                    break;
                case JsonType.TYPE_COLLECTION :
                    writeArray(builder,writer);
                    break;
            }
        } else if(!Objects.isNull(builder.getValue())){
            switch (type){
                case JsonType.TYPE_STRING :
                    writerString(builder,writer);
                    break;
                case JsonType.TYPE_LONG :
                    writerNumber(builder,writer);
                    break;
                case JsonType.TYPE_INT:
                    writerNumber(builder,writer);
                    break;
                case JsonType.TYPE_DOUBLE :
                    writerNumber(builder,writer);
                    break;
                case JsonType.TYPE_FLOAT :
                    writerNumber(builder,writer);
                    break;
                case JsonType.TYPE_DATE :
                    writerDate(builder,writer);
                    break;
                default :
            }
        }else{
            writerNull(builder,writer);
        }
    }

    /**
     * serialize a object
     */
    private void writeObject(WriteObjectBuilder builder, StringWriter writer){
        checkOrigin(builder,writer);
        writer.append(option.openCurly);
        if(builder.hasUnders()){
            WriteObjectBuilder[] underdefinitions = builder.getUnders();
            for(WriteObjectBuilder b : underdefinitions)
                distribute(b,writer);
        }
        writer.append(option.closeCurly);
        checkTail(builder,writer);
    }

    /**
     * serialize a array
     */
    private void writeArray(WriteObjectBuilder definition, StringWriter writer){
        checkOrigin(definition,writer);
        writer.append(option.openSquareBrackets);
        if(definition.hasUnders()){
            WriteObjectBuilder[] underdefinitions = definition.getUnders();
            for(WriteObjectBuilder b : underdefinitions)
                distribute(b,writer);
        }
        writer.append(option.closeSquareBrackets);
        checkTail(definition,writer);
    }
}
