package com.webcore.modle;

import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;
import com.webcore.annotation.Table;
import com.webcore.service.ImService;

import java.util.List;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
@Table(dataSourceName="webos", tableName="sysgroup", primaryKey="id")
public class Sysgroup extends Model<Sysgroup> {

	public void setId(String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}

	public void setGroupname(String groupname) {
		set("groupname", groupname);
	}

	public String getGroupname() {
		return get("groupname");
	}

	public void setAvatar(String avatar) {
		set("avatar", avatar);
	}

	public String getAvatar() {
		return get("avatar");
	}

	public void setImid(String imid) {
		set("imid", imid);
	}

	public String getImid() {
		return get("imid");
	}
	public List<Sysim> list ;
	public List<Sysim> getlist(){
		return ImService.getListImGroup(this.getId());
	}
}
