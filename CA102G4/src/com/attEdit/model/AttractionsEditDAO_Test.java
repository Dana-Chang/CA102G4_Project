package com.attEdit.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class AttractionsEditDAO_Test {
	public static void main(String[] args) {
		AttractionsEditDAO_interface dao = new AttractionsEditJDBCDAO();
		
		//新增
		AttractionsEditVO vo1 = new AttractionsEditVO();

		vo1.setMem_id("M000001");
		vo1.setAtt_no("A000000001");
		vo1.setAtt_name("西門町");
		vo1.setAtt_lat(25.042603);
		vo1.setAtt_lon(121.507527);
		vo1.setCountry("台灣");
		vo1.setAtt_information("西門町因為位於台北城的西門外，因此得名。在日治時代，西門町原本還只是一片荒涼的地方，後來日本人決定仿效東京淺草區，在此設立休閒商業區。其中最早的娛樂設施為1897年的台北座、1902年的榮座（現為新萬國商場）及1908年的八角堂（今西門紅樓）。新世界館（現為誠品116、真善美戲院）旁的街道，稱為「片倉通」，有許多日本料理店。");
		vo1.setAtt_picture(getPictureByteArray("C:\\ca102g4_pics\\attractions\\A000000001.jpg"));
		vo1.setAtt_address("台北市萬華區西門町");
		dao.insert(vo1);
		
		//全搜尋
		List<AttractionsEditVO> list = dao.getAll();
		for (AttractionsEditVO vo : list) {
			System.out.println(vo.toString());
		}
		
	}
	
	public static byte[] getPictureByteArray(String path) {
		File file = new File(path);
		FileInputStream fis;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int i;

			while ((i = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, i);
			}
			baos.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		return baos.toByteArray();
	}
}
