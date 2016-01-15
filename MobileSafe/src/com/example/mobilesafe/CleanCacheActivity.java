package com.example.mobilesafe;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CleanCacheActivity extends Activity {
	
	private ProgressBar pb;
	private TextView tv_scan_status;
	private PackageManager pm;
	private LinearLayout ll_container;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);
		
		tv_scan_status=(TextView) findViewById(R.id.tv_scan_status);
		pb=(ProgressBar) findViewById(R.id.pb);
		ll_container=(LinearLayout) findViewById(R.id.ll_container);
		scanCache();
	}
	/**
	 * 扫描手机里面所有应用程序的缓存信息
	 */
	private void scanCache(){
		pm=getPackageManager();
		
		new Thread(){
			public void run() {
				//Method:这个类代表一个方法。可以访问方法信息,可以动态调用方法。
				Method getPackageSizeInfoMethod=null;
			//	返回一个数组,其中包含方法对象所代表的类C的所有公共方法这门课。方法可能会宣布在C语言中,它实现的接口或C的超类返回数组中的元素没有特定的顺序。
			//	如果没有公共方法或这类代表一个原始类型或无效则返回一个空数组。
				Method[] methods=PackageManager.class.getMethods();//获取PackageManager里所有的方法(包括隐藏的方法)
				for(Method method : methods){
					System.out.println(method.getName());
					if("getPackageSizeInfo".equals(method.getName())){
						getPackageSizeInfoMethod=method;
					//	System.out.println("找到了："+method.getName());
					}
				}
				List<PackageInfo> packageInfos=pm.getInstalledPackages(0);
				pb.setMax(packageInfos.size());
				int progress=1;
				System.out.println("安装应用个数："+packageInfos.size()+"个");
				for(PackageInfo packinfo:packageInfos){
					try {
						//谁去执行，参数，包名、和回调对象
						getPackageSizeInfoMethod.invoke(pm,packinfo.packageName,new MyDataObserver());
						Thread.sleep(10);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					progress++;
					pb.setProgress(progress);
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv_scan_status.setText("扫描完成...");
						
					}
				});
			};
		}.start();
		
	}
	
	private class MyDataObserver extends IPackageStatsObserver.Stub{

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
			final long cache=pStats.cacheSize;
			long code=pStats.codeSize;
			long data=pStats.dataSize;
			final String packname=pStats.packageName;
			final ApplicationInfo appinfo;
			try {
				appinfo=pm.getApplicationInfo(packname, 0);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv_scan_status.setText("正在扫描："+appinfo.loadLabel(pm));
						if(cache > 0){
							View view=View.inflate(CleanCacheActivity.this,R.layout.list_item_cacheinfo,null);
							
							TextView tv_cache=(TextView) view.findViewById(R.id.tv_cache_size);
							String cacheStr=Formatter.formatFileSize(getApplicationContext(), cache);
							tv_cache.setText("缓存大小:"+cacheStr);
							
							TextView tv_name=(TextView) view.findViewById(R.id.tv_app_name);
							tv_name.setText(appinfo.loadLabel(pm));
							
							ImageView iv_delete=(ImageView) view.findViewById(R.id.iv_delete);
							iv_delete.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									try {
										Method method=PackageManager.class.getMethod("deleteApplicationCacheFiles",String.class,IPackageDataObserver.class);
										method.invoke(pm,packname,new MypackDataObserver());
										Toast.makeText(getApplicationContext(),"已清除",Toast.LENGTH_SHORT).show();
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
									}
								}
							});
							
							ll_container.addView(view,0);
						}
							
					}
				});
			} catch (NameNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			
		}
			
	}
	
	
	private class MypackDataObserver extends IPackageDataObserver.Stub{

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
			System.out.println(packageName+"  "+succeeded);
		}
	}
	/**
	 * 清理全部缓存
	 * @param view
	 */
	public void clearAll(View view){
		Method[] methods=PackageManager.class.getMethods();
		for(Method method:methods){
			if("freeStorageAndNotify".equals(method.getName())){
				try {
					//向系统申请很大的空间，迫使系统清理缓存
					method.invoke(pm,Integer.MAX_VALUE,new MypackDataObserver());
					Toast.makeText(this,"已清除",Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				return;
			}
			
		}
	}
	
}
