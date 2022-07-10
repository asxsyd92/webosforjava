package com.iworkflow.service.oa;

import com.iworkflow.service.modle.Notice;
import com.iworkflow.service.modle.WorkFlowTask;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;

public class NoticeService {
    private  volatile Notice instance = (Notice) new Notice().dao();
    public  Page<Notice> GetNotecePage(Kv kv, Integer page, Integer limit) {

        SqlPara sqlPara =  instance.getSqlPara("oa-notice.GetNotecePage", kv);
        return   instance.paginate(page,limit,sqlPara);
    }

    public Notice GetNoticeDetails(String id) {
        Notice da= (Notice) instance.findById(id);
        return  da;
    }
}
