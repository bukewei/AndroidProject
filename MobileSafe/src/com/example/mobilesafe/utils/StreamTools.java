package com.example.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class StreamTools {
	
	/**
	 * ��������ת�����ַ���
	 * @param iStream ������
	 * @return String ���ص��ַ���
	 * @throws IOException �׳��쳣
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
