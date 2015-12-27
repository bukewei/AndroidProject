package com.example.mobilesafe.ui;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {
	private CheckBox cb_status;
	private TextView tv_title;
	private TextView tv_desc;
	
	private String desc_on;
	private String desc_off;
	
	/**
	 * 初始化布局文件
	 * @param context 上下文
	 */
	private void iniView(Context context) {
		//把一个布局文件--->转化成View 并加载到 SettingItemView上
		View.inflate(context, R.layout.setting_item_view,this);
		cb_status=(CheckBox) this.findViewById(R.id.cb_status);
		tv_desc=(TextView) this.findViewById(R.id.tv_desc);
		tv_title=(TextView) this.findViewById(R.id.tv_title);
		
	}
	/**
	 * 获取组合空间是否选中
	 * @return
	 */
	public boolean isChecked(){
		return cb_status.isChecked();
	}
	/**
	 * 设置组合空间是否选中
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		if(checked){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
		cb_status.setChecked(checked);
	}
	/**
	 * 设置组合空间的描述信息
	 * @param text
	 */
	public void setDesc(String text) {
		tv_desc.setText(text);
	}
	/**
	 * 设置组合空间的描述信息
	 * @param checked true为开启  false为关闭
	 */
	public void setDesc(boolean checked){
		if(checked){
			tv_desc.setText("自动更新已开启");
		}else{
			tv_desc.setText("自动更新已关闭");
		}
		
	}
	/**
	 * 设置组合控件的状态
	 * @param status  true为开启  false为关闭
	 */
	public void setStatus(boolean status){
		setChecked(status);
		setDesc(status);
	}
	
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}

	/**
	 * 带有两个参数的构造方法是在布局文件使用的时候调用
	 * @param context
	 * @param attrs
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		String title=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "title");
		desc_on=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "desc_on");
		desc_off=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "desc_off");
		tv_title.setText(title);
		setDesc(desc_off);
	}

	/**
	 * 只有一个参数的构造方法是在new一个控件的时候调用
	 * @param context
	 */
	public SettingItemView(Context context) {
		super(context);
		iniView(context);
	}

}
