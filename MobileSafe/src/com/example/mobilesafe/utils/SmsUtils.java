package com.example.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

/**
 * 短信的工具类
 */
public class SmsUtils {
	/**
	 * 备份短信的回调接口
	 */
	public interface BackUpCallBack{
		/**
		 * 开始备份的时候，设置进度的最大值
		 * @param max   总进度
		 */
		public void beforeBackup(int max);
		/**
		 * 备份过程中，增加的进度
		 * @param progress  当前进度
		 */
		public void onSmsBackup(int progress);
		
	}
	/**
	 * 备份用户的短信
	 * @param context  上下文
	 * @param callBack 备份短信的接口
	 */
	public static void backupSms(Context context,BackUpCallBack callBack)throws Exception{
		
		//短信备份在外部存储设备
		File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
		FileOutputStream fos=new FileOutputStream(file);
		//把短信一条一条的读出来，按照一定的格式写到文件里
		XmlSerializer serializer=Xml.newSerializer();//获取xml文件的生成器（序列化器）
		//初始化生成器
		serializer.setOutput(fos,"utf-8");
		serializer.startDocument("utf-8",true);//文档开始
		serializer.startTag(null,"smss");//开始标签
		
		//得到内容提供者
		ContentResolver resolver=context.getContentResolver();
		Uri uri=Uri.parse("content://sms/");
		Cursor cursor=resolver.query(uri, new String[]{"body","address","type","date"},null, null, null);
		//开始备份的时候，设置进度条的最大值
		int max=cursor.getCount();
		callBack.beforeBackup(max);
		//增加sms的属性  备份的短信的个数
		serializer.attribute(null,"max",max+"");
		int process=0;//记录进度
		while(cursor.moveToNext()){
			//睡眠500毫秒
			Thread.sleep(500);
			String body=cursor.getString(cursor.getColumnIndex("body"));
			String address=cursor.getString(cursor.getColumnIndex("address"));
			String type=cursor.getString(cursor.getColumnIndex("type"));
			String date=cursor.getString(cursor.getColumnIndex("date"));
			serializer.startTag(null,"sms");
			
			serializer.startTag(null,"body");
			serializer.text(body);
			serializer.endTag(null,"body");
			
			serializer.startTag(null,"address");
			serializer.text(address);
			serializer.endTag(null,"address");
			
			serializer.startTag(null,"type");
			serializer.text(type);
			serializer.endTag(null,"type");
			
			serializer.startTag(null,"date");
			serializer.text(date);
			serializer.endTag(null,"date");
			
			serializer.endTag(null,"sms");
			//备份完一条，藤甲进度
			process++;
			callBack.onSmsBackup(process);
		}
		cursor.close();
		serializer.endTag(null, "smss");
		serializer.endDocument();//文档结束
		fos.close();
	}
	
	public interface RestoreCallBack{
		/**
		 * 开始恢复短信的时候，设置进度的最大值
		 * @param max   总进度
		 */
		public void beforeRestore(int max);
		/**
		 * 恢复过程中，增加的进度
		 * @param progress  当前进度
		 */
		public void onSmsRestore(int progress);
		
	}
	
	
	/**
	 * 恢复短信
	 * @param context 上下文
	 * @param flag    是否清理原来的短信    true：清理     false：不清理
	 */
	public static void restoreSms(Context context,boolean flag,RestoreCallBack callBack)throws Exception{
		Uri uri=Uri.parse("content://sms/");
		ContentResolver smsResolver=context.getContentResolver();
		if(flag){
			//为真，删除所有短信
			smsResolver.delete(uri, null, null);
		}
		
		//1.读取SD卡上的xml文件
		File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
		FileInputStream is=new FileInputStream(file);
		//获取一个xml文件解析器
		XmlPullParser parser=Xml.newPullParser();
		//初始化xml文件解析器
		parser.setInput(is,"utf-8");
		//得到当前解析条目的节点类型
		int evenType=parser.getEventType();
		ContentValues values=new ContentValues();
		int progress=0;
		//循环解析xml （只要节点类型不是结束节点）
		while(evenType != XmlPullParser.END_DOCUMENT){
			switch (evenType) {
			case XmlPullParser.START_TAG://开始标记
				if("smss".equals(parser.getName())){
					//获取属性的值
					int max=Integer.parseInt(parser.getAttributeValue(0));
					callBack.beforeRestore(max);
					Log.i("SmsUtils","一共有"+max+"条短信需要还原");
				}else if ("sms".equals(parser.getName())) {
					
				}else if ("body".equals(parser.getName())) {
					values.put("body",parser.nextText());
				}else if ("address".equals(parser.getName())) {
					values.put("address",parser.nextText());
				}else if ("type".equals(parser.getName())) {
					values.put("type",parser.nextText());
				}else if ("date".equals(parser.getName())) {
					values.put("date",parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG://结束标签
				if("sms".equals(parser.getName())){
					smsResolver.insert(uri, values);
					System.out.println(values.size());
					
					values.clear();
					
				}
				break;
			default:
				break;
			}
			
			progress++;
			callBack.onSmsRestore(progress);
			evenType=parser.next();
		}
		
		//2.读取max
		// 3.读取每一条短信信息，body date type address
		// 4.把短信插入到系统短息应用。
		
//		ContentValues values=new ContentValues();
//		values.put("body","短信内容");
//		values.put("date","1452086585438");
//		values.put("type","1");
//		values.put("address","5556");
//		context.getContentResolver().insert(uri, values);
		
		
	}
	
}
