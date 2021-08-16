package com.webcore.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.Date;

import static com.asxsydutils.utils.StringUtil.getPrimaryKey;


public class UsersIntegralService {
    public static boolean AddIntegral(String userid, double intr) {

        try{
       Record da= Db.findById("usersintegral","Userid",userid);

        if (da != null)
        {
            double jf = 0;
            double rs=0;
            jf = da.getInt("Integral");
            rs = jf + intr ;
            da.set("Addtime",new Date()) ;
            da.set("Integral",rs);
           return Db.update("usersintegral",da);

        }
        else {
            Record usersIntegral = new Record();
            usersIntegral.set("ID", getPrimaryKey()) ;
            usersIntegral.set("Userid",  userid);
            usersIntegral.set("Integral" , intr);
            usersIntegral.set("Addtime",new Date());
            return Db.save("usersintegral",usersIntegral);

        }}
        catch (Exception ex){
        System.out.println(ex.getMessage());
        return false;
    }
    }

}
