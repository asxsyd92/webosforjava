package com.webcore.modle;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;
import com.asxsydutils.annotation.Table;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
@Table(dataSourceName="webos", tableName="friendrelationship", primaryKey="id")
public abstract class Friendrelationship<M extends Friendrelationship<M>> extends Model<M> implements IBean {

	public void setImid(String imid) {
		set("imid", imid);
	}

	public String getImid() {
		return get("imid");
	}

	public void setId(String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}

	public void setFriendid(String friendid) {
		set("friendid", friendid);
	}

	public String getFriendid() {
		return get("friendid");
	}

}
