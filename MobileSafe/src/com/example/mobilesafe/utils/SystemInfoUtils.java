package com.example.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * 系统信息的工具类 
 */
public class SystemInfoUtils {
	/**
	 * 获取正在运行的进程的数量
	 * @param context  上下文
	 * @return         返回正在运行的进程的数量  int类型
	 */
	public static int getRunningProcessCount(Context context){
		//PackageManager   包管理器 相当于程序管理器。静态的内容。
		//ActivityManager  进程管理器。管理的手机的活动信息。动态的内容。
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
		return infos.size();
	}
	/**
	 * 获取手机可用的剩余内存
	 * @param context  上下文
	 * @return  返回手机可用的剩余内存 long类型
	 */
	public static long getAvailMem(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo=new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
	
	
	/**
	 * 获取手机可用的总内存
	 * @param context  上下文
	 * @return  返回手机可用的总内存
	 */
	public static long getTotalMen(Context context){
		//低版本不支持  最低4.0
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo = new MemoryInfo();
//		am.getMemoryInfo(outInfo);
//		return outInfo.totalMem;
		
		//读取系统记录内存信息的文件 获取内存信息
		try {
			File file=new File("proc/meminfo");
			FileInputStream fis=new FileInputStream(file);
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			String line=br.readLine();//读取一行
			//MemTotal:         513000 kB
			StringBuilder sBuilder=new StringBuilder();
			for(char c:line.toCharArray()){
				//判断是不是数字
				if(c>='0' && c<='9'){
					sBuilder.append(c);
				}
			}

			return Long.parseLong(sBuilder.toString())*1024;
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
}
