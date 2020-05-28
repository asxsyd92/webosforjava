package com.baidu.ueditor.hunter;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.io.FileUtils;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.MultiState;
import com.baidu.ueditor.define.State;

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
		if (this.dir.indexOf("image") > 0)
		{
			table = "Sys_BaiduPicture";
		}
		if (this.dir.indexOf("file") > 0)
		{
			table = "Sys_BaiduFile";
		}
		if (this.dir.indexOf("video") > 0)
		{
			table = "Sys_BaiduVideo";
		}
		String sql = "SELECT url ";
		String sqlwhere="FROM "+table;
if (index==0){
	index=1;
}
	Page<Record> da= Db.paginate(index,count,sql,sqlwhere);
//System.out.print(da.toString());
		//Collection<File> list =da.getList();

		/*File dir = new File( this.dir );


		if ( !dir.exists() ) {
			return new BaseState( false, AppInfo.NOT_EXIST );
		}
		
		if ( !dir.isDirectory() ) {
			return new BaseState( false, AppInfo.NOT_DIRECTORY );
		}
		
		Collection<File> list = FileUtils.listFiles( dir, this.allowFiles, true );
		
		if ( index < 0 || index > list.size() ) {
			state = new MultiState( true );
		} else {
			Object[] fileList = Arrays.copyOfRange( list.toArray(), index, index + this.count );
			state = this.getState( fileList );
		}
		*/
		state = this.getStates( da.getList() );
		/*BaseState fileState = null;	MultiState states = new MultiState( true );
	//	List<String> d=da.getList().stream().forEach(it->it.getStr("url"));
		for (Record d:da.getList() ) {
			String usrs=d.getStr("url");
			if (usrs!=null){
				fileState = new BaseState( true );
				state.putInfo( "url",PathFormat.format(d.getStr("url")) );
				states.addState( fileState );
			}

		}*/


		state.putInfo( "start", index );
		state.putInfo( "total", da.getTotalRow());
		
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
