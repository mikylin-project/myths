package cn.mikylin.myths.litioc.annotation;

import cn.mikylin.myths.exam.base.Null;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Constructor {
    //Object[] params() default {Null.NULL_STRING};
}
