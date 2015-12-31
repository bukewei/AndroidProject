package com.example.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.utils.StreamTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

public class SplashActivity extends Activity {

	private TextView tv_splash_version;
	private TextView tv_update_info;
	private static final String TAG = "SplashActivity";

	private static final int SHOW_UPDATE_DIALOG = 1;//显示更新对话框
	private static final int ENTER_HOME = 2;// 进入主界面
	private static final int URL_ERROR = 3;// 地址错误
	private static final int NETWORK_ERROR = 4;//网络错误
	private static final int JSON_ERROR = 5;// json错误
	private static final int SOCKET_TIMEOUT = 6;//连接网络超时

	private String description;// 更新描述
	private String version;
	private String apkurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_update_info = (TextView) findViewById(R.id.tv_update_info);
		tv_splash_version.setText("版本号：" + getAppVersion());

		 SharedPreferences sp=getSharedPreferences("config",MODE_PRIVATE);
		//获取升级设置
		boolean update=sp.getBoolean("update",true);
		
		//拷贝数据库
		copyDB();
		
		if(update){
			//检查新版本并升级
			checkUpdate();
		}else{
			//延迟两秒进入主页面
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// 进入主页面
					enterHome();
				}
			}, 2000);
		}
		
		
		
		//动画透明效果   (半透明到不透明)
		AlphaAnimation animation=new AlphaAnimation(0.2f,1f);
		animation.setDuration(500);
		findViewById(R.id.rl_root_splash).startAnimation(animation);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:
				Log.i(TAG, "显示升级的对话框");
	//			Toast.makeText(getApplicationContext(), "    有新版本！\r\n"+description,0).show();
				showUpdateDialong();
				break;
			case ENTER_HOME:
				enterHome();
				break;
			case URL_ERROR:
				enterHome();
				Toast.makeText(SplashActivity.this, "URL错误",0).show();
				break;
			case NETWORK_ERROR:
				enterHome();
				Toast.makeText(SplashActivity.this, "网络异常！",0).show();
				break;
			case JSON_ERROR:
				enterHome();
				Toast.makeText(SplashActivity.this, "json解析错误！",0).show();
				break;
			case SOCKET_TIMEOUT:
				enterHome();
				Toast.makeText(SplashActivity.this, "连接网络超时！",0).show();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 检查升级
	 */
	private void checkUpdate() {
		//访问网络的操作一般在子线程里进行 
		new Thread() {
			public void run() {
				long startTime=System.currentTimeMillis();
				// URL:http://192.168.1.101/updateinfo.json
				Message msg=Message.obtain();
				try {
					URL url = new URL(getString(R.string.serverurl));
					HttpURLConnection conn=(HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					//setConnectTimeout和setReadTimeout都要设置,否则会堵塞线程
					conn.setConnectTimeout(4000);
					conn.setReadTimeout(4000);
					int code=conn.getResponseCode();
					if(code == 200){
						InputStream iStream=conn.getInputStream();
						String result=StreamTools.readFromStream(iStream);
						Log.i(TAG,"JSON:"+result);
						//开始解析json
						JSONObject obj=new JSONObject(result);
						//得到更新信息
						version=(String) obj.get("version");
						description=obj.getString("description");
						apkurl=obj.getString("apkurl");
						//校验是否有新版本
						if(getAppVersion().equals(version)){
							//没有新版本
							msg.what=ENTER_HOME;
						}else{
							//有新版本，弹出升级对话框
							msg.what=SHOW_UPDATE_DIALOG;
						}
						
						
					}
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what=URL_ERROR;
				}catch (SocketTimeoutException  e) {
					msg.what=SOCKET_TIMEOUT;
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
					msg.what=NETWORK_ERROR;
				} catch (JSONException e) {
					msg.what=JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime=System.currentTimeMillis();
					//运行了多少时间
					long dTime=endTime-startTime;
					if(dTime < 2000){
						try {
							//睡眠  2秒减去运行的时间
							//两秒后进入主界面
							Thread.sleep(2000-dTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					handler.sendMessage(msg);
				}
			};
		}.start();
			

	}
	/**
	 * 弹出升级对话框
	 */
	private void showUpdateDialong(){
		//这里不能用getApplicationContext()
		//getApplicationContext();生命周期长，只要应用还存活它就存在；
		//  this 生命周期短，只要Activity不存在了，系统就会回收；  推荐用法:Activity.this
		AlertDialog.Builder builder=new Builder(SplashActivity.this);
		builder.setTitle("升级应用");
		builder.setMessage(description);
//		builder.setCancelable(false);//强制升级
		//点击返回和空白处时
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				//进入主界面
				enterHome();
			}
		});
		
		//点击确定
		builder.setPositiveButton("立即升级",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载apk，并替换安装
				//判断SD卡是否存在
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//使用afnal-0.5.1
					FinalHttp finalHttp=new FinalHttp();
					String downFileName= Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe"+version+".apk";
					Log.i(TAG, "保存路径:"+downFileName);
					finalHttp.download(apkurl,downFileName,new AjaxCallBack<File>() {
						/**
						 * 下载失败
						 */
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							//输出异常信息
							t.printStackTrace();
							Log.i(TAG, "下载失败:"+strMsg);
							Toast.makeText(SplashActivity.this, "下载失败",0).show();
						};
						/**
						 * 下载中
						 */
						public void onLoading(long count, long current) {
							//设置为可见
							tv_update_info.setVisibility(View.VISIBLE);
							//下载的百分比
							int progress=(int) ((current*100)/count);
							tv_update_info.setText("下载进度："+progress+"%");
						};
						/**
						 * 下载成功
						 */
						public void onSuccess(File t) {
							installAPK(t);
						}
						//安装APK
						private void installAPK(File t) {
							Intent intent=new Intent();
							intent.setAction("android.intent.action.VIEW");
							intent.addCategory("android.intent.category.DEFAULT");
							intent.setDataAndType(Uri.fromFile(t),"application/vnd.android.package-archive");
							startActivity(intent);
						};
					});
				}else{
					Toast.makeText(SplashActivity.this, "没有找到SD卡，无法安装应用",0).show();
					return;
				}
				
			}
		});
		//点击取消
		builder.setNegativeButton("下次再说",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 销毁对话框
				dialog.dismiss();
				enterHome();
			}
		});
		//显示
		builder.show();	
	}
	
	/**
	 * 进入主界面
	 */
	private void enterHome() {
		Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
	}
	
	/**
	 * 获取应用版本号
	 * @return
	 */
	public String getAppVersion() {
		PackageManager pManager = getPackageManager();
		try {
			//获取apk的功能清单文件
			PackageInfo info = pManager.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 *  path 把address.db这个数据库拷贝到data/data/《包名》/files/address.db
	 */
	private void copyDB(){
		
		try {
			File file=new File(getFilesDir(),"address.db");
			//如果文件存在，并且长度大于0
			if(file.exists() && file.length() > 0){
				Log.i(TAG, "文件已存在，不需要拷贝了");
			}else{
			
				InputStream is=getAssets().open("address.db");
				FileOutputStream fos=new FileOutputStream(file);
				byte[] buffer=new byte[1024];
				int len=0;
				while((len=is.read(buffer)) != -1){
					fos.write(buffer,0,len);
				}
				is.close();
				fos.close();
			}
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
			
	}
	


}
