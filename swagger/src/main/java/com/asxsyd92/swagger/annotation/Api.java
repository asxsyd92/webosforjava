package com.asxsyd92.swagger.annotation;

import java.lang.annotation.*;

/**
 * api
 *
 * @author lee
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
