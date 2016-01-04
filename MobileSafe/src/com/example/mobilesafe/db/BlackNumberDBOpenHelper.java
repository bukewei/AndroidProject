package com.example.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {
	/**
	 * 数据库创建的构造方法 数据库名称 blacknumber.db
	 * @param context 上下文
	 */
	public BlackNumberDBOpenHelper(Context context) {
		super(context, "blacknumber.db",null,1);
		// TODO 自动生成的构造函数存根
	}
	/**
	 * 数据库创建时调用
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="CREATE TABLE blacknumber(_id INTEGER PRIMARY KEY AUTOINCREMENT,number VARCHAR(20),mode VARCHAR(2))";
		db.execSQL(sql);
	}
	/**
	 * 数据库修改时调用  比如修改版本号
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自动生成的方法存根

	}

}
