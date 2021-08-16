package com.asxsyd92.swagger.model.enums;

/**
 * 请求方法
 *
 * @author lee
 * @version V1.0.0
 * @date 2017/12/11
 */
public enum HttpMethod {
    HEAD("head", "请求页面的首部"),
    GET("get", "查看"),
    POST("post", "创建"),
    PUT("put", "更新"),
    DELETE("delete", "删除");

    private String name;
    private String desc;

    private HttpMethod(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
