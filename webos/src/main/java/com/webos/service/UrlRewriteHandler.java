package com.webos.service;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("ALL")
public class UrlRewriteHandler  extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        String path = target.toLowerCase();
        nextHandler.handle(path, request, response, isHandled);
    }
}