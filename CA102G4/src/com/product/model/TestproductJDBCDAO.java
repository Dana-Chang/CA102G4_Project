package com.product.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;



public class TestproductJDBCDAO {

	public static void main(String[] args) {
		ProductJDBCDAO dao = new ProductJDBCDAO();
		
		// Insert
		ProductVO product1 = new ProductVO();
		product1.setProduct_category_id(10);
		product1.setProduct_mem_id("M000003");
		product1.setProduct_name("鋁框旅行箱 20吋銀");
		product1.setProduct_price(700);
		product1.setProduct_descr("行李箱內里為雙面多層收納設計絕非市售僅單薄單面無隔層內里！！！\r\n" + 
				"沒有鍍膜理論。亮面材質因為PC層並非再強化抗性，因此硬度不會更高！而遇ABS較接近，因此亮面，重擊後多半是直接箱體破裂報銷，而非擁有三層pc加厚防刮拉絲僅表皮層磨損。（歡迎大家Google只有電鍍框，何謂行李箱pc殼有鍍面技術？？）• PC塑料混合ABS基材，硬殼材質，極輕。\r\n" + 
				"• 鋁合金邊框，全四角邊角會被鋁合金邊框保護著。（絕非市售只有造型功能的正面兩角）\r\n" + 
				"• 8顆 360 度靜音飛機輪，極好推超好推。\r\n" + 
				"• 兩個國際 TSA 海關密碼鎖。\r\n" + 
				"• 隱藏式掛勾架，方便掛購物袋或是包包。\r\n" + 
				"• 行李箱內裏換成玫瑰金色更有質感。\r\n" + 
				"• 箱輪與手把和行李箱完全一致同色。\r\n" + 
				"• 產地：中國製，廠房打版生產，生產工廠營運三十幾年，終身不缺料！（由台灣多一道嚴格檢驗嚴選）");
		product1.setProduct_stock(1);
		product1.setProduct_status(1);
		product1.setProduct_date(new Timestamp(System.currentTimeMillis()));
		byte[] pic1 = null;
		byte[] pic2 = null;
		byte[] pic3 = null;
		byte[] pic4 = null;

		try {
			pic1 = getPictureByteArray("C:\\ca102g4_pics\\product\\prod4_1.jpg");
			pic2 = getPictureByteArray("C:\\ca102g4_pics\\product\\prod4_2.jpg");
			pic3 = getPictureByteArray("C:\\ca102g4_pics\\product\\prod4_3.jpg");
			pic4 = getPictureByteArray("C:\\ca102g4_pics\\product\\prod4_4.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		product1.setProduct_photo_1(pic1);
		product1.setProduct_photo_2(pic2);
		product1.setProduct_photo_3(pic3);
		product1.setProduct_photo_4(pic4);

		dao.insert(product1);
		System.out.println("---------------------");
		
		
		// 修改
//		ProductVO product2 = new ProductVO();
//		product2.setProduct_category_id(10);
//		product2.setProduct_mem_id("M000003");
//		product2.setProduct_name("鋁框旅行箱 20吋銀");
//		product2.setProduct_price(700);
//		product2.setProduct_descr("行李箱內里為雙面多層收納設計絕非市售僅單薄單面無隔層內里！！！\r\n" + 
//				"沒有鍍膜理論。亮面材質因為PC層並非再強化抗性，因此硬度不會更高！而遇ABS較接近，因此亮面，重擊後多半是直接箱體破裂報銷，而非擁有三層pc加厚防刮拉絲僅表皮層磨損。（歡迎大家Google只有電鍍框，何謂行李箱pc殼有鍍面技術？？）• PC塑料混合ABS基材，硬殼材質，極輕。\r\n" + 
//				"• 鋁合金邊框，全四角邊角會被鋁合金邊框保護著。（絕非市售只有造型功能的正面兩角）\r\n" + 
//				"• 8顆 360 度靜音飛機輪，極好推超好推。\r\n" + 
//				"• 兩個國際 TSA 海關密碼鎖。\r\n" + 
//				"• 隱藏式掛勾架，方便掛購物袋或是包包。\r\n" + 
//				"• 行李箱內裏換成玫瑰金色更有質感。\r\n" + 
//				"• 箱輪與手把和行李箱完全一致同色。\r\n" + 
//				"• 產地：中國製，廠房打版生產，生產工廠營運三十幾年，終身不缺料！（由台灣多一道嚴格檢驗嚴選）");
//		product2.setProduct_stock(1);
//		product2.setProduct_status(1);
//		product2.setProduct_date(new Timestamp(System.currentTimeMillis()));
//		byte[] pic2_1 = null;
//		byte[] pic2_2 = null;
//		byte[] pic2_3 = null;
//		byte[] pic2_4 = null;
//	
//		try {
//			pic2_1 = getPictureByteArray("C:\\ca102g4_pics\\product\\produp4_1.jpg");
//			pic2_2 = getPictureByteArray("C:\\ca102g4_pics\\product\\produp4_2.jpg");
//			pic2_3 = getPictureByteArray("C:\\ca102g4_pics\\product\\produp4_3.jpg");
//			pic2_4 = getPictureByteArray("C:\\ca102g4_pics\\product\\produp4_4.jpg");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		product2.setProduct_photo_1(pic2_1);
//		product2.setProduct_photo_2(pic2_2);
//		product2.setProduct_photo_3(pic2_3);
//		product2.setProduct_photo_4(pic2_4);
//		product2.setProduct_id(1005);
//		dao.update(product2);
//		
//		// 刪除
//				dao.delete(1004);
//
//		// 查詢
//		productVO product3 = dao.findByPK(1001);
//		System.out.print(product3.getProduct_id() + ",");
//		System.out.print(product3.getProduct_category_id()+ ",");
//		System.out.print(product3.getProduct_mem_id() + ",");
//		System.out.print(product3.getProduct_name() + ",");
//		System.out.print(product3.getProduct_price() + ",");
//		System.out.print(product3.getProduct_descr() + ",");
//		System.out.print(product3.getProduct_stock() + ",");
//		System.out.print(product3.getProduct_status() + ",");
//		System.out.print(product3.getProduct_date() + ",");
//		System.out.print(product3.getProduct_photo_1() + ",");
//		System.out.print(product3.getProduct_photo_2() + ",");
//		System.out.print(product3.getProduct_photo_3() + ",");
//		System.out.print(product3.getProduct_photo_4() + ",");
//		System.out.print(product3.getProduct_photo_5());
//		System.out.println();
//		System.out.println("---------------------");
//		
//		// 查詢
//		List<productVO> list = dao.getAll();
//		for (productVO product : list) {
//			System.out.print(product.getProduct_id() + ",");
//			System.out.print(product.getProduct_category_id()+ ",");
//			System.out.print(product.getProduct_mem_id() + ",");
//			System.out.print(product.getProduct_name() + ",");
//			System.out.print(product.getProduct_price() + ",");
//			System.out.print(product.getProduct_descr() + ",");
//			System.out.print(product.getProduct_stock() + ",");
//			System.out.print(product.getProduct_status() + ",");
//			System.out.print(product.getProduct_date() + ",");
//			System.out.print(product.getProduct_photo_1() + ",");
//			System.out.print(product.getProduct_photo_2() + ",");
//			System.out.print(product.getProduct_photo_3() + ",");
//			System.out.print(product.getProduct_photo_4() + ",");
//			System.out.print(product.getProduct_photo_5());
//	
//			System.out.println();
//		}
//		
	}
	
	public static byte[] getPictureByteArray(String path) throws IOException {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int i;
		while ((i = fis.read(buffer)) != -1) {
			baos.write(buffer, 0, i);
		}
		baos.close();
		fis.close();

		return baos.toByteArray();
	}

}
