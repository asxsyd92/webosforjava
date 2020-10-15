package com.asxsyd92.controllers.webos;

import com.asxsyd92.annotation.Route;
import com.asxsyd92.bll.CommomBll;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jwt.JwtInterceptor;

@Route(Key = "/Common")
public class CommomController extends Controller {
    /// <summary>
    ///
    /// </summary>
    /// <param name="tab">查询哪张表</param>
    /// <param name="title">查询条件</param>
    /// <param name="page"></param>
    /// <param name="limit"></param>
    /// <returns></returns>
    //获取公共list
    @Before({JwtInterceptor.class, GET.class})

    public void GetCommonList()
    {
        String tab= getPara("tab");
        String type= getPara("type");
        String title= getPara("title");
        String desc= getPara("desc");
        int page =  getParaToInt("page");
        int limit = getParaToInt("limit");

        if (tab == null || tab == "")
        {
            setAttr("Success", false);
            setAttr("msg", "标准符错误！");

        }else {
            Kv kv=Kv.by("tab",tab).set("type",type).set("title",title).set("desc",desc);
            Page<Record> da= CommomBll.Instance().GetCommonList(kv,page,limit);

            setAttr("code", 0);
            setAttr("msg", "成功！");
            setAttr("count", da.getTotalPage());
            setAttr("data", da.getList());

//        if (title == null || title == "")
//        {
//            if (type != Guid.Empty)
//            {
//                o = Asxsyd92Core.Utils.Data.SQLServer.SqlFromData.GetPage(tab, page, limit, desc, new { Classid = type });
//            }
//            else {
//                o = Asxsyd92Core.Utils.Data.SQLServer.SqlFromData.GetPage(tab, page, limit, desc);
//
//            }
//        }
//        else
//        {
//            if (type != Guid.Empty)
//            {
//                o = Asxsyd92Core.Utils.Data.SQLServer.SqlFromData.GetPage(tab, page, limit, desc, new { Classid = type },new { },new { Name =  title });
//            }
//            else
//            {
//                o = Asxsyd92Core.Utils.Data.SQLServer.SqlFromData.GetPage(tab, page, limit, desc, new {  }, new { }, new { Name =  title });
//            }
//
//        }
//
//        if (o.FirstOrDefault() != null)
//        {
//
//            var os = Models.Unity.ObjModel(o.FirstOrDefault());
//            return Asxsyd92Core.Utils.JSONhelper.ToJson(new { code = 0, msg = "", count = os.FirstOrDefault(c => c.Key.ToString() == "Count").Value, data = o });
//        }
//        return Asxsyd92Core.Utils.JSONhelper.ToJson(new { code = 0, msg = "", count = 0, data = o });

    }
    renderJson();
    }
}
