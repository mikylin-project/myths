package cn.mikylin.myths.litjson.read;

import cn.mikylin.myths.litjson.OptionBox;
import cn.mikylin.myths.litjson.handler.TypeHandler;
import java.util.Objects;

/**
 * read type manager
 * @author mikylin
 */
public class ReadManager {

    private OptionBox option;

    public ReadManager(OptionBox option){
        this.option = option;
    }

    protected OptionBox getOption(){
        return option;
    }


    //use type handler to trans value
    protected Object valTrans(String val,Class clz){
        Class valClass = clz;
        if(null == clz)
            valClass = String.class;

        TypeHandler typeHandler;
        if(null == (typeHandler = option.typeHandlers.get(valClass)))
            return null;

        return typeHandler.read(val);
    }

}
