package com.orderDetails.model;

import java.sql.Timestamp;
import java.util.List;

public class TestorderDetailsJDBCDAO {

	public static void main(String[] args) {
		OrderDetailsJDBCDAO dao = new OrderDetailsJDBCDAO();
		
		// 新增
		OrderDetailsVO orderDetails1 = new OrderDetailsVO();
		orderDetails1.setDetails_product_id(1003);
		orderDetails1.setDetails_order_qty(1);
		orderDetails1.setDetails_order_total(1600);

		dao.insert(orderDetails1);
		System.out.println("---------------------");
		
		
//		// 修改
		OrderDetailsVO orderDetails2 = new OrderDetailsVO();
		
		orderDetails2.setDetails_order_qty(2);
		orderDetails2.setDetails_order_total(3200);
		orderDetails2.setDetails_order_id("20180717-000003");
		orderDetails2.setDetails_product_id(1003);
		
		dao.update(orderDetails2);

		// PK查詢
//		orderDetailsVO orderDetails3 = dao.findByPK("20180717-000003",1003);
//		System.out.print(orderDetails3.getDetails_order_id() + ",");
//		System.out.print(orderDetails3.getDetails_product_id()+ ",");
//		System.out.print(orderDetails3.getDetails_order_qty() + ",");
//		System.out.print(orderDetails3.getDetails_order_total());
//
//		System.out.println();
//		System.out.println("---------------------");
//		
		// 全查詢
//		List<orderDetailsVO> list = dao.getAll();
//		for (orderDetailsVO orderDetails : list) {
//			System.out.print(orderDetails.getDetails_order_id() + ",");
//			System.out.print(orderDetails.getDetails_product_id()+ ",");
//			System.out.print(orderDetails.getDetails_order_qty() + ",");
//			System.out.print(orderDetails.getDetails_order_total());
//			
//	
//			System.out.println();
//		}


	}

}
