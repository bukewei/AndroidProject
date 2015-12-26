package com.example.mobilesafe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.utils.StreamTools;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private TextView tv_splash_version;
	private static final String TAG = "SplashActivity";

	private static final int SHOW_UPDATE_DIALOG = 1;// 显示更新对话框
	private static final int ENTER_HOME = 2;// 进入主界面
	private static final int URL_ERROR = 3;// 地址错误
	private static final int NETWORK_ERROR = 4;// 网络错误
	private static final int JSON_ERROR = 5;// json错误

	private String description;// 更新描述
	private String apkurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本号：" + getAppVersion());

		checkUpdate();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:
				Log.i(TAG, "显示升级的对话框");
				Toast.makeText(getApplicationContext(), "    有新版本！\r\n"+description,0).show();
				break;
			case ENTER_HOME:

				break;
			case URL_ERROR:
				Toast.makeText(getApplicationContext(), "URL错误！",0).show();
				break;
			case NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "网络异常！",0).show();
				break;
			case JSON_ERROR:
				Toast.makeText(getApplicationContext(), "json解析错误！",0).show();
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
		// 访问网络的操作一般在子线程里进行
		new Thread() {
			public void run() {
				long startTime=System.currentTimeMillis();
				// URL:http://192.168.1.101/updateinfo.json
				Message msg=Message.obtain();
				try {
					URL url = new URL(getString(R.string.serverurl));
					HttpURLConnection conn=(HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					int code=conn.getResponseCode();
					if(code == 200){
						InputStream iStream=conn.getInputStream();
						String result=StreamTools.readFromStream(iStream);
						Log.i(TAG,"JSON:"+result);
						//开始解析json
						JSONObject obj=new JSONObject(result);
						//得到更新信息
						String version=(String) obj.get("version");
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
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					msg.what=URL_ERROR;
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					msg.what=NETWORK_ERROR;
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					msg.what=JSON_ERROR;
					e.printStackTrace();
				}finally {
					long endTime=System.currentTimeMillis();
					//花了多少时间
					long dTime=endTime-startTime;
					if(dTime < 2000){
						try {
							Thread.sleep(2000-dTime);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
					
					handler.sendMessage(msg);
				}
			};
		}.start();

	}

	public String getAppVersion() {
		PackageManager pManager = getPackageManager();
		try {
			PackageInfo info = pManager.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
