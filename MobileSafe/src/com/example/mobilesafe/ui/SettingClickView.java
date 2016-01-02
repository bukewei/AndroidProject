package com.example.mobilesafe.ui;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 自定义的组合控件，包含有两个TextView 一个ImageView和一个View
 */
public class SettingClickView extends RelativeLayout {
	
	private TextView tv_title;
	private TextView tv_desc;
	
	private String desc_on;
	private String desc_off;

	/**
	 * 初始化布局文件
	 * @param context  上下文
	 */
	private void iniView(Context context){
		//把布局文件view加载到SettingClickView
		View.inflate(context, R.layout.setting_click_view,this);
		tv_desc=(TextView) this.findViewById(R.id.tv_desc);
		tv_title=(TextView) this.findViewById(R.id.tv_title);
	}
	
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
		iniView(context);
	}
	/**
	 * 带有两个参数的构造方法是在布局文件使用的时候调用
	 * @param context
	 * @param attrs
	 */
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		iniView(context);
		
		String title=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "title");
		desc_on=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "desc_on");
		desc_off=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "desc_off");
		tv_title.setText(title);
		setDesc(desc_off);
	}

	public SettingClickView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		iniView(context);
	}

	/**
	 * 设置组合控件状态
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		if(checked){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
	}
	
	/**
	 * 设置组合空间的描述信息
	 * @param desc
	 */
	public void setDesc(String desc) {
		tv_desc.setText(desc);
	}
	/**
	 * 设置控件标题
	 * @param title
	 */
	public void setTitle(String title){
		tv_title.setText(title);
	}
}
