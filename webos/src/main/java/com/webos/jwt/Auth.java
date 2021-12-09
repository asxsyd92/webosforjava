package com.webos.jwt;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {        //这是一个自定义注解
	String[] Login() default {};

}
