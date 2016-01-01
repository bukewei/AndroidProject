package com.example.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {

	/**
	 * 校验某个服务是否还在运行
	 * @param context      上下文
	 * @param serviceName  服务的名字
	 * @return  		         还在运行返回真，否则返回假
	 */
	public static boolean isServiceRunning(Context context,String serviceName){
	
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//最多返回100个服务的信息
		List<RunningServiceInfo> infos=am.getRunningServices(100);
		for(RunningServiceInfo info : infos){
			String name=info.service.getClassName();
			if(serviceName.equals(name)){
				return true;
			}
		}
		
		return false;
	}
}
