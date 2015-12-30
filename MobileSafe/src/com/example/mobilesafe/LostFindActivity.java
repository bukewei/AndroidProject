package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	
	private SharedPreferences sp;
	private TextView tv_safenumber;
	private TextView tv_protecting;
	private ImageView iv_protecting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp=getSharedPreferences("config",MODE_PRIVATE);
		//判断是否设置过手机向导
		boolean configed=sp.getBoolean("configed",false);
		if(configed){
			//设置过的就进入手机防盗页面
			setContentView(R.layout.activity_lost_find);
			tv_safenumber=(TextView) findViewById(R.id.tv_safenumber);
			tv_protecting=(TextView) findViewById(R.id.tv_protecting);
			iv_protecting=(ImageView) findViewById(R.id.iv_protecting);
			//得到设置过的安全号码
			String safeNumber=sp.getString("safenumber", "");
			tv_safenumber.setText(safeNumber);
			//根据防盗保护状态设置图片
			boolean protecting=sp.getBoolean("protecting", false);
			if(protecting){
				//已开启
				tv_protecting.setText("防盗保护已开启");
				iv_protecting.setImageResource(R.drawable.lock);
			}else{
				//没有开启
				tv_protecting.setText("防盗保护未开启");
				iv_protecting.setImageResource(R.drawable.unlock);
			}
		}else {
			//进入手机设置向导
			enterSetup();
		}
		
		
	}
	
	/**
	 * TextView点击事件    重新进入设置向导
	 * @param view
	 */
	public void reEnterSetup(View view){
		enterSetup();
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
