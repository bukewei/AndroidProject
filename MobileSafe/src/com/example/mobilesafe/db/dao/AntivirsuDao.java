package com.example.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 病毒数据库查询业务类
 */
public class AntivirsuDao {
	private String dbPath;
	
	public AntivirsuDao(Context context) {
		this.dbPath=context.getFilesDir().getPath()+"/antivirus.db";
		System.out.println(dbPath);
		// /data/data/com.example.mobilesafe/files
		//  /data/data/com.example.mobilesafe/files/antivirus.db

	}

	/**
	 * 查询一个md5是否在病毒数据库里存在
	 * @param md5  特征码
	 * @return  存在返回true 不存在返回false
	 */
	public boolean isVirus(String md5){
	//	dbPath="/data/data/com.example.mobilesafe/files/antivirus.db";
		
		boolean result=false;
		//打开数据库
		SQLiteDatabase db=SQLiteDatabase.openDatabase(dbPath, null,SQLiteDatabase.OPEN_READONLY);
		String sql="SELECT * FROM datable WHERE md5=?";
		Cursor cursor=db.rawQuery(sql, new String[]{md5});
		if(cursor.moveToNext()){
			result=true;
		}
		cursor.close();
		db.close();
		return result;
	}
	
	
}
