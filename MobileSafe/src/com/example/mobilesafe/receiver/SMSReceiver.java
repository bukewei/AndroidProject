package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
	private static final String TAG="SMSReceiver";
	private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
//		Log.i(TAG, "-----SMSReceiver------");
		//接收短信
		Object[] objs=(Object[]) intent.getExtras().get("pdus");
		sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
		for(Object b:objs){
			//具体的某一条短信
			SmsMessage sms=SmsMessage.createFromPdu((byte[]) b);
			//发送者
			String sender=sms.getOriginatingAddress();
			//取出安全号码
			String safenumber=sp.getString("safenumber","");
//			Log.i(TAG, "-----发送者------"+sender);
			Toast.makeText(context, "-----发送者------"+sender, 0).show();
			//获取短信内容
			String body=sms.getMessageBody();
			//如果发送者的号码里包含保存的安全号码
			if(sender.contains(safenumber)){
				if("#*location*#".equals(body)){
					Log.i(TAG, "----得到手机的GPS------");
					Toast.makeText(context, "----得到手机的GPS------", 0).show();
					//终止广播
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					Log.i(TAG, "----播放报警音乐------");
					
					//终止广播
					abortBroadcast();
				}else if ("#*wipedata*#".equals(body)) {
					Log.i(TAG, "----远程清楚手机数据------");
					
					//终止广播
					abortBroadcast();
				}else if ("#*lockscreen*#".equals(body)) {
					Log.i(TAG, "----远程锁屏------");
					
					//终止广播
					abortBroadcast();
				}
			}
		}
		
	}

}
