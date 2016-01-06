package com.example.mobilesafe;

import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.service.CallSSmsSafeService;
import com.example.mobilesafe.ui.SettingClickView;
import com.example.mobilesafe.ui.SettingItemView;
import com.example.mobilesafe.utils.ServiceUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {
	//自动更新设置
	private SettingItemView siv_update;
	private SettingItemView siv_show_address;
	private Intent showAddress;
	private SharedPreferences sp;
	//来电归属地
	private SettingClickView scv_changebg;
	//黑名单拦截设置
	private SettingItemView siv_callsms_safe;
	private Intent callSmsSafeIntent;
	
	
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
		
		//黑名单拦截服务是否运行
		boolean isCallSmsServiceRunning=ServiceUtils.isServiceRunning(SettingActivity.this,"com.example.mobilesafe.service.CallSSmsSafeService");
		siv_callsms_safe.setChecked(isCallSmsServiceRunning);
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		sp=getSharedPreferences("config",MODE_PRIVATE);
		siv_update=(SettingItemView) findViewById(R.id.siv_update);
		
		
		
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
		
		//设置号码归属地显示控件
		siv_show_address=(SettingItemView) findViewById(R.id.siv_show_address);
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
		
		//设置号码归属地的背景
		scv_changebg=(SettingClickView) findViewById(R.id.scv_changebg);
		//设置标题
		scv_changebg.setTitle("归属地提示框风格");
		final String[] items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
		int which=sp.getInt("which",0);
		//设置描述信息
		scv_changebg.setDesc(items[which]);
		scv_changebg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int dd=sp.getInt("which",0);
				//弹出一个对话框
				AlertDialog.Builder builder=new Builder(SettingActivity.this);
				builder.setTitle("归属地提示框风格");
				builder.setSingleChoiceItems(items, dd, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 保存选择参数
						Editor editor=sp.edit();
						editor.putInt("which",which);
						editor.commit();
						scv_changebg.setDesc(items[which]);
						//取消对话框
						dialog.dismiss();
					}
					
				});
				builder.setNegativeButton("取消",null);
				builder.show();
			}
		});
		
		//黑名单拦截服务是否运行
		siv_callsms_safe=(SettingItemView) findViewById(R.id.siv_callsms_safe);
		boolean isCallSmsServiceRunning=ServiceUtils.isServiceRunning(SettingActivity.this,"com.example.mobilesafe.service.CallSSmsSafeService");
		siv_callsms_safe.setChecked(isCallSmsServiceRunning);
		//黑名单拦截设置
		
		callSmsSafeIntent=new Intent(this,CallSSmsSafeService.class);
		siv_callsms_safe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(siv_callsms_safe.isChecked()){
					//变为非选中
					siv_callsms_safe.setChecked(false);
					stopService(callSmsSafeIntent);
				}else{
					//变为选中状态
					siv_callsms_safe.setChecked(true);
					startService(callSmsSafeIntent);
				}
				
			}
		});
		
		
		
	}
	
	
	
	
}
