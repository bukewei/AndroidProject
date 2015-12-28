package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup2Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setup2);
	}

	public void pre(View view) {
		Intent intent=new Intent(this,Setup1Activity.class);
		startActivity(intent);
		finish();
	}

	public void next(View view) {
		Intent intent=new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
	}
}
