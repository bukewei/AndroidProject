package com.example.mobilesafe.service;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

public class AddressService extends Service {
	protected static final String TAG="AddressService";
	
	//窗体管理者
	private WindowManager wm;
	private View view;
	
	//电话服务
	private TelephonyManager tm;
	private MyListenerPhone listenerPhone;
	
	private OutCallReceiver receiver;
	
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}
	
	//服务里的内部类       OutCallReceiver继承广播接收者
	//在这里的广播接收者的生命周期和服务一样
	class OutCallReceiver extends BroadcastReceiver{
		/**
		 * 接收到广播
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			//获得拨出去的号码
			String outPhone=getResultData();
			//查询数据库
			String address=NumberAddressQueryUtils.queryNumber(outPhone);
//			Toast.makeText(context,address, Toast.LENGTH_LONG).show();
			//调用自定义Toast
			myToast(address);
		}
		
	}
	
	private class MyListenerPhone extends PhoneStateListener{
		/**
		 * 呼叫状态改变
		 */
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO 自动生成的方法存根
			super.onCallStateChanged(state, incomingNumber);
			//state:状态       incomingNumber:来电号码
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://来电铃声响起
				//查询数据库
				String address=NumberAddressQueryUtils.queryNumber(incomingNumber);
//				Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
				myToast(address);
				break;
				case TelephonyManager.CALL_STATE_IDLE://电话的空闲状态：挂电话、来电拒绝
					//把这个View移除
					if(view != null){
						wm.removeView(view);
					}
					break;
			default:
				break;
			}
			
			
		}
	}
	

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
		
		tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		
		//监听来电
		listenerPhone=new MyListenerPhone();
		tm.listen(listenerPhone,PhoneStateListener.LISTEN_CALL_STATE);
		
		//用代码注册广播接收者
		receiver=new OutCallReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		
		
		//实例化窗体
		wm=(WindowManager) getSystemService(WINDOW_SERVICE);
	}
	
	
	private WindowManager.LayoutParams params;
	long[] mHits=new long[2];
	Editor editor;
	
	/**
	 * 自定义土司
	 * @param address
	 */
	public void myToast(String address){
		sp=getSharedPreferences("config",MODE_PRIVATE);
		editor=sp.edit();
		
		view=View.inflate(this,R.layout.address_show,null);
		TextView textView=(TextView) view.findViewById(R.id.tv_address);
		//点击事件
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//从指定源数组中复制一个数组，复制从指定的位置开始，到目标数组的指定位置结束
//				src - 源数组。
//				srcPos - 源数组中的起始位置。
//				dest - 目标数组。
//				destPos - 目标数据中的起始位置。
//				length - 要复制的数组元素的数量
				System.arraycopy(mHits, 1,mHits,0,mHits.length-1);
				//最后一位的值等于  从开机到现在的毫秒数
				mHits[mHits.length-1]=SystemClock.uptimeMillis();
				//第一次的点击的时间距离现在不超过500毫秒
				if(mHits[0] >= (SystemClock.uptimeMillis() - 500)){
					//判定为双击
					params.x=wm.getDefaultDisplay().getWidth()/2-view.getWidth()/2;
					wm.updateViewLayout(view,params);
					Log.i(TAG,"控件被双击了");
					//记录最后的x轴坐标
					editor.putInt("lastx",params.x);
					editor.commit();
				}
				
			}
		});
		//给view对象设置一个触摸的监听器
		view.setOnTouchListener(new OnTouchListener() {
			//手指触摸的开始位置
			int startX;
			int startY;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://手指按下屏幕
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();
					Log.i(TAG,"手指触摸到控件了");
			//		System.out.println("手指触摸到控件了");
					break;
				case MotionEvent.ACTION_MOVE://手指在屏幕上移动
					int newX=(int) event.getRawX();
					int newY=(int) event.getRawY();
					int dx=newX - startX;//移动的距离
					int dy=newY - startY;//移动的距离
					
					Log.i(TAG,"手指在控件上移动");
	//				System.out.println("手指在控件上移动");
					params.x += dx;
					params.y += dy;
					//处理边界问题
					if(params.x<0){
						params.x=0;
					}
					if(params.y<0){
						params.y=0;
					}
					if(params.x > (wm.getDefaultDisplay().getWidth()-view.getWidth())){
						params.x=wm.getDefaultDisplay().getWidth()-view.getWidth();
					}
					if(params.y > (wm.getDefaultDisplay().getHeight()-view.getHeight())){
						params.y=wm.getDefaultDisplay().getHeight()-view.getHeight();
					}
					wm.updateViewLayout(view,params);
					//重新初始化手指的开始位置
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();
					
					break;
				case MotionEvent.ACTION_UP://手指离开屏幕
					//记录控件距离左上角的坐标
					Log.i(TAG, "手指离开控件");
					
					editor.putInt("lastx",params.x);
					editor.putInt("lasty",params.y);
					editor.commit();
					break;
				default:
					break;
				}
				
				//返回true 事件处理完毕，不要让父控件 父布局 响应触摸事件了
				//返回false 事件没处理完  要想响应其他事件，必须返回false
				return false;
			}
		});
		
		
		//"半透明","活力橙","卫士蓝","金属灰","苹果绿"
		int [] ids={R.drawable.call_locate_white,R.drawable.call_locate_orange,
				R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green};
		
		view.setBackgroundResource(ids[sp.getInt("which",0)]);
		
		textView.setText(address);
		//设置窗体的参数
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;//包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        
        params.gravity=Gravity.TOP+Gravity.LEFT;//默认与窗口左上角对齐
        params.x=sp.getInt("lastx",0);
        params.y=sp.getInt("lasty",0);
        
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;//没有焦点  不锁屏
        // | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE 不可触
        params.format = PixelFormat.TRANSLUCENT;//半透明
     // android系统里面具有电话优先级的一种窗体类型，记得添加权限。
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
       wm.addView(view, params);
		
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//取消监听来电
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone=null;
		
		//用代码注销广播接收者
		unregisterReceiver(receiver);
		receiver=null;
	}
	
}
