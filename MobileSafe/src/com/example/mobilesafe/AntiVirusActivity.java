package com.example.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 杀毒
 */
public class AntiVirusActivity extends Activity {
	
	private ImageView iv_scan;
	private TextView tv_scan_state;
	private ProgressBar progressBar1;
	private RotateAnimation ra;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		
		iv_scan=(ImageView) findViewById(R.id.iv_scan);
		tv_scan_state=(TextView) findViewById(R.id.tv_scan_state);
		progressBar1=(ProgressBar) findViewById(R.id.progressBarl);
		
		ra=new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);//无限循环
		iv_scan.startAnimation(ra);
		progressBar1.setMax(100);
		new Thread(){
			public void run(){
				for(int i=0;i<100;i++){
					try {
						Thread.sleep(100);
						progressBar1.setProgress(i+1);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(AntiVirusActivity.this,"杀毒完成，没有发现病毒",Toast.LENGTH_SHORT).show();
					//	iv_scan.setVisibility(View.INVISIBLE);
						tv_scan_state.setText("扫描完成");
						ra.cancel();
					}
				});
			};
		}.start();
	}
	
}
