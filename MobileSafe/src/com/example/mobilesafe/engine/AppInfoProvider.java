package com.example.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.domain.AppInfo;

import android.content.Context;
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
			//packageInfo 相当于一个应用程序apk包的清单文件
			String packname=packageInfo.packageName;
			Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
			String name=packageInfo.applicationInfo.loadLabel(pm).toString();
			AppInfo appInfo=new AppInfo();
			appInfo.setPackname(packname);
			appInfo.setIcon(icon);
			appInfo.setAppname(name);
			appInfos.add(appInfo);
			
		}
		
		return appInfos;
	}
}
