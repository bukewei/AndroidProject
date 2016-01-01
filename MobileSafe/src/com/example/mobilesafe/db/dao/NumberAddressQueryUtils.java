package com.example.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

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
		
		//手机号码的正则表达式
		if(number.matches("^1[34568]\\d{9}$")){
			//是手机号码
			String  sql="SELECT location FROM data2 WHERE id=(SELECT outkey FROM data1 WHERE id=?)";
			//number.substring(0,7);截取文本的第一位到第六位
			Cursor cursor=database.rawQuery(sql, new String[]{number.substring(0,7)});
			while(cursor.moveToNext()){
				String location=cursor.getString(0);
				address=location;
			}
			cursor.close();
			
		}else{
			//其他号码
			switch (number.length()) {
			case 3:
				// 110
				address = "匪警号码";
				break;
			case 4:
				// 5554
				address = "模拟器";
				break;
			case 5:
				// 10086
				address = "客服电话";
				break;
			case 7:
				//
				address = "本地号码";
				break;

			case 8:
				address = "本地号码";
				break;
			default:
				//长度大于10  0开头
				if(number.length() > 10 && number.startsWith("0")){
					String sql="SELECT location FROM data2 WHERE area=?";
					String location="";
					Cursor cursor=database.rawQuery(sql, new String[]{number.substring(1,3)});
					while(cursor.moveToNext()){
						location=cursor.getString(0);
					}
					
					if(TextUtils.isEmpty(location)){
						cursor=database.rawQuery(sql, new String[]{number.substring(1,4)});
						while(cursor.moveToNext()){
							location=cursor.getString(0);
						}
					}
					cursor.close();
					address=location.substring(0,location.length()-2);
					
				}
				break;
			}
		}
		
		
		return address;
	}
}
