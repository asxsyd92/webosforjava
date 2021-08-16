package com.baidu.ueditor.hunter;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.MultiState;
import com.baidu.ueditor.define.State;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FileManager {

	private String dir = null;
	private String rootPath = null;
	private String[] allowFiles = null;
	private int count = 0;
	
	public FileManager ( Map<String, Object> conf ) {

		this.rootPath = (String)conf.get( "rootPath" );
		this.dir = this.rootPath +"/"+ (String)conf.get( "dir" );
		this.allowFiles = this.getAllowFiles( conf.get("allowFiles") );
		this.count = (Integer)conf.get( "count" );
		
	}
	
	public State listFile ( int index ) {

		State state = null;
		String table = "";
		if (this.dir.indexOf("image") > 0) {
			table = "Sys_BaiduPicture";
		}
		if (this.dir.indexOf("file") > 0) {
			table = "Sys_BaiduFile";
		}
		if (this.dir.indexOf("video") > 0) {
			table = "Sys_BaiduVideo";
		}
		String sql = "SELECT url ";
		String sqlwhere = "FROM " + table + " ORDER BY Adddate DESC";
		if (index == 0) {
			index = 1;
		}
		Page<Record> da = Db.paginate(index, count, sql, sqlwhere);

		state = this.getStates(da.getList());


		state.putInfo("start", index);
		state.putInfo("total", da.getTotalRow());

		return state;

	}
	private State getStates (List<Record> files ) {

		MultiState state = new MultiState( true );
		BaseState fileState = null;

		File file = null;

		for ( Record obj : files ) {
			if ( obj == null ) {
				break;
			}
			//file = (File)obj;
			fileState = new BaseState( true );
			fileState.putInfo( "url", obj.getStr("url"));
			state.addState( fileState );
		}

		return state;

	}
	private State getState ( Object[] files ) {
		
		MultiState state = new MultiState( true );
		BaseState fileState = null;
		
		File file = null;
		
		for ( Object obj : files ) {
			if ( obj == null ) {
				break;
			}
			file = (File)obj;
			fileState = new BaseState( true );
			fileState.putInfo( "url", PathFormat.format( this.getPath( file ) ) );
			state.addState( fileState );
		}
		
		return state;
		
	}
	
	private String getPath ( File file ) {
		
		String path = file.getAbsolutePath();
		
		return path.replace( this.rootPath, "/" );
		
	}
	
	private String[] getAllowFiles ( Object fileExt ) {
		
		String[] exts = null;
		String ext = null;
		
		if ( fileExt == null ) {
			return new String[ 0 ];
		}
		
		exts = (String[])fileExt;
		
		for ( int i = 0, len = exts.length; i < len; i++ ) {
			
			ext = exts[ i ];
			exts[ i ] = ext.replace( ".", "" );
			
		}
		
		return exts;
		
	}
	
}
