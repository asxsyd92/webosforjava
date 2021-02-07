package com.asxsyd92.socket.util;


import com.asxsyd92.socket.manager.IUserManager;
import com.asxsyd92.socket.manager.UserManager;
import com.asxsyd92.socket.util.serializer.FastJsonSerializer;
import com.asxsyd92.socket.util.serializer.IJsonSerializer;

/**
 * Created by pz on 16/11/23.
 */
public class LayIMFactory {
    //创建序列化器
    public static IJsonSerializer createSerializer(){
        return new FastJsonSerializer();
    }

    //创建在线人员管理工具
    public static IUserManager createManager(){
        return UserManager.getInstance();
    }



}
