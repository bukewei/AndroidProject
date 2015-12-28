package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class LostFindActivity extends Activity {
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp=getSharedPreferences("config",MODE_PRIVATE);
		//判断是否设置过手机向导
		boolean configed=sp.getBoolean("configed",false);
		if(configed){
			//设置过的就进入手机防盗页面
			setContentView(R.layout.activity_lost_find);
		}else {
			//进入手机设置向导
			enterSetup();
		}
		
		
	}
	
	
	private void reEnterSetup(View view){
		
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
	
	
	
}
