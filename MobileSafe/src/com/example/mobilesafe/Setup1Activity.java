package com.example.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

public class Setup1Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setup1);
	}
	
	

	@Override
	public void showNext() {
		//打开下一个设置向导页面
		Intent intent=new Intent(this,Setup2Activity.class);
		startActivity(intent);
		finish();
		//要求在finish()或者startActivity(intent);后面执行；
		overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
		
	}



	@Override
	public void showPre() {
		// TODO 自动生成的方法存根
		
	}
}
