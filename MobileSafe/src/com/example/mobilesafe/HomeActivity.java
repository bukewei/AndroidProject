package com.example.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	private static final String TAG="HomeActivity";
	
	private GridView list_home;
	private MyAdapter adapter;
	private SharedPreferences sp;
	
	private static String titles[] = {
			"手机防盗","通讯卫士","软件管理",
			"进程管理","流量统计","手机杀毒",
			"缓存清理","高级工具","设置中心"
		};
	private static int img_ids[] = {
			R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
			R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
			R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp=getSharedPreferences("config",MODE_PRIVATE);
		
		list_home=(GridView) findViewById(R.id.list_home);
		adapter=new MyAdapter();
		list_home.setAdapter(adapter);
		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO 自动生成的方法存根
				switch (position) {
				case 0://进入手机防盗页面
					showLostFindDialog();
					break;
				case 8://进入设置中心
					Intent  intent=new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
			
		});
		
	}
	
	private void showLostFindDialog(){
		//判断是否设置过密码
		if(isSetupPwd()){
			//已经设置过密码
			
		}else{
			//没有设置过密码
			showSetupPwdDialong();
		}
	}
	
	private void showSetupPwdDialong() {
		AlertDialog.Builder builder=new Builder(HomeActivity.this);
		//自定义一个布局文件
		View view=View.inflate(HomeActivity.this,R.layout.dialog_setup_pwd,null);
		builder.setView(view);
		builder.show();
	}

	private boolean isSetupPwd(){
		String password=sp.getString("password",null);
		//取反并返回
		return !TextUtils.isEmpty(password);
	}
	
	private class MyAdapter extends BaseAdapter{
		
		
		

		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return titles.length;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,R.layout.list_item_home,null);
			//必须要用  view.findViewById
			ImageView iv_item=(ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item=(TextView) view.findViewById(R.id.tv_item);
			
			System.out.println("------------- "+titles[position]+" ----------------");
			
			tv_item.setText(titles[position]);
			iv_item.setImageResource(img_ids[position]);
			
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO 自动生成的方法存根
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO 自动生成的方法存根
			return 0;
		}

		
		
	}
	
	
	
	
	
}
