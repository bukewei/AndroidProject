package com.example.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * 应用程序信息的业务bean
 */
public class AppInfo {
	private Drawable icon;//图标
	private String appname;//应用名
	private String packname;//包名
	private boolean inRom;//内置应用
	private boolean userApp;//用户安装
	
	
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	
	public boolean isInRom() {
		return inRom;
	}
	public boolean isUserApp() {
		return userApp;
	}
	
	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}
	
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	

	@Override
	public String toString() {
		return "AppInfo [icon=" + icon + ", appname=" + appname + ", packname=" + packname + ", inRom=" + inRom
				+ ", userApp=" + userApp + "]";
	}
}
