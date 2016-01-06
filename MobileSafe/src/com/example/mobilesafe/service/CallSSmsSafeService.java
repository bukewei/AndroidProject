package com.example.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
			Method method=clazz.getDeclaredMethod("getService",String.class);
			IBinder ibinder=(IBinder) method.invoke(null,TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(ibinder).endCall();
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 		
		
	}
	
}
