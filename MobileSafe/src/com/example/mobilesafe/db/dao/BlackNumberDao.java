package com.example.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.db.BlackNumberDBOpenHelper;
import com.example.mobilesafe.domain.BlackNumberInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 黑名单数据库增删改查业务类
 */
public class BlackNumberDao {

	private static final String DB_NAME="blacknumber";
	
	private BlackNumberDBOpenHelper helper;
	/**
	 * 构造方法
	 * @param context 上下文
	 */
	public BlackNumberDao(Context context){
		helper=new BlackNumberDBOpenHelper(context);
	}
	/**
	 *  插询黑名单号码是否存在
	 * @param number  号码
	 * @return   存在返回真，反之返回假
	 */
	public boolean find(String number){
		boolean result=false;
		//获取一个可写的数据库
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.rawQuery("SELECT * FROM blacknumber WWHERE number=?",new String[]{number});
		if(cursor.moveToNext()){
			result=true;
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * 查询黑名单号码的拦截模式
	 * @param number 号码
	 * @return  返回拦截模式的字符串
	 */
	public String findMode(String number){
		String result=null;
		//获取一个可写的数据库
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.rawQuery("SELECT mode FROM blacknumber WWHERE number=?",new String[]{number});
		if(cursor.moveToNext()){
			result=cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * 查询所有的黑名单号码 包括模式
	 * @return  返回黑名单号码信息集合
	 */
	public List<BlackNumberInfo> findAll(){
		List<BlackNumberInfo> result=new ArrayList<BlackNumberInfo>();
		//获取一个可写的数据库
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.rawQuery("SELECT number,mode FROM blacknumber order by _id desc",null);
		while(cursor.moveToNext()){
			BlackNumberInfo info=new BlackNumberInfo();
			String number=cursor.getString(0);
			String mode=cursor.getString(1);
			info.setMode(mode);
			info.setNumber(number);
			result.add(info);
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * 添加黑名单号码
	 * @param number 号码
	 * @param mode   拦截模式
	 * @return 新插入行数的id 错误返回-1
	 */
	public long add(String number,String mode){
		//获取一个可写的数据库
		SQLiteDatabase db=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("number",number);
		values.put("mode",mode);
		long id=db.insert(DB_NAME,null, values);
		db.close();
		return id;
	}
	/**
	 * 修改黑名单号码
	 * @param number 号码
	 * @param mode   拦截模式
	 * @return  返回受影响的行数
	 */
	public int update(String number,String mode){
		//获取一个可写的数据库
		SQLiteDatabase db=helper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("mode",mode);
		int rows=db.update(DB_NAME, values, "number=?",new String[]{number});
		db.close();
		return rows;
	}
	
	/**
	 * 删除黑名单号码
	 * @param number 号码
	 * @return  返回受影响的行数
	 */
	public int delete(String number){
		//获取一个可写的数据库
		SQLiteDatabase db=helper.getWritableDatabase();
		int rows=db.delete(DB_NAME, "number=?",new String[]{number});
		db.close();
		return rows;
	}
	
}
