package com.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;

public class Pic {

	// 使用byte[]方式
	public static byte[] getPictureByteArray(Part part) throws IOException {

		InputStream in = part.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int i;
		while ((i = in.read(buffer)) != -1) {
			baos.write(buffer, 0, i);
		}
		baos.close();
		in.close();

		return baos.toByteArray();
	}
		
	// 取出上傳的檔案名稱 (因為API未提供method,所以必須自行撰寫)
	private static String getFileNameFromPart(Part part) {

		String header = part.getHeader("content-disposition");
		//System.out.println("header=" + header); // 測試用
		String filename = new File(header.substring(header.lastIndexOf("=") + 2, header.length() - 1)).getName();
		//System.out.println("filename=" + filename); // 測試用
		if (filename.length() == 0) {
			return null;
		}
		return filename;
	}
	
	public static boolean noFileSelected(Part part){
		
		if (getFileNameFromPart(part) == null || part.getContentType() == null) {
			return true;
		}
		return false;
	}
	
}
