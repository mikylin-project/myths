package cn.mikylin.myths.litjson.read;

import cn.mikylin.myths.common.CollectionUtils;
import cn.mikylin.myths.common.MapUtils;
import cn.mikylin.myths.litjson.OptionBox;
import cn.mikylin.myths.litjson.exception.JSONCharException;
import cn.mikylin.myths.litjson.util.Invokes;
import cn.mikylin.myths.litjson.util.JsonAction;
import cn.mikylin.myths.litjson.util.JsonType;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReadObjectBuilder {

    //json string buffer
    private ReadCharBuffer chars;

    //char[] to save key
    private char[] key;

    //char[] to save value
    private char[] value;

    //control type
    private int controlType;

    //tache type
    private int readTache;

    //the object which wait to set
    private Object bean;

    //param setters
    //if object is a map, this could be null
    private Map<String, Method> setters;

    //read manager
    private ReadManager manager;

    //object or map
    private int type;

    //object class
    private Class clazz;

    private OptionBox option;

    protected ReadObjectBuilder(ReadCharBuffer buffer,
                                Object bean,
                                Class clz,
                                int parseType,
                                ReadManager manager){
        chars = buffer;
        controlType = JsonAction.CONTROL_REST;
        this.manager = manager;
        option = manager.getOption();
        key = option.blankChars;
        value = null;
        this.bean = bean;
        type = parseType;
        clazz = clz;
        typeSelect();
    }

    protected Object getObject(){
        return bean;
    }

    /**
     * boolean the type and do the work
     */
    private void typeSelect(){

        char beginChar;
        //ignore boolean
        //default to ignore the null-char
        while(option.isIgnoreChar(beginChar = chars.move()))
            if(Objects.isNull(beginChar)) return;

        if(!Objects.equals(beginChar,option.openCurly))
            throw new JSONCharException("json object should begin with " + option.openCurly);

        if(Objects.equals(type, JsonType.TYPE_OBJECT)){  //this type is object
            //create the bean
            if(Objects.isNull(bean))
                bean = Invokes.create(clazz);
            //get set methods
            setters = Invokes.getSetMethods(clazz);
        }else if(Objects.equals(type, JsonType.TYPE_MAP)){ //this type is map
            //create the map
            if(Objects.isNull(bean))
                bean = MapUtils.createMap();
        }

        //init the control type and read tache
        controlType = JsonAction.CONTROL_BEGIN_OBJECT;
        readTache = JsonAction.TACHE_BEGIN_READ_KEY;

        //begin to read the object
        readObject();

    }

    /**
     * read a object
     */
    private void readObject(){

        char readChar;

        for(;!Objects.equals(controlType, JsonAction.CONTROL_END_OBJECT);){

            //ignore boolean
            //default to ignore the null-char
            while(option.isIgnoreChar(readChar = chars.move()))
                if(Objects.isNull(readChar)) return;

            if(Objects.equals(readTache, JsonAction.TACHE_BEGIN_READ_KEY)) { //begin to read a key
                if(Objects.equals(readChar,option.doubleQuotationMark)){
                    key = chars.moveTil(option.doubleQuotationMark);
                    readTache = JsonAction.TACHE_END_READ_KEY;
                }else if(Objects.equals(readChar,option.singleQuotationMark)){
                    key = chars.moveTil(option.singleQuotationMark);
                    readTache = JsonAction.TACHE_END_READ_KEY;
                }

            }else if(Objects.equals(readTache, JsonAction.TACHE_END_READ_KEY)){ //key read end
                if(Objects.equals(readChar,option.colon))
                    readTache = JsonAction.TACHE_BEGIN_READ_VALUE;

            }else if(Objects.equals(readTache, JsonAction.TACHE_BEGIN_READ_VALUE)){ //begin to read a value
                if(Objects.equals(readChar,option.doubleQuotationMark)){ // readChar = '"'
                    value = chars.moveTil(option.doubleQuotationMark);
                    invokeBaseSet();
                    readTache = JsonAction.TACHE_END_READ_VALUE;
                }else if(readChar == option.comma){ // readChar = ','
                    if(!Objects.equals(key,option.blankChars))
                        invokeBaseSet();
                    readTache = JsonAction.TACHE_END_READ_VALUE;
                }else if(Objects.equals(readChar,option.openCurly)){ // readChar = '{'
                    chars.moveBack();
                    childObjectParse();
                    readTache = JsonAction.TACHE_END_READ_VALUE;
                }else if(Objects.equals(readChar,option.openSquareBrackets)){ // readChar = '['
                    chars.moveBack();
                    childArrayParse();
                    readTache = JsonAction.TACHE_END_READ_VALUE;
                } else{
                    chars.moveBack();
                    value = chars.moveTil(option.comma,option.closeCurly);
                    chars.moveBack();
                    invokeBaseSet();
                    readTache = JsonAction.TACHE_END_READ_VALUE;
                }

            }else if(Objects.equals(readTache, JsonAction.TACHE_END_READ_VALUE)){ //value read end
                if(Objects.equals(readChar,option.comma))
                    readTache = JsonAction.TACHE_BEGIN_READ_KEY;
                else if(Objects.equals(readChar,option.closeCurly))
                    controlType = JsonAction.CONTROL_END_OBJECT; //break the while
            }
        }
    }

    /**
     * set the value to the method
     */
    private void invokeBaseSet(){
        String keyStr = new String(key);
        String valueStr = new String(value);

        if(Objects.equals(type, JsonType.TYPE_OBJECT)){  //this type is object
            Method setter = setters.get(keyStr);
            Class clz = Invokes.methodParam(setter);
            Object val = manager.valTrans(valueStr,clz);
            Invokes.invokeSet(setters.get(keyStr),bean,val);
        }else if(type == JsonType.TYPE_MAP) //this type is map
            ((Map<String,Object>)bean).put(keyStr,valueStr);
        clearChars();
    }

    /**
     * recursion create object
     */
    private void childObjectParse(){
        String keyStr = new String(key);
        Object childObj = null;
        int childType = type;
        if(Objects.equals(type, JsonType.TYPE_OBJECT)){ //this type is object
            Method setter = setters.get(keyStr);
            Class clz = Invokes.methodParam(setter);
            if(Map.class.isAssignableFrom(clz)){ //if the param type is map
                childObj = MapUtils.createMap();
                childType = JsonType.TYPE_MAP;
            }else{ //the param type is object
                childObj = Invokes.create(clz);
                childType = JsonType.TYPE_OBJECT;
            }
            Invokes.invokeSet(setter,bean,childObj);
        }else if(type == JsonType.TYPE_MAP){ //this type is map
            childObj = MapUtils.createMap();
            childType = JsonType.TYPE_MAP;
            ((Map<String,Object>)bean).put(keyStr,childObj);
        }
        clearChars();
        new ReadObjectBuilder(chars,childObj,childObj.getClass(),childType,manager);
    }

    /**
     * recursion create array
     */
    private void childArrayParse(){
        String keyStr = new String(key);

        Method setter = setters.get(keyStr);
        Class genericityClass = Invokes.genericityClass(bean.getClass(),keyStr);
        List childArray = CollectionUtils.newArrayList();
        Invokes.invokeSet(setter,bean,childArray);
        new ReadArrayBuilder(chars,genericityClass,childArray,manager);
    }

    /**
     * clear the key char[] and value char[]
     */
    private void clearChars(){
        key = option.blankChars;
        value = option.blankChars;
    }



}
