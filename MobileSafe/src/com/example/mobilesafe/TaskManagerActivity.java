package com.example.mobilesafe;

import com.example.mobilesafe.utils.SystemInfoUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

/**
 * 任务管理
 */
public class TaskManagerActivity extends Activity {
	
	private TextView tv_process_count;
	private TextView tv_mem_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		tv_mem_info=(TextView) findViewById(R.id.tv_mem_info);
		tv_process_count=(TextView) findViewById(R.id.tv_process_count);
		
		int processCount=SystemInfoUtils.getRunningProcessCount(TaskManagerActivity.this);
		tv_process_count.setText("正在运行："+processCount+"个");
		long availMem=SystemInfoUtils.getAvailMem(this);
		long totalMen=SystemInfoUtils.getTotalMen(this);
		//格式化文本
		String avail=Formatter.formatFileSize(this,availMem);
		String total=Formatter.formatFileSize(this,totalMen);
		tv_mem_info.setText("剩余/总内存："+avail+"/"+total);
	}
	
}
