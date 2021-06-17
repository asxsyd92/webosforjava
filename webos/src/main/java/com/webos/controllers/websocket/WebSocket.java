package com.webos.controllers.websocket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Record;
import com.webcore.modle.Sysim;
import com.webcore.service.ImService;
import com.webos.socket.manager.OnLineUserManager;
import com.webos.socket.sender.MessageSender;
import com.webos.socket.user.SocketUser;
import com.webos.socket.user.message.ToServerTextMessage;
import com.webos.socket.util.LayIMFactory;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;


/**
 * 〈webSocket功能〉
 *
 * @author foam103
 * @create 2020/3/15
 */
@ServerEndpoint("/websocket.ws/{userId}")
public class WebSocket {

        private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();

        private Session session;
        private String userId;

        @OnOpen
        public void onOpen(@PathParam("userId") String userId, Session session) throws IOException {


                this.userId = userId;
                this.session = session;
                clients.put(userId, this);
                Record record=new Record();
                record.set("content","欢迎光临！");

                record.set("id",userId);
                record.set("type","TYPE_TEXT_MESSAGE");
                session.getAsyncRemote().sendText(JSON.toJSONString(record.getColumns()));
               SocketUser user = new SocketUser();
                user.setSession(session);
                user.setUserId(userId);
                //保存在线列表
                LayIMFactory.createManager().addUser(user);
               // print("当前在线用户："+LayIMFactory.createManager().getOnlineCount());
               // print("缓存中的用户个数："+new OnLineUserManager().getOnLineUsers().size());


        }

        @OnClose
        public void onClose(Session session) {
                clients.remove(userId);
        }

        @OnError
        public void onError(Session session, Throwable error) {
                error.printStackTrace();
        }

        @OnMessage
        public void onMessage(String message) {
                // message是json格式
                JSONObject obj = JSONObject.parseObject(message);
                String user = obj.get("userId").toString();
                String mes = obj.get("message").toString();
                // 判断是否在线，如果在线发送信息
                for (WebSocket item : clients.values()) {
                        if (item.userId.equals(user)) {
                                item.session.getAsyncRemote().sendText(mes);
                        }
                }
        }
}