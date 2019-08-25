package cn.mikylin.myths.exam.annotation;

import cn.mikylin.myths.exam.base.BaseFilter;
import cn.mikylin.myths.exam.base.Null;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Check {

    Class<? extends BaseFilter>[] filters();
    String[] group() default {Null.NULL_STRING};
}
