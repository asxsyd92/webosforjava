package com.baidu.ueditor.upload;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public final class Base64Uploader {

	public static State save(HttpServletRequest request, String content, Map<String, Object> conf) {
		
		byte[] data = decode(content);

		long maxSize = ((Long) conf.get("maxSize")).longValue();

		if (!validSize(data, maxSize)) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}

		String suffix = FileType.getSuffix("JPG");

		String savePath = PathFormat.parse((String) conf.get("savePath"),
				(String) conf.get("filename"));
		
		savePath = savePath + suffix;
		String physicalPath = (String) conf.get("rootPath") +"/"+ savePath;

		State storageState = StorageManager.saveBinaryFile(data, physicalPath);

		if (storageState.isSuccess()) {
			//将文件存入数据库
			String table = "";
			if (savePath.indexOf("image") > 0)
			{
				table = "Sys_BaiduPicture";
			}
			if (savePath.indexOf("file") > 0)
			{
				table = "Sys_BaiduFile";
			}
			if (savePath.indexOf("video") > 0)
			{
				table = "Sys_BaiduVideo";
			}
			String asxsyd92user="asxsyd92upload/"+request.getParameter("asxsyd92user").toLowerCase();
			try{
				Record files=new Record();
				files.set("ID", UUID.randomUUID().toString());
				files.set("UserID", request.getParameter("asxsyd92user").toLowerCase());
				files.set("Type", "");
				files.set("AddDate",  new Date());
				files.set("Url",asxsyd92user+"/"+ PathFormat.format(savePath));
				Db.save(table,files);
			}catch (Exception ex){}


			storageState.putInfo("url",asxsyd92user+"/"+ PathFormat.format(savePath));
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", "");
		}

		return storageState;
	}

	private static byte[] decode(String content) {
		return Base64.decodeBase64(content);
	}

	private static boolean validSize(byte[] data, long length) {
		return data.length <= length;
	}
	
}