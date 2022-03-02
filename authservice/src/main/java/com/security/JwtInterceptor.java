package com.security;

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
System.out.print("请求"+request.getRequestURI());
        String jwt = request.getHeader("Authorization");
        try {
            // 检测请求头是否为空
            if (jwt == null) {
             response.setStatus(401);
                inv.getController().renderError(401);
                System.out.println("用户未登录，验证失败");
                return  false;

            } else {
                Claims c =JwtUtils.parseJwt(jwt);

                System.out.println("用户[ " + c.get("name") + " ]已是登录状态");
                System.out.println("结束进入拦截器检验jwt头部是否含有Authorization方法！");
                Controller controller = inv.getController();
                controller.setAttr("claims", c);
                inv.invoke();
                return true;
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());

                inv.getController().renderError(401);  e.printStackTrace();


    }
        return false;
    }

    @Override
    public void intercept(Invocation inv) {
        System.out.print("请求"+inv.getController().getRequest().getRequestURI());
        Controller controller = inv.getController();
        Method method = inv.getMethod();
        if (!method.isAnnotationPresent(Authentication.class)) {
            inv.invoke();
        }else {
            preHandle(controller.getRequest(),controller.getResponse(),inv);
            inv.getController().getRequest().removeAttribute("me");// 移除避免暴露当前角色信息
            inv.getController().getRequest().removeAttribute("claims");
        }



    }


}
