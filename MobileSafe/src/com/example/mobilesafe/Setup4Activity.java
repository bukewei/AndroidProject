package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {
	
	private static final String STATE_OFF = "手机防盗还没有开启";
	private static final String STATE_ON = "手机防盗已经开启";
	private SharedPreferences sp;
	private CheckBox cb_proteting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setup4);
		cb_proteting=(CheckBox) findViewById(R.id.cb_proteting);
		sp=getSharedPreferences("config",MODE_PRIVATE);
		
		boolean protecting=sp.getBoolean("protecting",false);
		if(protecting){
			//手机防盗已经开启
			cb_proteting.setText(STATE_ON);
			cb_proteting.setChecked(true);
		}else{
			//还没有开启
			cb_proteting.setText(STATE_OFF);
			cb_proteting.setChecked(false);
		}
		//CheckBox的点击事件
		cb_proteting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO 自动生成的方法存根
				if(isChecked){
					cb_proteting.setText(STATE_ON);
				}else{
					cb_proteting.setText(STATE_OFF);
				}
				//保存选择的状态
				Editor editor=sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
			}
		});
		
		
	}

	@Override
	public void showNext() {
		//保存有没有设置的状态
		Editor editor=sp.edit();
		editor.putBoolean("configed",true);
		editor.commit();
		
		Intent intent=new Intent(this,LostFindActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
		
	}

	@Override
	public void showPre() {
		Intent intent=new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in,R.anim.tran_pre_out);
	}
	
}
