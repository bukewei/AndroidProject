package com.example.mobilesafe;

import com.example.mobilesafe.utils.SmsUtils;
import com.example.mobilesafe.utils.SmsUtils.BackUpCallBack;
import com.example.mobilesafe.utils.SmsUtils.RestoreCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AtoolsActivity extends Activity {
	//进度条对话框
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
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平方向进度条
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
	 * 恢复短信
	 * @param flag 是否清除原有短信        true：清除    false：保留
	 */
	public void Restore(final boolean flag){
		pd=new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平方向进度条
		pd.setMessage("正在恢复短信：");
		pd.show();
		
		new Thread(){
			public void run(){
				try {
					SmsUtils.restoreSms(AtoolsActivity.this,flag,new RestoreCallBack() {
						@Override
						public void onSmsRestore(int progress) {
							// 设置进度
							pd.setProgress(progress);
						}
						@Override
						public void beforeRestore(int max) {
							// 设置最大值
							pd.setMax(max);
						}
					});
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this,"短信还原成功",Toast.LENGTH_SHORT).show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this,"短信还原失败",Toast.LENGTH_SHORT).show();
						}
					});
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
		//AlertDialog的内部类Builder
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("短信恢复提示：");
		builder.setMessage("在恢复备份短信之前是否清除收件箱里的原有短信？");
		//设置确定按钮
		builder.setPositiveButton("清除",new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Restore(true);
			}
		});
		//设置否定按钮
		builder.setNegativeButton("保留",new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Restore(false);
			}
		});
		builder.show();
		
	}

}
