package com.orderDetails.model;

import java.util.List;


public class OrderDetailsService {
	private OrderDetailsDAO_interface dao;

	public OrderDetailsService() {
		dao = new OrderDetailsDAO();
	}

	public OrderDetailsVO addOrderDetails(Integer details_product_id, Integer details_order_qty,Integer details_order_total) {

		OrderDetailsVO orderDetailsVO = new OrderDetailsVO();


		orderDetailsVO.setDetails_product_id(details_product_id);
		orderDetailsVO.setDetails_order_qty(details_order_qty);
		orderDetailsVO.setDetails_order_total(details_order_total);

		dao.insert(orderDetailsVO);

		return orderDetailsVO;
	}

	public OrderDetailsVO updateOrderDetails(Integer details_order_qty,Integer details_order_total,String details_order_id,Integer details_product_id) {

		OrderDetailsVO orderDetailsVO = new OrderDetailsVO();
		
		
		orderDetailsVO.setDetails_order_qty(details_order_qty);
		orderDetailsVO.setDetails_order_total(details_order_total);
		orderDetailsVO.setDetails_order_id(details_order_id);
		orderDetailsVO.setDetails_product_id(details_product_id);
		
		
		dao.update(orderDetailsVO);

		return orderDetailsVO;
	}

	public OrderDetailsVO getOneOrderDetails(String details_order_id, Integer details_product_id) {
		return dao.findByPK(details_order_id,details_product_id);
	}

	public List<OrderDetailsVO> getAll() {
		return dao.getAll();
	}
	
	public List<OrderDetailsVO> getOrderDetialsByOrdId(String details_order_id) {
		return dao.getOrderDetialsByOrdId(details_order_id);
	}
}
