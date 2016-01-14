package com.example.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ApplockDBOpenHelper extends SQLiteOpenHelper {
	/**
	 * 数据库的构造方法  数据库名：applock.db   版本：1
	 * @param context
	 */
	public ApplockDBOpenHelper(Context context) {
		super(context, "applock.db",null,1);
	}
	/**
	 * 初始化数据库的表结构   第一次创建时调用
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="CREATE TABLE applock (_id INTEGER PRIMARY KEY AUTOINCREMENT,packname VARCHAR(20))";
		db.execSQL(sql);
	}
	/**
	 * 数据库版本变化时调用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
