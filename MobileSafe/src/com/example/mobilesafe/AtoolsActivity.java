package com.example.mobilesafe;

import com.example.mobilesafe.utils.SmsUtils;
import com.example.mobilesafe.utils.SmsUtils.BackUpCallBack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AtoolsActivity extends Activity {
	
	private ProgressDialog pd;
	
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
	/**
	 * 短信备份
	 * @param view
	 */
	public void smsBackup(View view){
		pd=new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//横向
		pd.setMessage("正在备份短信");
		pd.show();
		
		new Thread(){
			public void run(){
				try {
					SmsUtils.backupSms(AtoolsActivity.this,new BackUpCallBack() {
						
						@Override
						public void onSmsBackup(int progress) {
							// 设置进度
							pd.setProgress(progress);
						}
						
						@Override
						public void beforeBackup(int max) {
							// 设置最大值
							pd.setMax(max);
						}
					});
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this,"短信备份成功",Toast.LENGTH_SHORT).show();
						}
					});
					
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					Toast.makeText(AtoolsActivity.this,"短信备份失败",Toast.LENGTH_SHORT).show();
				}finally {
					//最后销毁对话框
					pd.dismiss();
				}
			};
		}.start();
		
	}
	/**
	 * 短信还原
	 * @param view
	 */
	public void smsRestore(View view){
		SmsUtils.restoreSms(this,true);
		Toast.makeText(this,"短信还原成功",Toast.LENGTH_SHORT).show();
	}

}
