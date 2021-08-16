package com.webcore.modle;

import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Model;
import java.util.List;
import com.webcore.annotation.Table;
import com.webcore.service.ImService;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
@Table(dataSourceName="webos", tableName="sysfriend", primaryKey="id")
public class Sysfriend extends Model<Sysfriend> {

	public void setGroupname(String groupname) {
		set("groupname", groupname);
	}

	public String getGroupname() {
		return get("groupname");
	}

	public void setId(String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}

	public void setImid(String imid) {
		set("imid", imid);
	}

	public String getImid() {
		return get("imid");
	}
	public List<Sysim> list ;
	public List<Sysim> getlist(){
		return ImService.getListImUser(this.getId());
	}

}
