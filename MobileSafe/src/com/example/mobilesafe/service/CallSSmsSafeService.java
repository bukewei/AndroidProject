package com.example.mobilesafe.service;

import com.example.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

public class CallSSmsSafeService extends Service {
	
	private static final String TAG="CallSSmsSafeService";
	private BlackNumberDao dao;
	private InnerSmsReceiver receiver;
	
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
	}
}
