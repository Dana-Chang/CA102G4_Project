package com.attractions.model;

import java.io.*;
import java.util.*;

public class AttractionsDAO_Test {
	public static void main(String[] args) {
		AttractionsDAO_interface dao = new AttractionsJDBCDAO();
		
		//新增
//		AttractionsVO vo1 = new AttractionsVO();
//		vo1.setAtt_name("台北101");
//		vo1.setAtt_lat(25.033611);
//		vo1.setAtt_lon(121.562283);
//		vo1.setCountry("台灣");
//		vo1.setAdministrative_area("台北市");
//		vo1.setAtt_information("台北101（TAIPEI 101）是位於臺灣臺北市信義區的摩天大樓，樓高509.2公尺（1,671英尺），地上樓層共有101層、另有地下5層，總樓地板面積37萬4千平方公尺，由李祖原聯合建築師事務所設計，KTRT團隊承造，於1999年9月動工，2004年12月31日完工開幕。最初名稱為臺北國際金融中心（英語：Taipei World Financial Center），2003年改為現名。興建與經營機構為台北金融大樓公司。其為台灣第一高樓以及唯一樓層超過100層的台灣建築物，曾於2004年12月31日至2010年1月4日間擁有世界第一高樓的紀錄，目前為世界第八高樓以及地震帶最高摩天大廈，完工以來即成為臺北重要地標之一。此外，大樓內擁有全球第二大（僅次上海中心大廈）、全球唯二開放遊客觀賞的巨型阻尼器（另一為上海中心大廈），以及目前全球起降速度第四快的電梯，僅次於哈里發塔（全球第三快）、上海中心大廈（全球第二快）、廣州周大福金融中心（全球最快）。");
//		vo1.setAtt_picture(getPictureByteArray("C:\\ca102g4_pics\\attractions\\101.jpg"));
//		vo1.setAtt_address("110台北市信義區信義路五段7號");
//		vo1.setAtt_status(1);
//		dao.insert(vo1);
		
//		修改
//		AttractionsVO vo2 = new AttractionsVO();
//		vo2.setAtt_no("A000000001");
//		vo2.setAtt_name("西門町");
//		vo2.setAtt_lat(25.042603);
//		vo2.setAtt_lon(121.507527);
//		vo2.setCountry("台灣");
//		vo2.setAdministrative_area("台北市");
//		vo2.setAtt_information("西門町因為位於台北城的西門外，因此得名。在日治時代，西門町原本還只是一片荒涼的地方，後來日本人決定仿效東京淺草區，在此設立休閒商業區。其中最早的娛樂設施為1897年的台北座、1902年的榮座（現為新萬國商場）及1908年的八角堂（今西門紅樓）。新世界館（現為誠品116、真善美戲院）旁的街道，稱為「片倉通」，有許多日本料理店。");
//		vo2.setAtt_picture(getPictureByteArray("C:\\ca102g4_pics\\attractions\\A000000001.jpg"));
//		vo2.setAtt_address("台北市萬華區西門町");
//		vo2.setAtt_status(1);
//		dao.update(vo2);
		
//		//刪除
//		dao.delete("A000000011");
		
//		//主鍵搜尋
		AttractionsVO vo3 = dao.findByPrimaryKey("A000000001");
		System.out.println(vo3.toString());

		
		//全搜尋
//		List<AttractionsVO> list = dao.getAll();
//		for (AttractionsVO att : list) {
//			System.out.println(att.toString());
//		}
		//圖片搜尋
//		byte[] att_picture = dao.getAttPicture("A000000001");
//		System.out.println(att_picture);
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
