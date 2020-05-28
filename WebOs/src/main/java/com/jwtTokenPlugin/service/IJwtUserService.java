package com.jwtTokenPlugin.service;


import com.jwtTokenPlugin.Bean.IJwtAble;

/**
 * 我有故事，你有酒么？
 * JKhaled created by yunqisong@foxmail.com ${DATE}
 * FOR : 必须实现的接口
 */
public interface IJwtUserService {
    /**
     * 登录接口 返回一个 IJwtAble  的数据
     *
     * @param userName
     * @param password
     * @return
     */
    IJwtAble login(String userName, String password);
}
