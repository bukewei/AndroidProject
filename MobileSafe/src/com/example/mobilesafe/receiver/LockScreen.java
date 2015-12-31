package com.example.mobilesafe.receiver;


import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class LockScreen {
		
	/**
	 * 一键锁屏
	 * @param view
	 */
	public static void lockscreen(Context context){
		//设备策咯服务		 
		DevicePolicyManager	dpm=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName who=new ComponentName(context,MyAdmin.class);
		//判断是不是管理员
		if(dpm.isAdminActive(who)){
			dpm.lockNow();//锁屏
	//		dpm.resetPassword("",0);//设置屏幕密码      设置为空时是取消密码
			//清除SD卡上的数据
//			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
			//恢复出厂设置
//			dpm.wipeData(0);
		}else{
			Toast.makeText(context,"还没有打开管理员权限",0).show();
			return;
		}
	}
	/**
	 * 清除数据，恢复出厂设置
	 */
	public  static void wipeData(Context context){
		//设备策咯服务		 
		DevicePolicyManager	dpm=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName who=new ComponentName(context,MyAdmin.class);
		//判断是不是管理员
		if(dpm.isAdminActive(who)){
//			dpm.lockNow();//锁屏
	//		dpm.resetPassword("",0);//设置屏幕密码      设置为空时是取消密码
			//清除SD卡上的数据
//			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
			//恢复出厂设置
//			dpm.wipeData(0);
			Toast.makeText(context,"数据已清除",0).show();
		}else{
			Toast.makeText(context,"还没有打开管理员权限",0).show();
			return;
		}
	}
	
	
	/**
	 * 卸载当前软件
	 * @param view
	 */
	public static void uninstall(Context context){
		//设备策咯服务		 
		DevicePolicyManager	dpm=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		//1.先清除管理员权限
		ComponentName mDeviceAdminSample=new ComponentName(context,MyAdmin.class);
		dpm.removeActiveAdmin(mDeviceAdminSample);
		
		//2.普通应用的卸载
		Uri packageURI = Uri.parse("package:" + context.getPackageName());
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}
	
}
