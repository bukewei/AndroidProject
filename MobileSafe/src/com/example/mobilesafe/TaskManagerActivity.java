package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.domain.TaskInfo;
import com.example.mobilesafe.engine.TaskInfoProvider;
import com.example.mobilesafe.utils.SystemInfoUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 任务管理
 */
public class TaskManagerActivity extends Activity {
	
	private TextView tv_process_count;
	private TextView tv_mem_info;
	private LinearLayout ll_loading;
	private ListView lv_task_manager;
	private TextView tv_status;
	
	private List<TaskInfo> allTaskInfos;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;
	/**
	 * 进程数
	 */
	private int processCount;
	private long availMem;
	private long totalMen;
	private TaskManagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		
		tv_mem_info=(TextView) findViewById(R.id.tv_mem_info);
		tv_process_count=(TextView) findViewById(R.id.tv_process_count);
		ll_loading=(LinearLayout) findViewById(R.id.ll_loading);
		lv_task_manager=(ListView) findViewById(R.id.lv_task_manager);
		tv_status=(TextView) findViewById(R.id.tv_status);
		
		fillData();
		//滚动事件
		lv_task_manager.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO 自动生成的方法存根
				
			}
			/**
			 * 滚动调用
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(userTaskInfos != null && systemTaskInfos !=null){
					if(firstVisibleItem>userTaskInfos.size()){
						tv_status.setText("系统进程："+systemTaskInfos.size()+"个");
					}else{
						tv_status.setText("用户进程："+userTaskInfos.size()+"个");
					}
				}
			}
		});
		//listview条目点击事件
		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {
			/**
			 * 条目被点击
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TaskInfo taskInfo;
				if(position==0){
					return;
				}else if(position==(userTaskInfos.size()+1)){
					return;
				}else if(position <= userTaskInfos.size()){
					taskInfo=userTaskInfos.get(position-1);
				}else{
					taskInfo=systemTaskInfos.get(position-1-userTaskInfos.size()-1);
				}
				//是不是本应用
				if(getPackageName().equals(taskInfo.getPackname())){
					return;
				}
				ViewHolder holder=(ViewHolder) view.getTag();
				if(taskInfo.isChecked()){
					//变为未选中
					taskInfo.setChecked(false);
					holder.cb_status.setChecked(false);
				}else{
					//变为选中
					taskInfo.setChecked(true);
					holder.cb_status.setChecked(true);
				}
				
			}
			
		});
		
	}
	/**
	 * 填充数据
	 */
	private void fillData(){
		ll_loading.setVisibility(View.VISIBLE);//可见
		new Thread(){
			public void run() {
				allTaskInfos=TaskInfoProvider.getTaskInfos(TaskManagerActivity.this);
				userTaskInfos=new ArrayList<TaskInfo>();
				systemTaskInfos=new ArrayList<TaskInfo>();
				for(TaskInfo info:allTaskInfos){
					if(info.isUserTask()){
						userTaskInfos.add(info);
					}else{
						systemTaskInfos.add(info);
					}
				}
				//更新界面
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);//不可见
						if(adapter == null){
							adapter=new TaskManagerAdapter();
							lv_task_manager.setAdapter(adapter);
						}else{
							//通知数据适配器数据已改变
							adapter.notifyDataSetChanged();
						}
						setTitle();
					}
				});
			};
		}.start();
	}
	
	private void setTitle(){
		processCount=SystemInfoUtils.getRunningProcessCount(this);
		tv_process_count.setText("运行中的进程："+processCount+"个");
		availMem=SystemInfoUtils.getAvailMem(this);
		totalMen=SystemInfoUtils.getTotalMen(this);
		//格式化文本
		String avail=Formatter.formatFileSize(this,availMem);
		String total=Formatter.formatFileSize(this,totalMen);
		tv_mem_info.setText("剩余/总内存："+avail+"/"+total);
	}
	
	/**
	 * 进程管理适配器
	 */
	private class TaskManagerAdapter extends BaseAdapter{
		private static final String TAG="TaskManagerAdapter";
		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return userTaskInfos.size()+1+systemTaskInfos.size()+1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TaskInfo taskInfo;
			if(position==0){
				//标签显示用户进程数
				TextView tv=new TextView(TaskManagerActivity.this);
				tv.setBackgroundColor(Color.GRAY);//灰色
				tv.setTextColor(Color.WHITE);//白色
				tv.setText("用户进程："+userTaskInfos.size()+"个");
				return tv;
			}else if(position==(userTaskInfos.size()+1)){
				//标签显示系统进程数
				//标签显示用户进程数
				TextView tv=new TextView(TaskManagerActivity.this);
				tv.setBackgroundColor(Color.GRAY);//灰色
				tv.setTextColor(Color.WHITE);//白色
				tv.setText("系统进程："+systemTaskInfos.size()+"个");
				return tv;
			}else if(position<=userTaskInfos.size()){//position从零开始不需要加1
				taskInfo=userTaskInfos.get(position-1);
			}else{
				taskInfo=systemTaskInfos.get(position-1-userTaskInfos.size()-1);
			}
			View view;
			ViewHolder holder;
			//有旧的view 并且旧的view是一个相对布局   就复用
			if(convertView != null && convertView instanceof RelativeLayout){
				view=convertView;
				holder=(ViewHolder) view.getTag();
				Log.i(TAG, "复用缓存："+position);
			}else{
				view=View.inflate(TaskManagerActivity.this,R.layout.list_item_taskinfo,null);
				holder=new ViewHolder();
				holder.iv_icon=(ImageView) view.findViewById(R.id.iv_task_icon);
				holder.tv_name=(TextView) view.findViewById(R.id.tv_task_name);
				holder.tv_memsize=(TextView) view.findViewById(R.id.tv_task_memsize);
				holder.cb_status=(CheckBox) view.findViewById(R.id.cb_status);
				view.setTag(holder);
				Log.i(TAG, "创建新的view对象："+position);
			}
			holder.iv_icon.setImageDrawable(taskInfo.getIcon());
			holder.tv_name.setText(taskInfo.getName());
			String memSize=Formatter.formatFileSize(TaskManagerActivity.this,taskInfo.getMemsize());
			holder.tv_memsize.setText(memSize);
			holder.cb_status.setChecked(taskInfo.isChecked());
			//不能杀死本程序的进程
			if(getPackageName().equals(taskInfo.getPackname())){
				holder.cb_status.setVisibility(View.INVISIBLE);
			}else{
				holder.cb_status.setVisibility(View.VISIBLE);
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
	
	
	static class ViewHolder{
		public ImageView iv_icon;
		public TextView tv_name;
		public TextView tv_memsize;
		public CheckBox cb_status;
	}
	/**
	 * 选中全部
	 * @param view
	 */
	public void selectAll(View view){
		for(TaskInfo info:allTaskInfos){
			if(getPackageName().equals(info.getPackname())){
				continue;
			}
			info.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 反选
	 * @param view
	 */
	public void selectOppo(View view){
		for(TaskInfo info:allTaskInfos){
			if(getPackageName().equals(info.getPackname())){
				continue;
			}
			info.setChecked(!info.isChecked());
		}
		adapter.notifyDataSetChanged();
	}
	/**
	 * 一键清理
	 * @param view
	 */
	public void killAll(View view){
		ActivityManager am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int count=0;
		long savedMem=0;
		//记录被杀死的进程
		List<TaskInfo> killedTaskInfos=new ArrayList<TaskInfo>();
		for(TaskInfo info:allTaskInfos){
			if(info.isChecked()){
				//如果被勾选就清理
				am.killBackgroundProcesses(info.getPackname());
				if(info.isUserTask()){
					userTaskInfos.remove(info);
				}else{
					systemTaskInfos.remove(info);
				}
				killedTaskInfos.add(info);
				count++;
				savedMem+=info.getMemsize();
			}
		}
		
		allTaskInfos.removeAll(killedTaskInfos);
		adapter.notifyDataSetChanged();
		String freeMemSize=Formatter.formatFileSize(this,savedMem);
		Toast.makeText(this,"杀死了"+count+"个进程，释放了"+freeMemSize+"内存",Toast.LENGTH_LONG).show();
		processCount-=count;
		availMem+=savedMem;
		tv_process_count.setText("运行中的进程："+processCount+"个");
		//格式化文本
		String avail=Formatter.formatFileSize(this,availMem);
		String total=Formatter.formatFileSize(this,totalMen);
		tv_mem_info.setText("剩余/总内存："+avail+"/"+total);

	}
	/**
	 * 进入设置
	 * @param view
	 */
	public void enterSetting(View view){
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		adapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
