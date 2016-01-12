package com.example.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.example.mobilesafe.R;
import com.example.mobilesafe.receiver.MyWidget;
import com.example.mobilesafe.utils.SystemInfoUtils;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;
/**
 * 更新窗口小部件服务
 */
public class UpdateWidgetService extends Service {
	
	private static final String TAG="UpdateWidgetService";
	private Timer timer;
	private TimerTask task;
	/**
	 * widget的管理器
	 */
	private AppWidgetManager awm;
	
	private ScreenOffReceiver offReceiver;
	private ScreenOnReceiver onReceiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}
	
	private class ScreenOffReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO 自动生成的方法存根
			Log.i(TAG, "屏幕锁屏了");
			//关闭计时器  节省电量
			stopTimer();
		}
		
	}
	
	private class ScreenOnReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "屏幕解锁了");
			//开启计时器
			startTimer();
		}

	}
	
	@Override
	public void onCreate() {
		onReceiver=new ScreenOnReceiver();
		offReceiver=new ScreenOffReceiver();
		//注册接收者  监听屏幕开启广播
		registerReceiver(onReceiver,new IntentFilter(Intent.ACTION_SCREEN_ON));
		//注册接收者  监听屏幕关闭广播
		registerReceiver(offReceiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
		awm=AppWidgetManager.getInstance(this);
		startTimer();
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		unregisterReceiver(offReceiver);
		unregisterReceiver(onReceiver);
		offReceiver=null;
		onReceiver=null;
		stopTimer();
	}
	
	
	private void startTimer() {
		//防止重复开启
		if(timer == null && task == null){
			timer=new Timer();
			task=new TimerTask() {
				
				@Override
				public void run() {
					Log.i(TAG, "更新widget");
					//设置更新的组件
					ComponentName provider=new ComponentName(UpdateWidgetService.this,MyWidget.class);
					RemoteViews views=new RemoteViews(getPackageName(),R.layout.process_widget);
					views.setTextViewText(R.id.process_count,"正在运行的进程："+SystemInfoUtils.getRunningProcessCount(getApplicationContext())+"个");
					long size=SystemInfoUtils.getAvailMem(getApplicationContext());
					views.setTextViewText(R.id.process_memory,"可用内存："+Formatter.formatFileSize(getApplicationContext(), size));
					// 描述一个动作,这个动作是由另外的一个应用程序执行的.
					// 自定义一个广播事件,杀死后台进度的事件
					Intent intent=new Intent();
					intent.setAction("com.example.mobilesafe.killall");
					//延期的意图
					PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
					//点击事件
					views.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);
					awm.updateAppWidget(provider, views);
					
				}
			};
			timer.schedule(task, 0,3000);
		}
		
	}
	
	
	public void stopTimer() {
		if(timer != null && task != null){
			//停止计时器
			timer.cancel();
			task.cancel();
			timer=null;
			task=null;
		}
		
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
