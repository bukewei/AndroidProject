package com.example.mobilesafe;


import com.example.mobilesafe.receiver.MyAdmin;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	
	private SharedPreferences sp;
	private TextView tv_safenumber;
	private TextView tv_protecting;
	private TextView tv_shebei;
	private ImageView iv_protecting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp=getSharedPreferences("config",MODE_PRIVATE);
		//判断是否设置过手机向导
		boolean configed=sp.getBoolean("configed",false);
		if(configed){
			//设置过的就进入手机防盗页面
			setContentView(R.layout.activity_lost_find);
			tv_safenumber=(TextView) findViewById(R.id.tv_safenumber);
			tv_protecting=(TextView) findViewById(R.id.tv_protecting);
			tv_shebei=(TextView) findViewById(R.id.tv_shebei);
			iv_protecting=(ImageView) findViewById(R.id.iv_protecting);
			//得到设置过的安全号码
			String safeNumber=sp.getString("safenumber", "");
			tv_safenumber.setText(safeNumber);
			
			//设备管理是否开启
			DevicePolicyManager	dpm=(DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
			ComponentName shebeiguanli=new ComponentName(this,MyAdmin.class);
			//判断是不是管理员
			if(dpm.isAdminActive(shebeiguanli)){
				tv_shebei.setText("设备管理已开启");
			}else{
				tv_shebei.setText("打开设备管理");
			}
			
			//根据防盗保护状态设置图片
			boolean protecting=sp.getBoolean("protecting", false);
			if(protecting){
				//已开启
				tv_protecting.setText("防盗保护已开启");
				iv_protecting.setImageResource(R.drawable.lock);
			}else{
				//没有开启
				tv_protecting.setText("防盗保护未开启");
				iv_protecting.setImageResource(R.drawable.unlock);
			}
		}else {
			//进入手机设置向导
			enterSetup();
		}
		
		
	}
	
	/**
	 * TextView点击事件    重新进入设置向导
	 * @param view
	 */
	public void reEnterSetup(View view){
		enterSetup();
	}
	
	
	/**
	 * 进入手机设置向导
	 */
	private void enterSetup() {
		Intent intent=new Intent(LostFindActivity.this,Setup1Activity.class);
		startActivity(intent);
		//关闭当前页
		finish();
	}
	/**
	 * 打开设备管理
	 * @param view
	 */
	public void openAdmin(View view){
		//创建一个Intent
		Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		//要激活谁
		ComponentName mDeviceAdminSample=new ComponentName(this,MyAdmin.class);
		
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceAdminSample);
		//劝说用户开启管理员权限
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"开启锁屏,用于远程锁屏。");
		startActivity(intent);
		
	}
	
}
