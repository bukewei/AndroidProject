package com.example.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * 业务方法，提供手机里面安装的所有的应用程序信息
 */
public class AppInfoProvider {
	/**
	 * 获取所有的安装的应用程序信息
	 * @param context 上下文
	 * @return  返回AppInfo对象集合
	 */
	public static List<AppInfo> getAppInfos(Context context){
		//返回PackageManager实例 找到全局包信息。
		PackageManager pm=context.getPackageManager();
		//所有安装在系统上的应用程序包信息
		List<PackageInfo> packageInfos=pm.getInstalledPackages(0);
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		for(PackageInfo packageInfo : packageInfos){
			AppInfo appInfo=new AppInfo();
			//packageInfo 相当于一个应用程序apk包的清单文件
			String packname=packageInfo.packageName;
			Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
			String name=packageInfo.applicationInfo.loadLabel(pm).toString();
			//应用程序的标记  
			int flags=packageInfo.applicationInfo.flags;
			//不同的数相与等于0
			if((flags & ApplicationInfo.FLAG_SYSTEM)==0){
				//用户程序
				appInfo.setUserApp(true);
			}else{
				//系统程序
				appInfo.setUserApp(false);
			}
			
			if((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0){
				//安装手机的内存
				appInfo.setInRom(true);
			}else{
				//安装在手机的外部存储设备
				appInfo.setInRom(false);
			}
			
			appInfo.setPackname(packname);
			appInfo.setIcon(icon);
			appInfo.setAppname(name);
			appInfos.add(appInfo);
			
		}
		
		return appInfos;
	}
}
