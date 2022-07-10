package com.iworkflow.service.modle;

import com.asxsydutils.annotation.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Table(dataSourceName="webos", tableName="NoticeRead", primaryKey="id")
public abstract class NoticeRead<M extends NoticeRead<M>> extends Model<M> implements IBean {

	public void setId(String Id) {
		set("Id", Id);
	}

	public String getId() {
		return get("Id");
	}

	public void setUserAccount(String UserAccount) {
		set("UserAccount", UserAccount);
	}

	public String getUserAccount() {
		return get("UserAccount");
	}

	public void setNoticeId(Integer NoticeId) {
		set("NoticeId", NoticeId);
	}

	public Integer getNoticeId() {
		return get("NoticeId");
	}

	public void setReadTime(java.util.Date ReadTime) {
		if(ReadTime==null) return;
		set("ReadTime", new java.sql.Timestamp(ReadTime.getTime()));
	}

	public java.util.Date getReadTime() {
		return get("ReadTime");
	}

	public void setIsRead(Integer IsRead) {
		set("IsRead", IsRead);
	}

	public Integer getIsRead() {
		return get("IsRead");
	}

	public void setNote(String Note) {
		set("Note", Note);
	}

	public String getNote() {
		return get("Note");
	}

}
