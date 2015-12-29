package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

public class Setup4Activity extends Activity {
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setup4);
		
		sp=getSharedPreferences("config",MODE_PRIVATE);
	}

	public void pre(View view) {
		
		Intent intent=new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in,R.anim.tran_pre_out);
	}

	public void next(View view){
		Editor editor=sp.edit();
		editor.putBoolean("configed",true);
		editor.commit();
		
		Intent intent=new Intent(this,LostFindActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
	}
	
}
