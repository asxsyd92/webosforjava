package com.webos.socket.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
//即时辅助类
public class util <T>
{
    public Object toJson(List<T> data){
        try {


            NameFilter formatName = new NameFilter() {
                public String process(Object object, String name, Object value) {
                    return name.toLowerCase();
                }
            };
            String str = JSONObject.toJSONString(data,formatName, SerializerFeature.WriteMapNullValue);

            Object da=  JSONObject.parse(str);
            return da;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return  null;
        }
    }
}
