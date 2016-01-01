package com.example.mobilesafe;

import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.ui.SettingItemView;
import com.example.mobilesafe.utils.ServiceUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	private SettingItemView siv_update;
	private SettingItemView siv_show_address;
	private Intent showAddress;
	private SharedPreferences sp;
	
	/**
	 * 重新开启Activity
	 */
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		showAddress=new Intent(this,AddressService.class);
		boolean isServiceRunning=ServiceUtils.isServiceRunning(SettingActivity.this,"com.example.mobilesafe.service.AddressService");
		if(isServiceRunning){
			//服务是开启状态
			siv_show_address.setChecked(true);
		}else{
			//服务是关闭状态
			siv_show_address.setChecked(false);
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		sp=getSharedPreferences("config",MODE_PRIVATE);
		siv_update=(SettingItemView) findViewById(R.id.siv_update);
		siv_show_address=(SettingItemView) findViewById(R.id.siv_show_address);
		
		
		boolean update=sp.getBoolean("update", false);
		if(update){
			//自动升级开启
			siv_update.setChecked(update);
	//		siv_update.setDesc(update);
		}else{
			//自动升级已关闭
			siv_update.setChecked(update);
//			siv_update.setDesc(update);
//			siv_update.setStatus(update);
		}
		
		siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor=sp.edit();
				//判断是否打开自动升级
				if(siv_update.isChecked()){
					//已经打开了自动升级，现在关闭
					siv_update.setChecked(false);
//					siv_update.setDesc(false);
//					siv_update.setStatus(false);
					editor.putBoolean("update",false);
				}else{
					//没有打开自动升级，现在打开
					siv_update.setChecked(true);
//					siv_update.setDesc(true);
//					siv_update.setStatus(true);
					editor.putBoolean("update",true);
				}
				//提交
				editor.commit();
			}
		});
		
		
		showAddress=new Intent(this,AddressService.class);
		boolean isServiceRunning=ServiceUtils.isServiceRunning(SettingActivity.this,"com.example.mobilesafe.service.AddressService");
		if(isServiceRunning){
			//服务是开启状态
			siv_show_address.setChecked(true);
		}else{
			//服务是关闭状态
			siv_show_address.setChecked(false);
		}
		//点击事件
		siv_show_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(siv_show_address.isChecked()){
					//变为非选中状态
					siv_show_address.setChecked(false);
					stopService(showAddress);
				}else{
					//变为选中状态
					siv_show_address.setChecked(true);
					startService(showAddress);
				}
			}
		});
		
		
	}
	
	
	
	
}
