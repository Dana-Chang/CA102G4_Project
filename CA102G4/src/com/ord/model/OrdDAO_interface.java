package com.ord.model;

import java.util.List;

import com.orderDetails.model.OrderDetailsVO;

public interface OrdDAO_interface {
	 public void insert(OrdVO ordVO);
     public void update(OrdVO ordVO);
     public OrdVO findByPK(String order_id);
     public List<OrdVO> getAll();
     
     //查詢買家所有訂單
	 public List<OrdVO> getForAllBuy(String buyer_mem_id);
	 
	//查詢賣家所有訂單
	 public List<OrdVO> getForAllSell(String seller_mem_id);
	 
	//同時新增訂單與訂單明細 (用在訂單主檔與明細檔一次新增成功)
     public void insertWithOrderDetails(OrdVO ordVO , List<OrderDetailsVO> list);
     
   //查詢賣家平均評價
     public int getRatingBySellerId(String seller_mem_id);
   //查詢買家平均評價
     public int getRatingByBuyerId(String buyer_mem_id);
}
