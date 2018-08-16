package com.orderDetails.model;

import java.util.List;
import java.util.Set;

public interface OrderDetailsDAO_interface {
	public void insert(OrderDetailsVO orderDetailsVO);
    public void update(OrderDetailsVO orderDetailsVO);
	public List<OrderDetailsVO> getAll();
	public OrderDetailsVO findByPK(String details_order_id,Integer details_product_id);
	
	//同時新增訂單與訂單明細 (用在訂單主檔與明細檔一次新增成功)
    public void insert2 (OrderDetailsVO orderDetailsVO, java.sql.Connection con);
    
    //查詢某訂單的所有訂單明細(一對多)(回傳 Set)
    public List<OrderDetailsVO> getOrderDetialsByOrdId(String details_order_id);
}
