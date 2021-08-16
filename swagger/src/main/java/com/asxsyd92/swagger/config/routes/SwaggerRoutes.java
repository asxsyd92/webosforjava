package com.asxsyd92.swagger.config.routes;

import com.asxsyd92.swagger.controller.SwaggerController;
import com.jfinal.config.Routes;

/**
 * 默认路由
 *
 * @author lee
 * @version V1.0.0
 * @date 2017/7/8
 */
public class SwaggerRoutes extends Routes {

    @Override
    public void config() {
        setBaseViewPath("/WEB-INF/views");
        add("/swagger", SwaggerController.class);
    }

}
