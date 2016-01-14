package com.example.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
/**
 * 流量管理
 */
public class TrafficManagerActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_manager);
		
		//1.获取一个包管理器
		PackageManager pm=getPackageManager();
		//2.遍历手机操作系统 获取所有程序的uid
		List<ApplicationInfo> applicationInfos=pm.getInstalledApplications(0);
		for(ApplicationInfo info:applicationInfos){
			int uid=info.uid;
			//proc/uid_stat/10086
			long tx=TrafficStats.getUidTxBytes(uid);//发送的 上传的流量
			long rx=TrafficStats.getUidRxBytes(uid);//下载的流量
			//方法返回值 -1 代表的是应用程序没有产生流量 或者操作系统不支持流量统计
		}
		TrafficStats.getMobileTxBytes();//获取手机4g/3g/2g网络上传的总流量
		TrafficStats.getMobileRxBytes();//获取手机4g/3g/2g网络下载的总流量
		
		TrafficStats.getTotalTxBytes();//手机全部网络接口的上传总流量（包括wifi和移动流量）
		TrafficStats.getTotalRxBytes();//手机全部网络接口的下载总流量（包括wifi和移动流量）
		
		
	}
	
	
	
	
	
}
