package com.orderDetails.model;

public class OrderDetailsVO {
	private String details_order_id;
	private Integer details_product_id;
	private Integer details_order_qty;
	private Integer details_order_total;
	
	
	public String getDetails_order_id() {
		return details_order_id;
	}
	public void setDetails_order_id(String details_order_id) {
		this.details_order_id = details_order_id;
	}
	public Integer getDetails_product_id() {
		return details_product_id;
	}
	public void setDetails_product_id(Integer details_product_id) {
		this.details_product_id = details_product_id;
	}
	public Integer getDetails_order_qty() {
		return details_order_qty;
	}
	public void setDetails_order_qty(Integer details_order_qty) {
		this.details_order_qty = details_order_qty;
	}
	public Integer getDetails_order_total() {
		return details_order_total;
	}
	public void setDetails_order_total(Integer details_order_total) {
		this.details_order_total = details_order_total;
	}

}
