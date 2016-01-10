package com.example.mobilesafe;

import com.example.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberAddressQueryActivity extends Activity {
	
	private EditText et_phone;
	private TextView tv_result;
	
	//系统的震动服务
	private Vibrator vibrator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_addres_query);
		
		vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
		
		et_phone=(EditText) findViewById(R.id.et_phone);
		tv_result=(TextView) findViewById(R.id.tv_result);
		
		et_phone.addTextChangedListener(new TextWatcher() {
			/**
			 * 文本发生改变时回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
	//			System.out.println("文本发生变化了！！！");
				System.out.println(s.toString());
				if(s != null && s.length() >= 3){
					//查询数据库，并显示结果
					String address=NumberAddressQueryUtils.queryNumber(s.toString());
					tv_result.setText(address);
				}else {
					tv_result.setText(s.toString());
				}
				
			}
			/**
			 * 当文本发生变化之前回调
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO 自动生成的方法存根
				
			}
			/**
			 * 当文本发生变化之后回调
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				
			}
		});
	}
	
	/**
	 * 查询你号码归属地
	 * @param view
	 */
	public void numberAddressQuery(View view){
		String phone=et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this,"要查询的号码不能为空",Toast.LENGTH_SHORT).show();
			//文本框抖动
			Animation shake=AnimationUtils.loadAnimation(this,R.anim.shake);
			et_phone.startAnimation(shake);
			//手机震动
	//		vibrator.vibrate(2000);//震动多长时间
	//		long[] pattern={100,200,200,300,300,400,100,500};
			long[] pattern={100,100};
			//-1：不重复 0：循环震动     1：从数组的第二个开始
			vibrator.vibrate(pattern, -1);
			
		}else{
			//开始查询
			String address=NumberAddressQueryUtils.queryNumber(phone);
			tv_result.setText(address);
		}
				
	}
	
	
	
}
