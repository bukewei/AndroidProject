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

	private static final int SHOW_UPDATE_DIALOG = 1;// ��ʾ���¶Ի���
	private static final int ENTER_HOME = 2;// ����������
	private static final int URL_ERROR = 3;// ��ַ����
	private static final int NETWORK_ERROR = 4;// �������
	private static final int JSON_ERROR = 5;// json����

	private String description;// ��������
	private String apkurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("�汾�ţ�" + getAppVersion());

		checkUpdate();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:
				Log.i(TAG, "��ʾ�����ĶԻ���");
				Toast.makeText(getApplicationContext(), "    ���°汾��\r\n"+description,0).show();
				break;
			case ENTER_HOME:

				break;
			case URL_ERROR:
				Toast.makeText(getApplicationContext(), "URL����",0).show();
				break;
			case NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "�����쳣��",0).show();
				break;
			case JSON_ERROR:
				Toast.makeText(getApplicationContext(), "json��������",0).show();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * �������
	 */
	private void checkUpdate() {
		// ��������Ĳ���һ�������߳������
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
						//��ʼ����json
						JSONObject obj=new JSONObject(result);
						//�õ�������Ϣ
						String version=(String) obj.get("version");
						description=obj.getString("description");
						apkurl=obj.getString("apkurl");
						//У���Ƿ����°汾
						if(getAppVersion().equals(version)){
							//û���°汾
							msg.what=ENTER_HOME;
						}else{
							//���°汾�����������Ի���
							msg.what=SHOW_UPDATE_DIALOG;
						}
						
						
					}
					
				} catch (MalformedURLException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
					msg.what=URL_ERROR;
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
					msg.what=NETWORK_ERROR;
				} catch (JSONException e) {
					// TODO �Զ����ɵ� catch ��
					msg.what=JSON_ERROR;
					e.printStackTrace();
				}finally {
					long endTime=System.currentTimeMillis();
					//���˶���ʱ��
					long dTime=endTime-startTime;
					if(dTime < 2000){
						try {
							Thread.sleep(2000-dTime);
						} catch (InterruptedException e) {
							// TODO �Զ����ɵ� catch ��
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
			// TODO �Զ����ɵ� catch ��
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
