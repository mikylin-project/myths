package cn.mikylin.myths.litjson.read;

import cn.mikylin.myths.common.CollectionUtils;
import cn.mikylin.myths.common.Constants;
import cn.mikylin.myths.common.MapUtils;
import cn.mikylin.myths.litjson.OptionBox;
import cn.mikylin.myths.litjson.exception.JSONCharException;
import cn.mikylin.myths.litjson.util.Invokes;
import cn.mikylin.myths.litjson.util.JsonAction;
import cn.mikylin.myths.litjson.util.JsonType;
import java.util.List;
import java.util.Objects;

public class ReadArrayBuilder {

    private List list;

    private ReadCharBuffer chars;

    //control type
    private int controlType;

    //tache type
    private int readTache;

    //object class
    private Class clazz;

    //value
    private char[] value;

    private ReadManager manager;

    private OptionBox option;

    protected ReadArrayBuilder(ReadCharBuffer buffer,
                               Class clz,
                               List parseList,
                               ReadManager manager){
        chars = buffer;
        clazz = clz;
        list = parseList;
        option = manager.getOption();
        value = option.blankChars;
        this.manager = manager;
        typeSelect();
    }

    protected List getArray(){
        return list;
    }

    private void typeSelect(){

        char beginChar;

        //ignore boolean
        //default to ignore the null-char
        while(option.isIgnoreChar(beginChar = chars.move()))
            if(beginChar == Constants.Blank.NULL_CHAR) return;

        if(!Objects.equals(beginChar,option.openSquareBrackets))
            throw new JSONCharException("json array should begin with " + option.openSquareBrackets);

        if(null == list)
            list = CollectionUtils.newArrayList();

        controlType = JsonAction.CONTROL_BEGIN_COLLECTION;
        readTache = JsonAction.TACHE_BEGIN_READ_VALUE;

        readList();
    }


    private void readList(){

        char readChar;

        for(;!Objects.equals(controlType, JsonAction.CONTROL_END_COLLECTION);){

            //ignore boolean
            //default to ignore the null-char
            while(option.isIgnoreChar(readChar = chars.move()))
                if(readChar == Constants.Blank.NULL_CHAR) return;

            if(readTache == JsonAction.TACHE_BEGIN_READ_VALUE){
                if(Objects.equals(readChar,option.openSquareBrackets)){ // readChar = '['
                    chars.moveBack();
                    addChildList();
                    readTache = JsonAction.TACHE_END_READ_VALUE;
                }else if(Objects.equals(readChar,option.doubleQuotationMark)){ // readChar = '"'
                    value = chars.moveTil(option.doubleQuotationMark);
                    addBaseSet();
                    readTache = JsonAction.TACHE_END_READ_VALUE;
                }else if(Objects.equals(readChar,option.singleQuotationMark)){ // readChar = '\''
                    value = chars.moveTil(option.singleQuotationMark);
                    addBaseSet();
                    readTache = JsonAction.TACHE_END_READ_VALUE;
                } else if(Objects.equals(readChar,option.openCurly)){ // readChar = '{'
                    chars.moveBack();
                    addChildObject();
                    readTache = JsonAction.TACHE_END_READ_VALUE;
                }else{
                    chars.moveBack();
                    value = chars.moveTil(option.comma,option.closeSquareBrackets);
                    chars.moveBack();
                    addBaseSet();
                    readTache = JsonAction.TACHE_END_READ_VALUE;
                }

            }else if(readTache == JsonAction.TACHE_END_READ_VALUE){
                if(Objects.equals(readChar,option.comma)) // readChar = ','
                    readTache = JsonAction.TACHE_BEGIN_READ_VALUE;
                else if(Objects.equals(readChar,option.closeSquareBrackets)) // readChar = ']'
                    controlType = JsonAction.CONTROL_END_COLLECTION; //break the while
            }
        }
    }


    /**
     * set the value to the method
     */
    private void addBaseSet(){
        String valueStr = new String(value);
        Object val = manager.valTrans(valueStr,clazz);
        list.add(val);
        clearChars();
    }

    /**
     * begin to build a child list
     */
    private void addChildList(){
        List childList = CollectionUtils.newArrayList();
        list.add(childList);
        new ReadArrayBuilder(chars,clazz,childList,manager);
    }

    /**
     * begin to build a child object
     */
    private void addChildObject(){
        Object childObj;
        int childType;
        if(Objects.isNull(clazz)){
            childObj = MapUtils.newHashMap();
            childType = JsonType.TYPE_MAP;
        }else{
            childObj = Invokes.create(clazz);
            childType = JsonType.TYPE_OBJECT;
        }
        list.add(childObj);
        new ReadObjectBuilder(chars,childObj,clazz,childType,manager);
    }

    /**
     * clear the key char[] and value char[]
     */
    private void clearChars(){
        value = new char[0];
    }
}
