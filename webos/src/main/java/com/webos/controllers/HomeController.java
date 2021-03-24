package com.webos.controllers;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;


public class HomeController extends Controller {
    @Clear
    public void index() {
        render("index.html");

    }


}