package com.example.mobilesafe;

import com.example.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
				Intent intent;
				switch (position) {
				case 0://进入手机防盗页面
					showLostFindDialog();
					break;
				case 1://进入黑名单拦截页面
					intent=new Intent(HomeActivity.this,CallSmsSafeActivity.class);
					startActivity(intent);
					break;
				case 7://进入高级工具
					intent=new Intent(HomeActivity.this,AtoolsActivity.class);
					startActivity(intent);
					break;
				case 8://进入设置中心
					intent=new Intent(HomeActivity.this,SettingActivity.class);
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
			showEnterDialog();
		}else{
			//没有设置过密码
			showSetupPwdDialong();
		}
	}
	/**
	 * 打开手机防盗页面
	 */
	public void enterLostFind(){
		Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
		startActivity(intent);
		//HomeActivity暂时不能销毁，否则返回时会直接进入桌面
//		finish();
	}
	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	private Button ok;
	private Button cancel;
	private AlertDialog dialog;//对话框
	/**
	 * 输入密码对话框
	 */
	private void showEnterDialog() {
		AlertDialog.Builder builder=new Builder(HomeActivity.this);
		//自定义一个布局文件
		View view=View.inflate(HomeActivity.this,R.layout.dialog_enter_pwd,null);
		et_setup_pwd=(EditText) view.findViewById(R.id.et_setup_pwd);
		ok=(Button) view.findViewById(R.id.ok);
		cancel=(Button) view.findViewById(R.id.cancel);
		//点击取消按钮
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//把对话框销毁
				dialog.dismiss();
			}
		});
		//点击确定按钮
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 取出输入框密码
				String pwd=et_setup_pwd.getText().toString().trim();
				if(TextUtils.isEmpty(pwd)){
					Toast.makeText(HomeActivity.this,"密码不能为空！",0).show();
					Log.i(TAG,"密码不能为空！");
					return;
				}
				//进行加密，然后比较保存的密码
				pwd=MD5Utils.Md5Pwd(pwd);
				String savePwd=sp.getString("password","");
				if(TextUtils.isEmpty(pwd)){
					Toast.makeText(HomeActivity.this,"密码不能为空！",0).show();
					return;
				}
				if(pwd.equals(savePwd)){
					//把对话框销毁
					dialog.dismiss();
					//密码一致进入手机防盗页面
					Log.i(TAG,"密码一致进入手机防盗页面");
//					Toast.makeText(HomeActivity.this,"密码正确！",0).show();
					//进入手机防盗页面
					enterLostFind();
				}else {
					Toast.makeText(HomeActivity.this,"密码错误！",0).show();
					Log.i(TAG,"密码错误！");
					return;
				}
				
			}
		});
		//为了适配低版本
		dialog=builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}
	
	
	/**
	 * 设置密码对话框
	 */
	private void showSetupPwdDialong() {
		AlertDialog.Builder builder=new Builder(HomeActivity.this);
		//自定义一个布局文件
		View view=View.inflate(HomeActivity.this,R.layout.dialog_setup_pwd,null);
		et_setup_pwd=(EditText) view.findViewById(R.id.et_setup_pwd);
		et_setup_confirm=(EditText) view.findViewById(R.id.et_setup_confirm);
		ok=(Button) view.findViewById(R.id.ok);
		cancel=(Button) view.findViewById(R.id.cancel);
		//点击取消按钮
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//把对话框销毁
				dialog.dismiss();
			}
		});
		//点击确定按钮
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 取出密码
				String password=et_setup_pwd.getText().toString().trim();
				String pwd_confirm=et_setup_confirm.getText().toString().trim();
				if(TextUtils.isEmpty(password) || TextUtils.isEmpty(pwd_confirm)){
					Toast.makeText(HomeActivity.this,"密码不能为空！",0).show();
					return;
				}
				//判断密码是否一致
				if (password.equals(pwd_confirm)) {
					//一致
					Editor editor=sp.edit();
					//加密密码然后保存
					password=MD5Utils.Md5Pwd(password);
					editor.putString("password",password);
					editor.commit();
					Log.i(TAG,"密码一致，保存密码进入手机防盗页面");
					//把对话框销毁
					dialog.dismiss();
					//进入手机防盗页面
					enterLostFind();
					
				}else{
					//不一致
					Toast.makeText(HomeActivity.this,"两次输入的密码不一致，请重新输入！",0).show();
					return;
				}
				
			}
		});
		//为了适配低版本
		dialog=builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
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
			
//			System.out.println("------------- "+titles[position]+" ----------------");
			
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
