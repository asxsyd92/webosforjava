package com.webcore.modle;

import com.jfinal.plugin.activerecord.Model;
import com.webcore.annotation.Table;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
@Table(dataSourceName="webos", tableName="sysim", primaryKey="id")
public class Sysim extends Model<Sysim> {

	public void setId(String id) {
		set("id", id);
	}

	public String getId() {
		return get("id");
	}

	public void setUsername(String username) {
		set("username", username);
	}

	public String getUsername() {
		return get("username");
	}

	public void setStatus(String status) {
		set("status", status);
	}

	public String getStatus() {
		return get("status");
	}

	public void setSign(String sign) {
		set("sign", sign);
	}

	public String getSign() {
		return get("sign");
	}

	public void setAvatar(String avatar) {
		set("avatar", avatar);
	}

	public String getAvatar() {
		return get("avatar");
	}

}
