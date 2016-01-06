package com.example.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallSSmsSafeService extends Service {
	
	private static final String TAG="CallSSmsSafeService";
	private BlackNumberDao dao;
	private InnerSmsReceiver receiver;
	private TelephonyManager tm;
	private MyListener listener;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}
	/**
	 * 继承广播服务
	 */
	private class InnerSmsReceiver extends BroadcastReceiver{
		/**
		 * 接收到广播
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "内部广播接收者，接收到广播，短信到来");
			//检查发件人是否是黑名单号码，设置短信全部拦截
			Object[] objs=(Object[]) intent.getExtras().get("pdus");
			for(Object obj : objs){
				SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) obj);
				//得到短信发件人
				String sender=smsMessage.getOriginatingAddress();
				Log.i(TAG, "短信发件人："+sender);
				//查看拦截模式
				String result=dao.findMode(sender);
				if("2".equals(result) || "0".equals(result)){
					Log.i(TAG, "拦截短信");
					//终止广播  (拦截短信)
					abortBroadcast();
				}
				//获得短信内容
				String body=smsMessage.getMessageBody();
				Log.i(TAG, "短信内容："+body);
				//真正的拦截要进行分词，这里只是演示代码
				if(body.contains("拦截关键字") || body.contains("lanjie")){
					//终止广播  (拦截短信)
					abortBroadcast();
					Log.i(TAG, "进行关键字拦截,短信广播已终止");
				}
			}
			
		}
		
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		// 服务被创建 进行初始化
		dao=new BlackNumberDao(this);
		//得到电话管理服务
		tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener=new MyListener();
		//监听电话状态
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		receiver=new InnerSmsReceiver();
		//代码注册短信广播接收者
		IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		
		//设置优先级
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		//注册接收者
		registerReceiver(receiver, filter);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver=null;
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
	}
	/**
	 *  电话状态监听器
	 */
	private class MyListener extends PhoneStateListener{
		/**
		 * 呼叫状态改变
		 */
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO 自动生成的方法存根
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://响铃状态
				String result=dao.findMode(incomingNumber);//查询拦截模式
				if("1".equals(result) || "0".equals(result)){
					Log.i(TAG, "黑名单号码，挂断电话");
					
				//	删除呼叫记录有时成功，有时不成功的情况；
				//	立刻把电话挂断了，但呼叫的生成并不是同步的代码；它是一个异步的代码。
				//  用观察者去监听日志产生后再去删除
				//	观察呼叫记录数据库内容的变化
					Uri uri=Uri.parse("content://call_log/calls");
					//注册内容观察者
					getContentResolver().registerContentObserver(uri, true, new CallLogObserver(incomingNumber, new Handler()));
					
					endCall();
				}
				break;

			default:
				break;
			}
			
			super.onCallStateChanged(state, incomingNumber);
		}

	}
	
	public void endCall() {
		//特殊方法调用系统源码
		try {
			
			//加载servicemanager的字节码
			Class clazz=CallSSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
			//得到字节码的方法
			Method method=clazz.getDeclaredMethod("getService",String.class);
			//调用方法得到远程服务代理类
			IBinder ibinder=(IBinder) method.invoke(null,TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(ibinder).endCall();
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 		
		
	}
	/**
	 * 继承内容观察者
	 */
	private class CallLogObserver extends ContentObserver{
		
		private String incomingNumber;//呼入的号码
		
		public CallLogObserver(String incomingNumber,Handler handler) {
			super(handler);
			
			this.incomingNumber=incomingNumber;
		}
		/**
		 * 呼叫记录，数据库内容发生变化
		 */
		@Override
		public void onChange(boolean selfChange) {
			// TODO 自动生成的方法存根
			super.onChange(selfChange);
			Log.i(TAG, "数据库变化了，产生了呼叫记录");
			//注销内容观察者
			getContentResolver().unregisterContentObserver(this);
			deleteCallLog(incomingNumber);
		}
	}
	
	
	/**
	 * 利用内容提供者删除呼叫记录
	 * @param incomingNumber  呼入的号码
	 */
	public void deleteCallLog(String incomingNumber){
		//得到内容提供者
		ContentResolver resolver=getContentResolver();
		//呼叫记录uri的路径
		Uri uri=Uri.parse("content://call_log/calls");//得到全部的呼叫记录
		int row = resolver.delete(uri, "number=?",new String[]{incomingNumber});
		Log.i(TAG, "记录被删除，删除的行数："+row+"行");
	}
	
	
}
