package com.asxsyd92.swagger.model;


import java.util.ArrayList;
import java.util.List;

/**
 * 全局参数
 *
 * @author 爱上歆随懿恫
 * @version V1.0.0
 * @date 2018/1/5
 */
public class SwaggerGlobalPara {
    private static List<SwaggerPath.Parameter> parameterList = new ArrayList<>();

    public static List<SwaggerPath.Parameter> getParameterList() {
        return parameterList;
    }

    public static void addPara(SwaggerPath.Parameter parameter) {
        parameterList.add(parameter);
    }
}
