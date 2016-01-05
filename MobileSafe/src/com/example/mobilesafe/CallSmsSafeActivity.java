package com.example.mobilesafe;

import java.util.List;

import com.example.mobilesafe.db.dao.BlackNumberDao;
import com.example.mobilesafe.domain.BlackNumberInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CallSmsSafeActivity extends Activity {
	
	public static final String TAG="CallSmsSafeActivity";
	private ListView lv_callsms_safe;
	
	private List<BlackNumberInfo> infos;
	private CallSmsSafeAdapter adapter;
	private BlackNumberDao dao;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		
		lv_callsms_safe=(ListView) findViewById(R.id.lv_callsms_safe);
		dao=new BlackNumberDao(this);
		add();
		
		infos=dao.findAll();
		adapter=new CallSmsSafeAdapter();
		lv_callsms_safe.setAdapter(adapter);
		
		
	}
	
	public void add(){
		dao.add("5557","0");
		dao.add("5556","1");
		dao.add("5554","2");
	}
	
	
	private class CallSmsSafeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return infos.size();
		}
		/**
		 * 有多少条目被显示，这个方法就或被调用多少次
		 */
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			//1.减少内存中view对象创建的个数
			if(convertView==null){
				Log.i(TAG, "创建新的view对象"+position);
				//把一个布局文件转化成view对象
				view = View.inflate(CallSmsSafeActivity.this, R.layout.list_item_callsms, null);
				//2.减少 查询次数   保存内存中对象的地址
				holder=new ViewHolder();
				holder.tv_number=(TextView) view.findViewById(R.id.tv_black_number);
				holder.tv_mode=(TextView) view.findViewById(R.id.tv_black_mode);
				holder.iv_delete=(ImageView) view.findViewById(R.id.iv_delete);
				//创建时找到他们的引用，存放在
				view.setTag(holder);
				
			}else{
				Log.i(TAG, "旧的view对象，复用旧的缓存的view对象："+position);
				view =convertView;
				holder=(ViewHolder) view.getTag();//5%
			}
			
			holder.tv_number.setText(infos.get(position).getNumber());
			String mode=infos.get(position).getMode();
			if("1".equals(mode)){
				holder.tv_mode.setText("电话拦截");
			}else if("2".equals(mode)){
				holder.tv_mode.setText("短信拦截");
			}else{
				holder.tv_mode.setText("全部拦截");
			}
			//点击事件
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//弹出对话框
					AlertDialog.Builder builder=new Builder(CallSmsSafeActivity.this);
					builder.setTitle("警告");
					builder.setMessage("确定要删除这条记录吗？");
					builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//删除数据库的内容
							dao.delete(infos.get(position).getNumber());
							//更新界面
							infos.remove(position);
							//通知listview数据适配器更新
							adapter.notifyDataSetChanged();
						}
					});
					builder.setNegativeButton("取消",null);
					builder.show();
				}
			});
			
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
	/**
	 * view对象的容器
	 *记录孩子的内存地址。
	 *相当于一个记事本
	 */
	static class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
	
	/**
	 * 添加黑名单号码
	 * @param view
	 */
	public void addBlackNumber(View view){
		
	}
	
	
	
}
package com.example.mobilesafe;

import java.util.List;

import com.example.mobilesafe.db.dao.BlackNumberDao;
import com.example.mobilesafe.domain.BlackNumberInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import net.tsz.afinal.FinalDb.DaoConfig;

public class CallSmsSafeActivity extends Activity {
	
	public static final String TAG="CallSmsSafeActivity";
	private ListView lv_callsms_safe;
	
	private List<BlackNumberInfo> infos;
	private CallSmsSafeAdapter adapter;
	private BlackNumberDao dao;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		
		lv_callsms_safe=(ListView) findViewById(R.id.lv_callsms_safe);
		dao=new BlackNumberDao(this);
		infos=dao.findAll();
		adapter=new CallSmsSafeAdapter();
		lv_callsms_safe.setAdapter(adapter);
		
		add();
	}
	
	public void add(){
		dao.add("5557","0");
		dao.add("5556","1");
		dao.add("5554","2");
	}
	
	
	private class CallSmsSafeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return infos.size();
		}
		/**
		 * 有多少条目被显示，这个方法就或被调用多少次
		 */
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			//1.减少内存中view对象创建的个数
			if(convertView==null){
				Log.i(TAG, "创建新的view对象"+position);
				//把一个布局文件转化成view对象
				view = View.inflate(getApplicationContext(), R.layout.list_item_callsms, null);
				//2.减少 查询次数   保存内存中对象的地址
				holder=new  ViewHolder();
				holder.tv_number=(TextView) view.findViewById(R.id.tv_black_number);
				holder.tv_mode=(TextView) view.findViewById(R.id.tv_black_mode);
				holder.iv_delete=(ImageView) view.findViewById(R.id.iv_delete);
				//创建时找到他们的引用，存放在
				view.setTag(holder);
				
			}else{
				Log.i(TAG, "旧的view对象，复用旧的缓存的view对象："+position);
				view =convertView;
				holder=(ViewHolder) view.getTag();//5%
			}
			
			holder.tv_number.setText(infos.get(position).getNumber());
			String mode=infos.get(position).getMode();
			if("1".equals(mode)){
				holder.tv_mode.setText("电话拦截");
			}else if("2".equals(mode)){
				holder.tv_mode.setText("短信拦截");
			}else{
				holder.tv_mode.setText("全部拦截");
			}
			//点击事件
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//弹出对话框
					AlertDialog.Builder builder=new Builder(CallSmsSafeActivity.this);
					builder.setTitle("警告");
					builder.setMessage("确定要删除这条记录吗？");
					builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//删除数据库的内容
							dao.delete(infos.get(position).getNumber());
							//更新界面
							infos.remove(position);
							//通知listview数据适配器更新
							adapter.notifyDataSetChanged();
						}
					});
					builder.setNegativeButton("取消",null);
					builder.show();
				}
			});
			
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
	/**
	 * view对象的容器
	 *记录孩子的内存地址。
	 *相当于一个记事本
	 */
	static class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
	
	/**
	 * 添加黑名单号码
	 * @param view
	 */
	public void addBlackNumber(View view){
		
	}
	
	
	
}
