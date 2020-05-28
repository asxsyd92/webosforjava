package com.asxsyd92.service;

import com.jfinal.kit.Kv;
import com.jwtTokenPlugin.Bean.IJwtAble;
import com.jwtTokenPlugin.service.IJwtUserService;

import java.util.Arrays;


public class UserService implements IJwtUserService {
    public static final UserService me = new UserService();

    private UserService() {
    }

    private static Kv store = Kv.create();

    static {
        store.set("com/asxsyd92",
                new User().setForces(Arrays.asList("登录后台", "管理用户"))
                        .setRoles(Arrays.asList("管理员", "普通用户")).setUserName("admin").setPassword("123456")
        ).set("user",
                new User().setForces(Arrays.asList("前台登录", "发布文章"))
                        .setRoles(Arrays.asList("普通用户")).setUserName("user").setPassword("123456")
        );
    }

    @Override
    public IJwtAble login(String userName, String password) {
        User user = (User) store.get(userName);// 假装登录验证
        if (user != null)
            return user;
        throw new RuntimeException("找不到用户");
    }
}

