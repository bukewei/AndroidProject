package com.example.mobilesafe.domain;


/**
 *黑名单号码的业务bean 
 */
public class BlackNumberInfo {
	private String number;//黑名单号码
	private String mode;//黑名单号码的拦截模式
	
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	@Override
	public String toString() {
		return "BlackNumberInfo [number="+number+",mode="+mode+"]";
	}
}
