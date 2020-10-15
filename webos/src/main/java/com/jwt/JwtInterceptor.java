package com.jwt;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class JwtInterceptor implements Interceptor {



    // 拦截每个请求
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Invocation inv) {
        System.out.println("开始进入拦截器检验jwt头部是否含有Authorization方法！");
        // 通过url得到token请求头是否包含Authorization
        String jwt = request.getHeader("Authorization");
        System.out.println(jwt);
        try {
            // 检测请求头是否为空
            if (jwt == null) {
                System.out.println("用户未登录，验证失败");
            } else {
                Claims c =JwtUtils.parseJwt(jwt);
                System.out.println("用户[ " + c.get("name") + " ]已是登录状态");
                System.out.println("结束进入拦截器检验jwt头部是否含有Authorization方法！");
                Controller controller = inv.getController();
                controller.setAttr("claims", c);
                inv.invoke();
                return true;
            }
            System.out.println("token解析错误，验证失败");
            response.getWriter().write("未登录，请重新登录后操作");
        } catch (Exception e) {
            System.out.print(e.getMessage());
            if (e.getMessage()==null){
                inv.getController().renderError(500);  e.printStackTrace();

            }else {
                inv.getController().renderError(403);  e.printStackTrace();

            }
    }
        return false;
    }

    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        Method method = inv.getMethod();
        preHandle(controller.getRequest(),controller.getResponse(),inv);
        //Class clazz = inv.getTarget().getClass();
     /*   if (handleClass(clazz, method, controller) && handleMethod(method, controller)) {
            inv.invoke();
        } else {
            controller.renderError(401);
        }*/
        inv.getController().getRequest().removeAttribute("me");// 移除避免暴露当前角色信息
        inv.getController().getRequest().removeAttribute("claims");

    }
}
