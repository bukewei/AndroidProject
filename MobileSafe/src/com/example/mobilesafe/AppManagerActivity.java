package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.domain.AppInfo;
import com.example.mobilesafe.engine.AppInfoProvider;
import com.example.mobilesafe.utils.DensityUtil;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppManagerActivity extends Activity {

	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	
	private ListView lv_app_manager;
	private LinearLayout ll_loading;

	//所有的应用程序包信息
	private List<AppInfo> appInfos;
	//用户应用程序集合
	private List<AppInfo> userAppInfos;
	//系统应用程序集合
	private List<AppInfo> systemAppInfos;
	//当前程序信息的状态
	private TextView tv_status;
	/**
	 * 弹出的悬浮窗体
	 */
	private PopupWindow popupWindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		
		tv_avail_rom=(TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd=(TextView) findViewById(R.id.tv_avail_sd);
		tv_status=(TextView) findViewById(R.id.tv_status);
		//得到SD卡可用空间大小
		long SdSize=getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
		//得到内存可用大小
		long RomSize=getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
		//把内容格式化成文件大小形式的字符串
		tv_avail_rom.setText("手机内存可用："+Formatter.formatFileSize(this,RomSize));
		tv_avail_sd.setText("SD卡可用："+Formatter.formatFileSize(this,SdSize));
		
		lv_app_manager=(ListView) findViewById(R.id.lv_app_manager);
		ll_loading=(LinearLayout) findViewById(R.id.ll_loading);
		
		ll_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run(){
				appInfos=AppInfoProvider.getAppInfos(AppManagerActivity.this);
				userAppInfos=new ArrayList<AppInfo>();
				systemAppInfos=new ArrayList<AppInfo>();
				for(AppInfo info : appInfos){
					if(info.isUserApp()){
						userAppInfos.add(info);
					}else{
						systemAppInfos.add(info);
					}
				}
				
				//加载listview的数据适配器
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
					lv_app_manager.setAdapter(new AppManagerAdapter());
					ll_loading.setVisibility(View.INVISIBLE);
						
					}
				});
			};
		}.start();
		
		//给listview注册一个滚动监听器
		lv_app_manager.setOnScrollListener(new OnScrollListener() {
			/**
			 * 滚动状态改变
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			/**
			 * 滚动的时候调用
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
				dismissPopupWindow();
				
				// firstVisibleItem：第一个可见条目在listview集合的
				if(userAppInfos != null && systemAppInfos != null){
					if(firstVisibleItem > userAppInfos.size()){
						tv_status.setText("系统程序："+systemAppInfos.size()+"个");
					}else{
						tv_status.setText("用户程序："+userAppInfos.size()+"个");
					}
				}
				
			}
		});
		/**
		 * 设置listview的点击事件
		 */
		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AppInfo appInfo;
				if(position == 0){
					return;
				}else if(position==(userAppInfos.size()+1)){
					return;
				}else{
					if(position<=userAppInfos.size()+1){
						//点击的是用户程序
						int newposition=position-1;
						appInfo=userAppInfos.get(newposition);
					}else{
						//点击的是系统程序
						int newposition=position-1-userAppInfos.size()-1;
						appInfo=systemAppInfos.get(newposition);
					}
					
					dismissPopupWindow();
					
//					TextView contentView=new TextView(AppManagerActivity.this);
//					contentView.setText(appInfo.getPackname());
//					contentView.setTextColor(Color.BLACK);
					View contentView=View.inflate(AppManagerActivity.this,R.layout.popu_app_item, null);
					//-2：代表包裹内容
					popupWindow=new PopupWindow(contentView,-2,-2);
					//动画效果的播放要求窗体必须有背景色
					//设置背景资源      设置为透明
					popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					int[] location=new int[2];
					//获取view在窗口的位置
					view.getLocationInWindow(location);
					//在代码里面设置的宽和高 都是像素单位   在这里为了适配不同分辨率需要转换成  dip  （dp是dip的简写）
					int dipX=DensityUtil.px2dip(AppManagerActivity.this,location[0]+100);
					//设置显示位置     parent父控件      显示在左上角
					popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP,dipX,location[1]);
					
					ScaleAnimation sa=new ScaleAnimation(0.3f,1.0f,0.3f,1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0.5f);
					sa.setDuration(300);//持续时间
					AlphaAnimation aa=new AlphaAnimation(0.5f,1.0f);
					aa.setDuration(300);
					AnimationSet set=new AnimationSet(false);
					set.addAnimation(aa);
					set.addAnimation(sa);
					contentView.startAnimation(set);
				}
				
			}
			
		});
		
	}
	
	private class AppManagerAdapter extends BaseAdapter{
		//控制listview的条目多少
		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
		//	return appInfos.size();
			return userAppInfos.size()+1+systemAppInfos.size()+1;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AppInfo appInfo;
			if(position == 0){//显示的是应用程序有多少个   标签
				TextView tView=new TextView(AppManagerActivity.this);
				tView.setTextColor(Color.WHITE);
				tView.setBackgroundColor(Color.GRAY);
				tView.setText("用户程序："+userAppInfos.size()+"个");
				return tView;
			}else if(position == (userAppInfos.size()+1)){//用户程序显示玩后
				TextView tv=new TextView(AppManagerActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统程序："+systemAppInfos.size()+"个");
				return tv;
			}else if(position <= userAppInfos.size()){
				//显示用户程序
				int newposition=position-1;//多了一个item要减去
				appInfo=userAppInfos.get(newposition);
			}else{
				//显示系统程序
				//减去除了系统程序的所有item  得到在systemAppInfos里的位置
				int newposition=position-1-userAppInfos.size()-1;
				appInfo=systemAppInfos.get(newposition);
			}
			
			
			View view;
			ViewHolder holder;
			//convertView不为空，并且是一个相对布局
			if(convertView != null && convertView instanceof RelativeLayout){
				//有旧的view对象    还要判断是否是合适的类型去复用
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}else{
				//没有旧的view对象
				view=View.inflate(AppManagerActivity.this,R.layout.list_item_appinfo,null);
				holder=new ViewHolder();
				holder.iv_icon=(ImageView) view.findViewById(R.id.iv_app_icon);
				holder.tv_location=(TextView) view.findViewById(R.id.tv_app_location);
				holder.tv_name=(TextView) view.findViewById(R.id.tv_app_name);
				view.setTag(holder);
			}
	//		appInfo=appInfos.get(position);
			//显示图标
			holder.iv_icon.setImageDrawable(appInfo.getIcon());
			//显示应用名
			holder.tv_name.setText(appInfo.getAppname());
			//显示安装位置
			if(appInfo.isInRom()){
				holder.tv_location.setText("手机内存");
			}else{
				holder.tv_location.setText("外部存储");
			}
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
	 * view对象的容器 记录view的内存地址。 相当于一个记事本
	 */
	static class ViewHolder{
		public TextView tv_name;//名字
		public TextView tv_location;//位置
		public ImageView iv_icon;//图标
	}
	
	/**
	 * 获取某个目录的可用空间大小
	 * @param path  要获取可用空间大小的目录
	 * @return   返回可用空间大小 long类型
	 */
	private long getAvailSpace(String path){
		//文件系统api
		StatFs statf=new StatFs(path);
		//获取分区的个数
		statf.getBlockCount();
		//获取分区的大小
		long size=statf.getBlockSize();
		//获取可用的区块个数
		long count=statf.getAvailableBlocks();
		
		//返回可用空间大小
		return size*count;
	}
	
	/**
	 * 把旧的弹出窗体关闭
	 */
	private void dismissPopupWindow(){
		//不为空，并且是显示状态
		if(popupWindow != null && popupWindow.isShowing()){
			popupWindow.dismiss();//关闭
			popupWindow=null;
		}
	}
	
	@Override
	protected void onDestroy() {
		dismissPopupWindow();
		super.onDestroy();
	}
	
	
}
