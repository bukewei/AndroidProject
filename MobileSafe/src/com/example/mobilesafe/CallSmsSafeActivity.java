package com.example.mobilesafe;

import java.util.List;

import com.example.mobilesafe.db.dao.BlackNumberDao;
import com.example.mobilesafe.domain.BlackNumberInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CallSmsSafeActivity extends Activity {
	
	public static final String TAG="CallSmsSafeActivity";
	private ListView lv_callsms_safe;
	
	private List<BlackNumberInfo> infos;
	private CallSmsSafeAdapter adapter;
	private BlackNumberDao dao;
	
	private LinearLayout ll_loading;
	
	private int offset=0;
	private int maxnumber=20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		
		ll_loading=(LinearLayout) findViewById(R.id.ll_loading);
		
		lv_callsms_safe=(ListView) findViewById(R.id.lv_callsms_safe);
		dao=new BlackNumberDao(this);
		
		fillData();
//		add();
		
//		infos=dao.findAll();
//		adapter=new CallSmsSafeAdapter();
//		lv_callsms_safe.setAdapter(adapter);
		
		//listview注册一个滚动事件的监听器
		lv_callsms_safe.setOnScrollListener(new OnScrollListener() {
			/**
			 * 滚动状态发生改变
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE://空闲状态
					//判断当前listview滚动的位置
					//获取最后一个可见条目在集合里面的位置
					int lastposition=lv_callsms_safe.getLastVisiblePosition();
					//集合里面有20个item 位置从0开始的 最后一个条目的位置 19
					if(lastposition == (infos.size()-1)){
						System.out.println("列表被移动到了最后一个位置，加载更多的数据");
						offset+=maxnumber;
						fillData();
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://手指触摸滚动
					System.out.println("手指触摸滚动");
					break;
				case OnScrollListener.SCROLL_STATE_FLING://惯性滑行状态
					System.out.println("惯性滑行状态");
					break;
				default:
					break;
				}
			}
			/**
			 * 滚动的时候调用的方法
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				System.out.println("第一个可见的item："+firstVisibleItem+"\n共有"+visibleItemCount+"个可见  \nitem总数为"+totalItemCount);
			}
		});
		
	}
	
	private void fillData(){
		//设置为可见
		ll_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run(){
				if(infos == null){
					//还没有加载过数据
					infos=dao.findPart(offset, maxnumber);
				}else{
					//原来已经加载过数据了
					List<BlackNumberInfo> data=dao.findPart(offset, maxnumber);
					if(data.isEmpty()){
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(CallSmsSafeActivity.this,"已经没有更多数据了",Toast.LENGTH_SHORT).show();
								return;
							}
						});
					
					}
					infos.addAll(dao.findPart(offset, maxnumber));
				}
				runOnUiThread(new Runnable() {
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						if(adapter == null){
							//数据适配器为空
							adapter=new CallSmsSafeAdapter();
							lv_callsms_safe.setAdapter(adapter);
						}else{
							//已经有数据适配器了，直接更新即可
							adapter.notifyDataSetChanged();
						}
					}
				});
			};
		}.start();
		
	}
	
//	public void add(){
//		dao.add("5557","0");
//		dao.add("5556","1");
//		dao.add("5554","2");
//	}
	
	
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
			Log.i(TAG, "position:"+position+",内存地址：convertView:"+convertView);
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
				//创建时找到他们的引用，存放在hoder中   保存在view对象里
				view.setTag(holder);
				
			}else{
				Log.i(TAG, "旧的view对象，复用旧的缓存的view对象："+position);
				view =convertView;
				holder=(ViewHolder) view.getTag();//效率提高约5%
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
	
	private EditText et_blacknumber;
	private CheckBox cb_phone;
	private CheckBox cb_sms;
	private Button bt_ok;
	private Button bt_cancel;
	
	/**
	 * 添加黑名单号码
	 * @param view
	 */
	public void addBlackNumber(View view){
		AlertDialog.Builder builder=new Builder(this);
		final AlertDialog dialog=builder.create();
		View contentView=View.inflate(this,R.layout.dialog_add_blacknumber,null);
		et_blacknumber=(EditText) contentView.findViewById(R.id.et_blacknumber);
		cb_phone=(CheckBox) contentView.findViewById(R.id.cb_phone);
		cb_sms=(CheckBox) contentView.findViewById(R.id.cb_sms);
		bt_ok=(Button) contentView.findViewById(R.id.bt_ok);
		bt_cancel=(Button) contentView.findViewById(R.id.bt_cancel);
		dialog.setView(contentView,0,0,0,0);
		dialog.show();
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String blacknumber=et_blacknumber.getText().toString().trim();
				if(TextUtils.isEmpty(blacknumber)){
					Toast.makeText(CallSmsSafeActivity.this,"号码不能为空",Toast.LENGTH_SHORT).show();
					return;
				}
				String mode;
				if(cb_phone.isChecked() && cb_sms.isChecked()){
					//全部选中  全部拦截
					mode="0";
				}else if(cb_phone.isChecked() && !cb_sms.isChecked()){
					//电话拦截
					mode="1";
				}else if(cb_sms.isChecked() && !cb_phone.isChecked()){
					//短信拦截
					mode="2";
				}else{
					Toast.makeText(CallSmsSafeActivity.this,"还没有选择拦截模式",Toast.LENGTH_SHORT).show();
					return;
				}
				//添加到数据库中
				dao.add(blacknumber, mode);
				//更新listview集合里面的内容
				BlackNumberInfo info=new BlackNumberInfo();
				info.setNumber(blacknumber);
				info.setMode(mode);
				infos.add(0,info);
				//通知listview数据适配器数据更新
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		
	}
	
	
	
}

