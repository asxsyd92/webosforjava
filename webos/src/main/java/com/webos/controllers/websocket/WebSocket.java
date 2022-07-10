package com.webos.controllers.websocket;


import com.alibaba.fastjson.JSON;
import com.asxsydutils.utils.StringUtil;
import com.webcore.modle.Sysim;
import com.webos.socket.sender.MessageSender;
import com.webos.socket.user.*;
import com.webos.socket.user.message.MessageType;
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
@ServerEndpoint("/websocket.ws/{uid}")
public class WebSocket {

        private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();

        private Session session;
        private String userId;

        @OnOpen
        public void onOpen(@PathParam("uid")String uid, Session session) throws IOException {

               SocketUser user = new SocketUser();
                user.setSession(session);
                user.setUserId(uid);
                //保存在线列表
                LayIMFactory.createManager().addUser(user);
               // print("当前在线用户："+LayIMFactory.createManager().getOnlineCount());
               // print("缓存中的用户个数："+new OnLineUserManager().getOnLineUsers().size());
        }

        @OnClose
        public void onClose(@PathParam("uid")String uid,Session session) {


          SocketUser usre=  LayIMFactory.createManager().getUser(uid);
            LayIMFactory.createManager().removeUser(usre);
        }

        @OnError
        public void onError(Session session, Throwable error) {
                error.printStackTrace();
        }

        @OnMessage
        public void onMessage(String message) {
            Map<String,Object> map=   JSON.parseObject(message,Map.class);
            if (map.get("type").equals("group")){
                GroupMessage da = JSON.parseObject(map.get("data").toString(), GroupMessage.class);
                System.out.println(da);
                for (Sysim userid :da.getTo().getList()) {
                    //遍历发送消息 自己过滤，不给自己发送(发送人id和群成员内的某个id相同)
                   // if (!da.getMine().getId().equals(userid.getId()))
                    {
                        SocketUser user = LayIMFactory.createManager().getUser(userid.getId());
                        if (user.isExist()) {
                            if (user.getSession() == null) {
                                //  LayIMLog.info("该用户不在线 ");
                            } else {
                                Session session = user.getSession();
                                if (session.isOpen()) {


                                    //构造用户需要接收到的消息
                                   // session.getAsyncRemote().sendText(message);
                                    da.getTo().setType(MessageType.GROUP);
                                    da.getTo().setContent(da.getMine().getContent());
                                    To to=new To();
                                    to.setContent(da.getMine().getContent());
                                    to.setAvatar(da.getMine().getAvatar());
                                    //群id
                                    to.setId(da.getTo().getId());
                                    to.setType(MessageType.GROUP);
                                    to.setUsername(da.getMine().getUsername());
                                    to.setName(da.getMine().getUsername());
                                    to.setStatus("online");

                                    user.getSession().getAsyncRemote().sendText(JSON.toJSONString(to));
                                }
                            }
                        }else{
                            //  LayIMLog.info("该用户不在线 ");
                        }
                    }
                }

            }else {

                UserMessage da = JSON.parseObject(map.get("data").toString(), UserMessage.class);

                //机器人
                if (da.getTo().getId().equals(StringUtil.GuidEmpty())) {


                    try {
                        UserMessage dacon = MessageSender.RobotMeesage(da);


                        SocketUser user = LayIMFactory.createManager().getUser(da.getMine().getId());
                        if (user != null) {
                            da.getTo().setType(MessageType.FRIEND);
                            user.getSession().getAsyncRemote().sendText(JSON.toJSONString(da.getTo()));
                        }

                    } catch (Exception ex) {
                    }

                } else {
                    SocketUser user = LayIMFactory.createManager().getUser(da.getTo().getId());
                    if (user != null) {
                        da.getTo().setType(MessageType.FRIEND);
                        user.getSession().getAsyncRemote().sendText(JSON.toJSONString(da.getTo()));
                    }
                }

            }
        }
}