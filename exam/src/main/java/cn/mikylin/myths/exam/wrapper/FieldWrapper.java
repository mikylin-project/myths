package cn.mikylin.myths.exam.wrapper;

import cn.mikylin.myths.common.reflect.FieldUtils;
import cn.mikylin.myths.exam.annotation.Check;
import cn.mikylin.myths.exam.annotation.Column;
import cn.mikylin.myths.exam.base.Null;

import java.lang.reflect.Field;

public final class FieldWrapper {

    private Check check;
    private Column column;
    private Object value;
    private String name;
    private Class<?> type;

    public FieldWrapper(Field field,Object entity){

        this.check = field.getAnnotation(Check.class);
        this.column = field.getAnnotation(Column.class);
        this.value = FieldUtils.refField(field,entity);

        if(column != null
                && column.name() != Null.NULL_STRING)
            this.name = column.name();
        else this.name = field.getName();

        this.type = field.getType();
    }

    public Check check(){
        return check;
    }

    public Column column(){
        return column;
    }

    public Object value(){
        return value;
    }

    public String name(){
        return name;
    }

    public Class<?> type(){
        return type;
    }


}
