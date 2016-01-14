package com.example.mobilesafe;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.List;

import com.example.mobilesafe.db.dao.AntivirsuDao;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 杀毒
 */
public class AntiVirusActivity extends Activity {
	private static final int SCANING=0;
	private static final int FINISH=2;
	
	
	private ImageView iv_scan;
	private TextView tv_scan_status;
	private ProgressBar progressBar1;
	private LinearLayout ll_scan_list;
	private RotateAnimation ra;
	private PackageManager pm;
	
	private AntivirsuDao aDao;
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg){
			switch (msg.what) {
			case SCANING:
				ScanInfo scanInfo=(ScanInfo) msg.obj;
				tv_scan_status.setText("正在扫描："+scanInfo.name);
				TextView tView=new TextView(AntiVirusActivity.this);
				if(scanInfo.isvirus){
					tView.setTextColor(Color.RED);
					tView.setText("发现病毒："+scanInfo.name);
				}else {
					tView.setTextColor(Color.BLACK);
					tView.setText("扫描安全："+scanInfo.name);
				}
				ll_scan_list.addView(tView,0);
				break;
			case FINISH:
				tv_scan_status.setText("扫描完成  安全");
				iv_scan.clearAnimation();//停止播放动画
				break;
			default:
				break;
			}
		};
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		
		iv_scan=(ImageView) findViewById(R.id.iv_scan);
		tv_scan_status=(TextView) findViewById(R.id.tv_scan_status);
		progressBar1=(ProgressBar) findViewById(R.id.progressBarl);
		ll_scan_list=(LinearLayout) findViewById(R.id.ll_scan_list);
		
		ra=new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);//无限循环
		iv_scan.startAnimation(ra);
		
		aDao=new AntivirsuDao(getApplicationContext());
		
		scanVirus();
	}

	/**
	 * 扫描病毒
	 */
	private void scanVirus() {
		pm=getPackageManager();
		tv_scan_status.setText("999999999核杀毒引擎正在初始化...");
		new Thread(){
			public void run(){
				List<PackageInfo> infos=pm.getInstalledPackages(0);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				progressBar1.setMax(infos.size());
				int progress=0;
				for(PackageInfo info:infos){
					//apk的完整路径
					String sourcedir=info.applicationInfo.sourceDir;
					String md5=getFileMd5(sourcedir);
					ScanInfo scanInfo=new ScanInfo();
					scanInfo.name=info.applicationInfo.loadLabel(pm).toString();
					System.out.println(scanInfo.packname+":"+md5);
					//查询md5信息，是否在病毒数据库里面存在
					if(aDao.isVirus(md5)){
						//发现病毒
						scanInfo.isvirus=true;
					}else{
						//安全
						scanInfo.isvirus=false;
					}
					Message msg=Message.obtain();
					msg.obj=scanInfo;
					msg.what=SCANING;
					handler.sendMessage(msg);
					
					/*try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}*/
					progress++;
					progressBar1.setProgress(progress);
				}
				Message msg=Message.obtain();
				msg.what=FINISH;
				handler.sendMessage(msg);
				
			/*	runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(AntiVirusActivity.this,"杀毒完成，没有发现病毒",Toast.LENGTH_SHORT).show();
						iv_scan.clearAnimation();//停止动画
						tv_scan_status.setText("扫描完成");
					}
				});*/
			};
		}.start();
	}
	
	class ScanInfo{
		public String packname;
		public String name;
		public boolean isvirus;
	}
	
	
	
	/**
	 * 获取文件的md5值
	 * @param path  文件的全路径
	 * @return
	 */
	private String getFileMd5(String path){
		
		try {
			//查杀病毒可能要 获取一个文件的特征信息，签名信息等
			File file=new File(path);
			//md5
			MessageDigest digest=MessageDigest.getInstance("md5");
			FileInputStream fis=new FileInputStream(file);
			byte[] buffer=new byte[1024];//1kb
			int len=-1;
			while((len=fis.read(buffer)) != -1){
				digest.update(buffer, 0, len);
			}
			byte[] result=digest.digest();
			StringBuffer sbBuffer=new StringBuffer();
			for(byte b:result){
				//与运算
				int number=b & 0xff;//在这里可以进行加盐操作
				String str=Integer.toHexString(number);
				if(str.length() == 1){
					sbBuffer.append("0");
				}
				sbBuffer.append(str);
			}
			return sbBuffer.toString();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return "";
		}
	}
	
	
	
	
	
	
	
}
