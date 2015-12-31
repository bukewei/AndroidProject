package com.example.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {
	
	private static String path="data/data/com.example.mobilesafe/files/address.db";
	
	/**
	 * 用号码查询数据库，返回号码的归属地
	 * @param number  要查询的号码
	 * @return  查询后得到的归属地字符串
	 */
	public static String queryNumber(String number) {
		String address=number;
		// path 把address.db这个数据库拷贝到data/data/《包名》/files/address.db
		SQLiteDatabase database=SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READONLY);
		String  sql="SELECT location FROM data2 WHERE id=(SELECT outkey FROM data1 WHERE id=?)";
		//number.substring(0,7);截取文本的第一位到第六位
		Cursor cursor=database.rawQuery(sql, new String[]{number.substring(0,7)});
		while(cursor.moveToNext()){
			String location=cursor.getString(0);
			address=location;
		}
		cursor.close();
		
		return address;
	}
}
