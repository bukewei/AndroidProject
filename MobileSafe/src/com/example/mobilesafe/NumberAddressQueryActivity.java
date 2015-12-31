package com.example.mobilesafe;

import com.example.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberAddressQueryActivity extends Activity {
	
	private EditText et_phone;
	private TextView tv_result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_addres_query);
		
		et_phone=(EditText) findViewById(R.id.et_phone);
		tv_result=(TextView) findViewById(R.id.tv_result);
		
		et_phone.addTextChangedListener(new TextWatcher() {
			/**
			 * 文本发生改变时回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
				System.out.println("文本发生变化了！！！");
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
			
		}else{
			//开始查询
			String address=NumberAddressQueryUtils.queryNumber(phone);
			tv_result.setText(address);
		}
				
	}
	
	
	
}
