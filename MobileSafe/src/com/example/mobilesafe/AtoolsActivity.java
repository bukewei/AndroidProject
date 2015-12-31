package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AtoolsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	/**
	 * 点击事件  进入号码归属地查询页面
	 * @param view
	 */
	public void numberQuery(View view){
		Intent numberQueryIntent=new Intent(this,NumberAddressQueryActivity.class);
		startActivity(numberQueryIntent);
	}
	

}
