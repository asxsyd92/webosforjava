package com.webos.socket.manager;


import com.webos.socket.user.SocketUser;

/**
 * Created by pz on 16/11/23.
 */
public interface IUserManager {

    boolean addUser(SocketUser user);

    boolean removeUser(SocketUser user);

    int getOnlineCount();

    SocketUser getUser(String userId);

}
