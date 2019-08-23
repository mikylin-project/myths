package cn.mikylin.myths.litjson.write;

import cn.mikylin.myths.litjson.util.JsonType;

/**
 * Object definition
 * use in object to json string
 * @author mikylin
 */
public class WriteObjectBuilder {

    //object class
    private Class clazz;
    //object type
    private int type;
    //json key
    private String key;
    //json value
    private Object value;
    //next floor objects
    private WriteObjectBuilder[] unders;
    //layer num
    private int stair;
    //is the tail?
    private boolean tail;
    //type manager
    private WriteManager manager;
    //upper floor object type
    private int parentType;

    /**
     * init for top object
     */
    protected WriteObjectBuilder(Object value, WriteManager typeManager){
        this(null,value,typeManager,0,true, JsonType.TYPE_TOP);
    }

    protected WriteObjectBuilder(String key, Object value, WriteManager manager, int stair, boolean tail, int parentType){
        clazz = value.getClass();
        this.key = key;
        this.manager = manager;
        this.stair = stair;
        this.tail = tail;
        this.unders = null;
        this.parentType = parentType;
        this.manager.typeSeletor(value,clazz,this);
    }

    protected boolean hasUnders(){
        return unders == null ? false : true;
    }

    protected WriteObjectBuilder[] getUnders(){
        return unders;
    }

    protected Object getValue(){
        return value;
    }

    protected void setType(int type){
        this.type = type;
    }

    protected int getType(){
        return type;
    }

    protected String getKey(){
        return key;
    }

    protected boolean isOrigin(){
        return stair == 0;
    }

    protected boolean isTail(){
        return tail;
    }

    protected void setValue(Object value){
        this.value = value;
    }

    protected void setUnders(WriteObjectBuilder[] handlers){
        this.unders = handlers;
    }

    protected int getStair(){
        return stair;
    }

    protected int getParentType(){
        return parentType;
    }


}