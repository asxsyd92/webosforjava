package com.asxsyd92.swagger.model.enums;

/**
 * 协议 枚举
 *
 * @author 爱上歆随懿恫
 * @version V1.0.0
 * @date 2017/12/11
 */
public enum Scheme {
    HTTP("http"),
    HTTPS("https");

    private String name;

    private Scheme(String name) {
        this.name = name;
    }
}
