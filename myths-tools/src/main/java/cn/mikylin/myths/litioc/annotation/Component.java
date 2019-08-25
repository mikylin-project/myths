package cn.mikylin.myths.litioc.annotation;

import cn.mikylin.myths.exam.base.Null;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    String name() default Null.NULL_STRING;

}
