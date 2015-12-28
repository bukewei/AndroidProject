package com.example.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	/**
	 * 标准的MD5算法加密
	 * @param pwd 要被加密的密码
	 * @return 返回加密后的密码
	 */
	public static String Md5Pwd(String pwd) {
		
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
//			String password="123456";
			byte [] bytes=digest.digest(pwd.getBytes());
			StringBuffer buffer=new StringBuffer();
			for(byte b: bytes){
				//用每个byte去和11111111做与运算并且得到的是int类型的值：
				//byte & 11111111;  0xff是十六进制，十进制为255
				int number =b & 0xff;
				String str=Integer.toHexString(number);
				if(str.length() == 1){
					buffer.append("0");
				}
				buffer.append(str);
			}
			//md5加密的值
	//		System.out.println(buffer.toString());
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成的 catch 块
			System.out.println("加密算法没有找到");
			e.printStackTrace();
			return "";
		}
		
	}
}
