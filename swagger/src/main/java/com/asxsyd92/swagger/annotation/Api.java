package com.asxsyd92.swagger.annotation;

import java.lang.annotation.*;

/**
 * api
 *
 * @author 爱上歆随懿恫
 * @version V1.0.0
 * @date 2017/7/7
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Api {

    String tag() default "";

    String description() default "";
}
