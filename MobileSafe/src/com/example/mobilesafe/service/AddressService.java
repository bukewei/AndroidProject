package com.example.mobilesafe.service;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AddressService extends Service {
	
	//窗体管理者
	private WindowManager wm;
	private View view;
	
	//电话服务
	private TelephonyManager tm;
	private MyListenerPhone listenerPhone;
	
	private OutCallReceiver receiver;

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
	
	/**
	 * 自定义土司
	 * @param address
	 */
	public void myToast(String address){
		view=View.inflate(this,R.layout.address_show,null);
		TextView textView=(TextView) view.findViewById(R.id.tv_address);
		
		//"半透明","活力橙","卫士蓝","金属灰","苹果绿"
		int [] ids={R.drawable.call_locate_white,R.drawable.call_locate_orange,
				R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green};
		SharedPreferences sp=getSharedPreferences("config",MODE_PRIVATE);
		view.setBackgroundResource(ids[sp.getInt("which",0)]);
		
		textView.setText(address);
		//设置窗体的参数
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;//包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;//没有焦点，不可触 不锁屏
        params.format = PixelFormat.TRANSLUCENT;//半透明
        params.type = WindowManager.LayoutParams.TYPE_TOAST;//Toast类型
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
