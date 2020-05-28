package com.jwtTokenPlugin.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jwtTokenPlugin.Bean.IJwtAble;
import com.jwtTokenPlugin.kit.JwtKit;
import io.jsonwebtoken.Claims;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 我有故事，你有酒么？
 * JKhaled created by yunqisong@foxmail.com 2017/7/23
 * FOR : Jwt插件核心拦截方法
 */
public class JwtTokenInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        Integer code = recoverAuthFromRequest(inv.getController().getRequest());
        switch (code) {
            case 200: {
                Claims claims=null;
                String authHeader = inv.getController().getRequest().getHeader(JwtKit.header);

                String authToken = authHeader.substring(JwtKit.tokenPrefix.length());
                claims  = JwtKit.getClaimsFromToken(authToken);
                Controller controller = inv.getController();
                controller.setAttr("claims", claims);
                inv.invoke();

                break;
            }
            default: {
                inv.getController().renderError(401);
            }
        }
        inv.getController().getRequest().removeAttribute("me");// 移除避免暴露当前角色信息
    }

    /**
     * 读取Token从请求中 判断是否允许访问
     *
     * @param request
     * @return
     */
    private Integer recoverAuthFromRequest(HttpServletRequest request) {
        //没有权限用户
        try {
            IJwtAble jwtBean = getMe(request);
            if (jwtBean == null) return 404;
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
            return 404;
        }
    }

    /**
     * 从请求头解析出me
     *
     * @param request
     * @return
     */
    protected static IJwtAble getMe(HttpServletRequest request) {
        IJwtAble me = (IJwtAble) request.getAttribute("me");
        if (null != me) return me;
        String authHeader = request.getHeader(JwtKit.header);
        if (StrKit.isBlank(authHeader) || authHeader.length() < JwtKit.tokenPrefix.length()) return null;
        String authToken = authHeader.substring(JwtKit.tokenPrefix.length());
        String jwtUser = JwtKit.getJwtUser(authToken);                      // 从token中解析出jwtAble
        if (jwtUser != null){
            Date created = JwtKit.getCreatedDateFormToken(authToken);
            me = JwtKit.getJwtBean(jwtUser,created);
            request.setAttribute("me", me);
        }
        return me;
    }
}
