package com.webcore.service;


import com.fasterxml.jackson.databind.type.ClassStack;
import com.webcore.modle.Sysfriend;
import com.webcore.modle.Sysgroup;
import com.webcore.modle.Sysim;

import java.util.List;

public class ImService {

    private  static Sysim instance = new Sysim();
    private  volatile Sysfriend frimd = new Sysfriend();
    private  volatile Sysgroup group = new Sysgroup();
    public Sysim getImUser(String id) {
      return instance.findById(id);
    }

    public List<Sysfriend> getImFriend(String id) {
       return frimd.find("SELECT *FROM sysfriend where imid='"+id+"'");
    }

    public List<Sysgroup> getImgroup(String id) {
       return group.find("SELECT *FROM sysgroup where imid='"+id+"'");
    }

    public static List<Sysim> getListImUser(String id) {
        String s="SELECT * FROM sysim WHERE ID IN(SELECT imid FROM friendrelationship where friendid='"+id+"')";
       return instance.find(s);
    }

    public static List<Sysim> getListImGroup(String id) {
        String s="SELECT * FROM sysim WHERE ID IN(SELECT imid FROM grouprelationship where groupid='"+id+"')";
        return instance.find(s);
    }

    public List<Sysim> getImgroupSysim(String gid) {
        String s="SELECT * FROM sysim WHERE ID IN(SELECT imid FROM grouprelationship where groupid='"+gid+"')";
        return instance.find(s);
    }
}
