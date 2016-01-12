package com.example.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class AutoCleanService extends Service {

	private ScreenOffReceiver receiver;
	private ActivityManager am;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		receiver=new ScreenOffReceiver();
		//注册广播接收者   action 屏幕关闭
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver=null;
	}
	/**
	 * 锁屏广播接收
	 */
	private class ScreenOffReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("ScreenOffReceiver", "接收到锁屏广播");
			List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
			for(RunningAppProcessInfo info:infos){
				am.killBackgroundProcesses(info.processName);
			}
			
		}
		
	}

}
