package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setup1);
	}
	
	
	
	public void next(View  view){
		//打开下一个设置向导页面
		Intent intent=new Intent(this,Setup2Activity.class);
		startActivity(intent);
		finish();
	}
}