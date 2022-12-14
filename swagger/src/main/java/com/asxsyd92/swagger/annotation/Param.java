package com.asxsyd92.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数
 *
 * @author 爱上歆随懿恫
 * @version V1.0.0
 * @date 2017/7/7
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String name() default "";

    String description() default "";

    boolean required() default false;

    String dataType() default "";
    
    String format() default "";
    
    String defaultValue() default "";

}
