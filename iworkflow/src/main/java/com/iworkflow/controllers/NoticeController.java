package com.iworkflow.controllers;

import com.alibaba.fastjson.JSON;
import com.asxsydutils.config.LoginUsers;
import com.asxsydutils.utils.Common;
import com.asxsydutils.utils.StringUtil;
import com.iworkflow.service.modle.Notice;
import com.iworkflow.service.modle.NoticeRead;
import com.iworkflow.service.oa.NoticeService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.security.Authorization;
import com.webcore.utils.data.mysqlserver.FromData;
import io.jsonwebtoken.Claims;
import kotlin.collections.ArrayDeque;

import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/notice")
@Before({ POST.class, Authorization.class})
public class NoticeController extends Controller {
    @Inject
    NoticeService service;

    public void GetNotecePage() {
        try {
            String title = getPara("title");
            String type = getPara("type");
            Integer page = getParaToInt("page");
            Integer limit = getParaToInt("limit");
            page = page == 0 ? 1 : page;
            limit = limit == 0 ? 15 : limit;
            String receiveid = "";
            Claims claims = getAttr("claims");
            receiveid = claims.get("id").toString();
            Kv kv = Kv.by("title", title).set("type", type).set("users", receiveid);
            Page<Notice> da = service.GetNotecePage(kv, page, limit);
            setAttr("code", 0);
            setAttr("msg", "成功！");
            setAttr("count", da.getTotalRow());
            setAttr("data", da.getList());
            setAttr("success", true);
        } catch (Exception ex) {
            setAttr("code", 0);
            setAttr("count", 0);
            setAttr("data", null);
            setAttr("msg", ex.getMessage());
            setAttr("success", false);
        }

        renderJson();
    }


    public void AddNotice() {
        try {
            String data = getPara("data");
            LoginUsers usersModel = Common.getLoginUser(this);
            Notice notice = JSON.parseObject(data, Notice.class);

            notice.setId(StringUtil.getPrimaryKey());
            if (notice.getTitle().isEmpty()) {
                setAttr("code", 0);
                setAttr("msg", "标题不能为空！");
                setAttr("count", 1);
                setAttr("data", null);
                setAttr("success", false);
                renderJson();
                return;
            }
            if (notice.getContents().isEmpty()) {
                setAttr("code", 0);
                setAttr("msg", "内容不能为空！");
                setAttr("count", 1);
                setAttr("data", null);
                setAttr("success", false);
                renderJson();
                return;
            }
            if (notice.getUsers() == null) {
                notice.setDeptID(null);
                notice.setUsers(null);
            } else {
                List<String> u = Arrays.stream(notice.getUsers().split(",")).collect(Collectors.toList());
                //用户
                List<String> user = new ArrayDeque<>();
                //部门
                List<String> org = new ArrayDeque<>();
                for (String item : u) {
                    if (item.contains("u_")) {
                        user.add(item);
                    } else {
                        org.add(item);
                    }
                }
                notice.setDeptID(String.join(",", org));
                notice.setUsers(String.join(",", user));
            }
            notice.setUserID(usersModel.getId());
            notice.setUserName(usersModel.getName());
            notice.setAddTime(new Date());

            notice.save();
            setAttr("code", 0);
            setAttr("msg", "发布成功");
            setAttr("count", 1);
            setAttr("data", notice);
            setAttr("success", true);
        } catch (Exception ex) {
            setAttr("code", 0);
            setAttr("msg", ex.getMessage());
            setAttr("count", 1);
            setAttr("data", null);
            setAttr("success", false);
        }
        renderJson();
    }

    public void GetNoticeDetails() {
        try {
            String id = getPara("id");
            LoginUsers usersModel = Common.getLoginUser(this);

            Notice notice = service.GetNoticeDetails(id);
            NoticeRead noticeRead = new NoticeRead();
            noticeRead.setNoticeId(notice.getId());
            noticeRead.setUserAccount(usersModel.getName());
            noticeRead.setReadTime(new Date());
            noticeRead.setId(StringUtil.getPrimaryKey());
            noticeRead.setUserId(usersModel.getId());
            noticeRead.setIsRead(1);

            NoticeRead read = (NoticeRead) noticeRead.findFirst("select * from NoticeRead where UserId= '" + noticeRead.getUserId() + "' and NoticeId='" + noticeRead.getNoticeId() + "'");
            if (read == null) {
                noticeRead.save();
            }
            setAttr("code", 0);
            setAttr("msg", "发布成功");
            setAttr("count", 1);
            setAttr("data", notice);
            setAttr("success", true);
        } catch (Exception ex) {
            setAttr("code", 0);
            setAttr("msg", ex.getMessage());
            setAttr("count", 1);
            setAttr("data", null);
            setAttr("success", false);
        }
        renderJson();
    }

}
