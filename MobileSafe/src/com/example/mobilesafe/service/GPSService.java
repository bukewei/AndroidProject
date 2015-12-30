package com.example.mobilesafe.service;

import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSService extends Service {
	
	private LocationManager lm;
	private MyLocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
		
		lm=(LocationManager) getSystemService(LOCATION_SERVICE);
		listener=new MyLocationListener();
		//注册监听位置服务
		//给位置提供者设置条件
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//设置参数细化：
		//criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置为最大精度 
		//criteria.setAltitudeRequired(false);//不要求海拔信息 
		//criteria.setBearingRequired(false);//不要求方位信息 
		//criteria.setCostAllowed(true);//是否允许付费 
		//criteria.setPowerRequirement(Criteria.POWER_LOW);//对电量的要求 
		//获取最好的位置提供者
		String proveder=lm.getBestProvider(criteria, true);
		
		System.out.println("当前最好的位置提供者："+proveder);
		
//		提供者提供者的名称来注册
//		minTime位置更新之间的最小时间间隔,以毫秒为单位    一般6秒
//		minDistance位置更新之间的最小距离,在米               一般50米
//		侦听器的LocationListener LocationListener。onLocationChanged方法将呼吁每个位置更新
	//	lm.requestLocationUpdates(proveder,60000,50,listener);
		lm.requestLocationUpdates(proveder,0,0,listener);
	}
	
	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		//取消监听位置服务
		lm.removeUpdates(listener);
		listener=null;
	}
	
	class MyLocationListener implements LocationListener{
		/**
		 * 当位置改变时回调
		 */
		@Override
		public void onLocationChanged(Location location) {
			String longitude="经度："+location.getLongitude();
			String latitude="纬度："+location.getLatitude();
			String accuracy="精确度："+location.getAccuracy();
			
			//把标准坐标转换成火星坐标
			try {
				InputStream iStream=getAssets().open("axisoffset.dat");
				ModifyOffset offset=ModifyOffset.getInstance(iStream);
				PointDouble double1=offset.s2c(new PointDouble(location.getLongitude(),location.getLatitude()));
				String longitudeHX="经度："+offset.X;
				String latitudeHX="纬度："+offset.Y;
				System.out.println(longitudeHX+"\n"+latitudeHX);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			SharedPreferences sp=getSharedPreferences("config",MODE_PRIVATE);
			Editor editor=sp.edit();
			editor.putString("lastlocation",longitude+"\n"+latitude+"\n"+accuracy);
			System.out.println(longitude+"\n"+latitude+"\n"+accuracy);
			editor.commit();
			
		}
		/**
		 * 当状态发生改变时回调；比如 开启，关闭
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO 自动生成的方法存根
			
		}
		/**
		 * 当某一个位置提供者可以使用时调用
		 */
		@Override
		public void onProviderEnabled(String provider) {
			// TODO 自动生成的方法存根
			
		}
		/**
		 * 当某一个位置提供者不可以使用时调用
		 */
		@Override
		public void onProviderDisabled(String provider) {
			// TODO 自动生成的方法存根
			
		}
		
	}
	

}
