package com.example.mobilesafe.receiver;

import com.example.mobilesafe.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {
	/**
	 * 不管是什么操作都会调用此方法
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Intent i=new Intent(context, UpdateWidgetService.class);
		context.startService(i);
	}
	/**
	 * 在xml里定义的更新时间到后调用
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
	}
	/**
	 * 创建(第一次创建)
	 */
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Intent intent=new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
	}
	/**
	 * 移除(最后一个移除)
	 */
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Intent intent=new Intent(context, UpdateWidgetService.class);
		context.stopService(intent);
	}
	
}
