package com.example.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	//1.定义一个手势识别器
	private GestureDetector detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		
		//2.实例化这个手势识别器
		detector=new GestureDetector(this,new SimpleOnGestureListener(){
			/**
			 * 当手指在上面滑动时调用
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				//屏蔽在x轴滑动很慢的情形    单位：像素
			//	Math.abs();求绝对值
				if(Math.abs(velocityX)<200){
					Toast.makeText(getApplicationContext(),"滑动的太慢了",0).show();
					System.out.println("滑动的太慢了");
					return true;
				}
				//屏蔽斜划
				if(Math.abs(e2.getRawY()-e1.getRawY()) > 100){
					Toast.makeText(getApplicationContext(),"不能斜划",0).show();
					System.out.println("不能斜划");
					return true;
				}
				//从左往右滑动
				if(e2.getRawX()-e1.getRawX() > 200){
					System.out.println("从左往右滑动,显示上一个页面");
					showPre();
					return true;
				}
				//从右往左滑动
				if(e1.getRawX()-e2.getRawX() > 200){
					System.out.println("从右往左滑动,显示下一个页面");
					showNext();
					return true;
				}
				
				
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	
	//3.使用手势识别器
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	
	//抽象方法，需要在子类中实现
	public abstract void showNext();
	public abstract void showPre();
	
	/**
	 * 下一步的点击事件
	 * @param view
	 */
	public void next(View view){
		showNext();
	}
	/**
	 * 上一步的点击事件
	 * @param view
	 */
	public void pre(View view){
		showPre();
	}
	
	
	
	
}
