package com.asxsyd92.service;

import com.jwtTokenPlugin.Bean.IJwtAble;

import java.util.Date;
import java.util.List;

public class User implements IJwtAble {

    private String userName;

    private String password;

    private List<String> _roles;

    private List<String> _forces;

    /**
     * 当前用户的角色
     *
     * @return
     */
    @Override
    public List<String> getRoles() {
        // 使用的时候写通过数据库查询返回给插件一个角色集合
        return get_roles();
    }

    /**
     * 当前用户的权限
     *
     * @return
     */
    @Override
    public List<String> getForces() {
        // 使用的时候写通过数据库查询返回给插件一个角色集合
        return get_forces();
    }

    /**
     * 上次修改密码时间
     *
     * @return
     */
    @Override
    public Date getLastModifyPasswordTime() {

        return new Date(System.currentTimeMillis() - 60L * 1000L * 60L * 24);
    }



    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setRoles(List<String> roles) {
        this._roles = roles;
        return this;
    }

    public User setForces(List<String> forces) {
        this._forces = forces;
        return this;
    }

    public List<String> get_roles() {
        return _roles;
    }

    public List<String> get_forces() {
        return _forces;
    }
}