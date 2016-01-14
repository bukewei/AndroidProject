package com.example.mobilesafe.service;

import java.util.List;

import com.example.mobilesafe.EnterPwdActivity;
import com.example.mobilesafe.db.dao.ApplockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
/**
 * 监控系统程序的运行状态
 */
public class WatchDogService extends Service {
	
	private ActivityManager am;
	private boolean flag;
	private String tempStopProtectPackname;
	private ScreenOffReceover offReceover;
	private InnerReceiver innerReceiver;
	private DataChangeReceiver dataChangeReceiver;
	
	private ApplockDao dao;
	private List<String> protectPacknames;
	private Intent intent;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}
	/**
	 * 接收锁屏广播
	 */
	private class ScreenOffReceover extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//临时停止保护等于空
			tempStopProtectPackname=null;
			System.out.println("锁屏事件，临时停止保护等于空 "+tempStopProtectPackname);
		}
	}
	/**
	 * 接收自定义广播
	 */
	private class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//接收临时停止保护的包名
			tempStopProtectPackname=intent.getStringExtra("packname");
			System.out.println("接收到了临时停止保护的广播事件"+tempStopProtectPackname);
		}
	}
	/**
	 * 数据库内容变化
	 */
	private class DataChangeReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("数据库的内容发生变化了。。。");
			//重新获取程序锁列表
			protectPacknames=dao.findAll();
		}
	}
	
	@Override
	public void onCreate() {
		offReceover=new ScreenOffReceover();
		registerReceiver(offReceover, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		innerReceiver=new InnerReceiver();
		registerReceiver(innerReceiver,new IntentFilter("com.example.mobilesafe.tempstop"));
		dataChangeReceiver=new DataChangeReceiver();
		registerReceiver(dataChangeReceiver, new IntentFilter("com.example.mobilesafe.applockchange"));
		
		am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		dao=new ApplockDao(this);
		protectPacknames=dao.findAll();
		flag=true;
		//打开输入密码界面意图
		intent=new Intent(getApplicationContext(),EnterPwdActivity.class);
		// 服务是没有任务栈信息的，在服务开启activity，要指定这个activity运行的任务栈
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//FLAG_ACTIVITY_NEW_TASK：可以运行在自己及新的任务栈
		new Thread(){
			public void run(){
				//不停的循环
				while(flag){
					//获取正在运行的任务栈                参数返回的个数
					List<RunningTaskInfo> infos=am.getRunningTasks(1);
					//用户最近操作的任务栈      界面     包名
					String packname=infos.get(0).topActivity.getPackageName();
	//				System.out.println("当前用户操作的应用程序:" + packname);// 找到了当前用户操作程序的包名
					//if (dao.find(packname)) {//查询数据库太慢了，消耗资源，改成查询内存
					if(protectPacknames.contains(packname)){
						//判断这个应用程序是否需要临时停止保护
						if(packname.equals(tempStopProtectPackname)){
					//		System.out.println("临时保护:"+tempStopProtectPackname);
						}else{
							//当前应用程序需要保护  弹出输入密码界面  
							//设置要保护程序的包名
							intent.putExtra("packname",packname);
							//打开输入密码的界面
							startActivity(intent);
						}
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		System.out.println("看门狗已退出");
		flag=false;
		unregisterReceiver(innerReceiver);
		innerReceiver=null;
		unregisterReceiver(offReceover);
		offReceover=null;
		unregisterReceiver(dataChangeReceiver);
		dataChangeReceiver=null;
		super.onDestroy();
	}
	

}
