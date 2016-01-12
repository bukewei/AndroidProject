package com.example.mobilesafe.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 杀死所有广播接收者
 */
public class KillAllRecevier extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("自定义广播接收到了，杀死所有广播接收者");
		ActivityManager am=(ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
		for(RunningAppProcessInfo info:infos){
			am.killBackgroundProcesses(info.processName);
		}
	}

}
