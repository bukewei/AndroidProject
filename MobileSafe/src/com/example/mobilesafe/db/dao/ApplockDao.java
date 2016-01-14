package com.example.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.db.ApplockDBOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 程序锁的dao
 */
public class ApplockDao {
	private ApplockDBOpenHelper helper;
	private Context context;
	
	/**
	 * 构造方法
	 * @param context 上下文
	 */
	public ApplockDao(Context context){
		helper=new ApplockDBOpenHelper(context);
		this.context=context;
	}
	/**
	 * 添加一个要锁定应用程序的包名
	 * @param packname 包名
	 */
	public void add(String packname){
		SQLiteDatabase db=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("packname", packname);
		db.insert("applock",null,values);
		db.close();
		sendApplockchangeBroadcast();
		//context.getContentResolver().notifyChange(uri, observer);
	}
	/**
	 * 删除一个要锁定应用程序的包名
	 * @param packname
	 */
	public void delete(String packname){
		SQLiteDatabase db=helper.getWritableDatabase();
		db.delete("applock","packname=?",new String[]{packname});
		db.close();
		sendApplockchangeBroadcast();
	}
	
	/**
	 * 发送数据库内容改变广播
	 */
	private void sendApplockchangeBroadcast() {
		Intent intent=new Intent();
		intent.setAction("com.example.mobilesafe.applockchange");
		context.sendBroadcast(intent);
	}
	/**
	 * 查询一条程序锁包名是否存在
	 * @param packname
	 * @return
	 */
	public boolean find(String packname){
		boolean result=false;
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.query("applock",null,"packname=?",new String[]{packname}, null,null,null);
		if(cursor.moveToNext()){
			result=true;
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * 查询并返回全部的包名
	 * @return List<String>
	 */
	public List<String> findAll(){
		List<String> protectPackname=new ArrayList<String>();
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.query("applock",new String[]{"packname"}, null,null,null,null,null);
		while(cursor.moveToNext()){
			protectPackname.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return protectPackname;
	}
	
}
