package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
		//读取保存的SIM卡序列号和当前的进行对比
//		String saveSim=sp.getString("sim","")+"test";//模拟序列号不同
		String saveSim=sp.getString("sim","");
		tm=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String sim=tm.getSimSerialNumber();
		if(sim.equals(saveSim)){
			//手机卡序列号没有变化
			System.out.println("手机卡未变更");
		}else{
			//序列号变化了
			System.out.println("手机卡以变更，正在发送报警信息");
			Toast.makeText(context,"手机卡以变更，正在发送报警信息",0).show();
		}
		
		
		
	}

}
