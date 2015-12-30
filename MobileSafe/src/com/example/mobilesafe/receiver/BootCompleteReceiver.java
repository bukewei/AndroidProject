package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
		boolean pritecting = sp.getBoolean("protecting", false);
		if(pritecting){
			//已开启防盗保护
			
			//读取保存的SIM卡序列号和当前的进行对比
//			String saveSim=sp.getString("sim","")+"test";//模拟序列号不同
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
				
				//发送信息
//				SmsManager.getDefault().sendTextMessage(sp.getString("safenumber",""), null,"SIM卡以变更", null,null);
				//第一个参数：对方手机号码
				//第二个参数：短信中心号码，一般设置为空
				//第三个参数：短信内容
				//第四个参数：sentIntent判断短信是否发送成功，如果你没有SIM卡，或者网络中断，则可以通过这个intent来判断。
				//注意强调的是“发送”的动作是否成功。那么至于对于对方是否收到，另当别论
				//第五个参数：当短信发送到收件人时，会收到这个deliveryIntent。即强调了“发送”后的结果
				//就是说是在"短信发送成功"和"对方收到此短信"才会激活sentIntent和deliveryIntent这两个Intent。这也相当于是延迟执行了Intent
				
			}
		}else{
			//没有开启防盗保护
			System.out.println("没有开启防盗保护");
		}
		
		
		
		
	}

}
