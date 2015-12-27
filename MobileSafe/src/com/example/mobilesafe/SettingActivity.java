package com.example.mobilesafe;

import com.example.mobilesafe.ui.SettingItemView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	private SettingItemView siv_update;
	private SharedPreferences sp;
	
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
		
	}
	
}
