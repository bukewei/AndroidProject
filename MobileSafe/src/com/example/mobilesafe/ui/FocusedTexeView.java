package com.example.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 *自定义TextView 一生成就有焦点
 */
public class FocusedTexeView extends TextView {

	public FocusedTexeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
	}

	public FocusedTexeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}

	public FocusedTexeView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	//当前没有焦点，欺骗系统有焦点
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// 不管有没有焦点都返回true
		return true;
	}
	
}
