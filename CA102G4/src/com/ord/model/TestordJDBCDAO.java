package com.ord.model;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;



public class TestordJDBCDAO {

	public static void main(String[] args) {
		OrdJDBCDAO dao = new OrdJDBCDAO();
		
		// 新增
		OrdVO ord1 = new OrdVO();
		ord1.setBuyer_mem_id("M000003");
		ord1.setSeller_mem_id("M000001");
		ord1.setOrder_address("台北市大安區東豐街43號45號1樓");
		ord1.setPayment_status(1);
		ord1.setPayment_method(2);
		ord1.setShipment_status(1);
		ord1.setOrder_date(new Timestamp(System.currentTimeMillis()));
		ord1.setOrder_status(1);
		ord1.setOrder_total(1600);
		ord1.setOrder_item(1);
		ord1.setShipment_method(2);
		ord1.setOrd_store_711_name("敦維");
		dao.insert(ord1);
		System.out.println("---------------------");
		
		
		// 修改
		OrdVO ord2 = new OrdVO();
		ord2.setPayment_status(1);
		ord2.setShipment_status(2);
		ord2.setOrder_status(2);
		ord2.setShipment_id("SHP1314821207");
		ord2.setOrder_id("20180719_000004");
		
		
		dao.update(ord2);
		

		// PK查詢
		OrdVO ord3 = dao.findByPK("20180717-000002");
		System.out.print(ord3.getOrder_id() + ",");
		System.out.print(ord3.getBuyer_mem_id()+ ",");
		System.out.print(ord3.getSeller_mem_id() + ",");
		System.out.print(ord3.getOrder_address() + ",");
		System.out.print(ord3.getPayment_status() + ",");
		System.out.print(ord3.getPayment_method() + ",");
		System.out.print(ord3.getShipment_status() + ",");
		System.out.print(ord3.getOrder_date() + ",");
		System.out.print(ord3.getOrder_status() + ",");
		System.out.print(ord3.getOrder_total() + ",");
		System.out.print(ord3.getOrder_item() + ",");
		System.out.print(ord3.getCancel_reason() + ",");
		System.out.print(ord3.getStob_rating() + ",");
		System.out.print(ord3.getStob_rating_descr());
		System.out.print(ord3.getBtos_rating() + ",");
		System.out.print(ord3.getBtos_rating_descr());
		System.out.print(ord3.getShipment_id());
		System.out.print(ord3.getShipment_method());
		System.out.print(ord3.getOrd_store_711_name());
		System.out.println();
		System.out.println("---------------------");
		
		// 全查詢
		List<OrdVO> list = dao.getAll();
		for (OrdVO ord : list) {
			System.out.print(ord.getOrder_id() + ",");
			System.out.print(ord.getBuyer_mem_id()+ ",");
			System.out.print(ord.getSeller_mem_id() + ",");
			System.out.print(ord.getOrder_address() + ",");
			System.out.print(ord.getPayment_status() + ",");
			System.out.print(ord.getPayment_method() + ",");
			System.out.print(ord.getShipment_status() + ",");
			System.out.print(ord.getOrder_date() + ",");
			System.out.print(ord.getOrder_status() + ",");
			System.out.print(ord.getOrder_total() + ",");
			System.out.print(ord.getOrder_item() + ",");
			System.out.print(ord.getCancel_reason() + ",");
			System.out.print(ord.getStob_rating() + ",");
			System.out.print(ord.getStob_rating_descr());
			System.out.print(ord.getBtos_rating() + ",");
			System.out.print(ord.getBtos_rating_descr());
			System.out.print(ord.getShipment_id());
			System.out.print(ord.getShipment_method());
			System.out.print(ord.getOrd_store_711_name());
	
			System.out.println();
		}

		
		// Buyer查詢
		List<OrdVO> list2 = dao.getForAllBuy("M000003");
		for (OrdVO ord : list2) {
			System.out.print(ord.getOrder_id() + ",");
			System.out.print(ord.getBuyer_mem_id()+ ",");
			System.out.print(ord.getSeller_mem_id() + ",");
			System.out.print(ord.getOrder_address() + ",");
			System.out.print(ord.getPayment_status() + ",");
			System.out.print(ord.getPayment_method() + ",");
			System.out.print(ord.getShipment_status() + ",");
			System.out.print(ord.getOrder_date() + ",");
			System.out.print(ord.getOrder_status() + ",");
			System.out.print(ord.getOrder_total() + ",");
			System.out.print(ord.getOrder_item() + ",");
			System.out.print(ord.getCancel_reason() + ",");
			System.out.print(ord.getStob_rating() + ",");
			System.out.print(ord.getStob_rating_descr());
			System.out.print(ord.getBtos_rating() + ",");
			System.out.print(ord.getBtos_rating_descr());
			System.out.print(ord.getShipment_id());
			System.out.print(ord.getShipment_method());
			System.out.print(ord.getOrd_store_711_name());
	
			System.out.println();
			
		}
		
		System.out.println("---------------------");
		// Seller查詢
		List<OrdVO> list3 = dao.getForAllSell("M000003");
		for (OrdVO ord : list3) {
			System.out.print(ord.getOrder_id() + ",");
			System.out.print(ord.getBuyer_mem_id()+ ",");
			System.out.print(ord.getSeller_mem_id() + ",");
			System.out.print(ord.getOrder_address() + ",");
			System.out.print(ord.getPayment_status() + ",");
			System.out.print(ord.getPayment_method() + ",");
			System.out.print(ord.getShipment_status() + ",");
			System.out.print(ord.getOrder_date() + ",");
			System.out.print(ord.getOrder_status() + ",");
			System.out.print(ord.getOrder_total() + ",");
			System.out.print(ord.getOrder_item() + ",");
			System.out.print(ord.getCancel_reason() + ",");
			System.out.print(ord.getStob_rating() + ",");
			System.out.print(ord.getStob_rating_descr());
			System.out.print(ord.getBtos_rating() + ",");
			System.out.print(ord.getBtos_rating_descr());
			System.out.print(ord.getShipment_id());
			System.out.print(ord.getShipment_method());
			System.out.print(ord.getOrd_store_711_name());
	
			System.out.println();
		}
	}

}
