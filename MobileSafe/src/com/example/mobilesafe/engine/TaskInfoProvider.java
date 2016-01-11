package com.example.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.domain.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

/**
 * 提供手机里面的进程信息
 */
public class TaskInfoProvider {
	/**
	 * 获取手机里面所有的进程信息
	 * @param context
	 * @return
	 */
	public static List<TaskInfo> getTaskInfos(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm=context.getPackageManager();
		List<RunningAppProcessInfo> processInfos=am.getRunningAppProcesses();
		List<TaskInfo> taskInfos=new ArrayList<TaskInfo>();
		for(RunningAppProcessInfo processInfo : processInfos){
			TaskInfo taskInfo=new TaskInfo();
			//应用程序的包名
			String packname=processInfo.processName;
			taskInfo.setPackname(packname);
			MemoryInfo[] memoryInfos=am.getProcessMemoryInfo(new int[]{processInfo.pid});
			long memsize=memoryInfos[0].getTotalPrivateDirty()*1024;
			taskInfo.setMemsize(memsize);
			try {
				ApplicationInfo applicationInfo=pm.getApplicationInfo(packname, 0);
				Drawable icon=applicationInfo.loadIcon(pm);
				taskInfo.setIcon(icon);
				String name=applicationInfo.loadLabel(pm).toString();
				taskInfo.setName(name);
				//不同的 相& 等于0
				if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0){
					//用户进程
					taskInfo.setUserTask(true);
				}else{
					//系统进程
					taskInfo.setUserTask(false);
				}
			} catch (NameNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
	
	
	
}
