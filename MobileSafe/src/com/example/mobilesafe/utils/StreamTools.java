package com.example.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class StreamTools {
	
	/**
	 * 把输入流转化成字符串
	 * @param iStream 输入流
	 * @return String 返回的字符串
	 * @throws IOException 抛出异常
	 */
	public static String readFromStream(InputStream iStream) throws IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=0;
		while ((len=iStream.read(buffer))!=-1) {
			baos.write(buffer,0,len);
		}
		iStream.close();
		String result=baos.toString();
		baos.close();
		return result;
	}
	
	
}
