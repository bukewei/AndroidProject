package com.example.mobilesafe;

import com.example.mobilesafe.ui.SettingItemView;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Setup2Activity extends BaseSetupActivity {
	
	private SettingItemView siv_setup2_sim;
	//电话管理者
	private TelephonyManager tm;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);执行父类的onCreate方法
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		
		siv_setup2_sim=(SettingItemView) findViewById(R.id.siv_setup2_sim);
		tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String sim=sp.getString("sim",null);
		if(TextUtils.isEmpty(sim)){
			//没有绑定
			siv_setup2_sim.setChecked(false);
		}else{
			//已经绑定
			siv_setup2_sim.setChecked(true);
		}
		//点击事件
		siv_setup2_sim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor=sp.edit();
				if(siv_setup2_sim.isChecked()){
					//取消绑定
					siv_setup2_sim.setChecked(false);
					editor.putString("sim",null);
				}else{
					//开始绑定SIM卡
					
					siv_setup2_sim.setChecked(true);
					//保存SIM卡的序列号
					String sim=tm.getSimSerialNumber();
					editor.putString("sim",sim);
				}
				//提交
				editor.commit();
			}
		});
		
	}

	@Override
	public void showNext() {
		//没有绑定不能进行下一步
		String sim=sp.getString("sim",null);
		if(TextUtils.isEmpty(sim)){
			//没有绑定
			Toast.makeText(Setup2Activity.this,"还没有绑定SIM卡",0).show();
			return;
		}
		
		
		Intent intent=new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
		
	}

	@Override
	public void showPre() {
		Intent intent=new Intent(this,Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in,R.anim.tran_pre_out);
		
	}
}
