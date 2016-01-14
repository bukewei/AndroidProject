package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 程序锁   输入密码界面
 */
public class EnterPwdActivity extends Activity {
	
	private EditText et_pwd;
	private TextView tv_name;
	private ImageView iv_icon;
	private String packname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pwd);
		
		et_pwd=(EditText) findViewById(R.id.et_password);
		tv_name=(TextView) findViewById(R.id.tv_name);
		iv_icon=(ImageView) findViewById(R.id.iv_icon);
		Intent intent=getIntent();
		//当前要保护的应用程序的包名
		packname=intent.getStringExtra("packname");
		PackageManager pm=getPackageManager();
		try {
			//获取应用程序信息
			ApplicationInfo info=pm.getApplicationInfo(packname, 0);
			tv_name.setText(info.loadLabel(pm));
			iv_icon.setImageDrawable(info.loadIcon(pm));
		} catch (NameNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	/**
	 * 按返回键     返回桌面
	 */
	@Override
	public void onBackPressed() {
		//回桌面。
//      <action android:name="android.intent.action.MAIN" />
//      <category android:name="android.intent.category.HOME" />
//      <category android:name="android.intent.category.DEFAULT" />
//      <category android:name="android.intent.category.MONKEY"/>
		Intent intent=new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
		//所有的activity最小化 不会执行ondestory 只执行 onstop方法。
	}
	
	@Override
	protected void onStop() {
		// TODO 自动生成的方法存根
		super.onStop();
		finish();
	}
	
	
	public void enterApp(View view){
		String pwd=et_pwd.getText().toString().trim();
		if(TextUtils.isEmpty(pwd)){
			Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
			return;
		}
		//如果正确密码是123
		if("123".equals(pwd)){
			//密码正确可以临时停止保护
			//自定义广播，临时停止保护
			Intent intent=new Intent();
			intent.setAction("com.example.mobilesafe.tempstop");
			intent.putExtra("packname",packname);
			//发送广播
			sendBroadcast(intent);
			finish();
		}else{
			Toast.makeText(this,"密码不正确",Toast.LENGTH_SHORT).show();
			return;
		}
		
	}
	
	
}
