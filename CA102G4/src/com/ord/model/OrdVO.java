package com.ord.model;

import java.sql.*;

public class OrdVO {
	private String order_id;
	private String buyer_mem_id;
	private String seller_mem_id;
	private String order_address;
	private Integer payment_status;
	private Integer payment_method;
	private Integer shipment_status;
	private Timestamp order_date;
	private Integer order_status;
	private Integer order_total;
	private Integer order_item;
	private Integer cancel_reason;
	private Integer stob_rating;
	private String stob_rating_descr;
	private Integer btos_rating;
	private String btos_rating_descr;
	private String shipment_id;
	private Integer shipment_method;
	private String ord_store_711_name;
	
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getBuyer_mem_id() {
		return buyer_mem_id;
	}
	public void setBuyer_mem_id(String buyer_mem_id) {
		this.buyer_mem_id = buyer_mem_id;
	}
	public String getSeller_mem_id() {
		return seller_mem_id;
	}
	public void setSeller_mem_id(String seller_mem_id) {
		this.seller_mem_id = seller_mem_id;
	}
	public String getOrder_address() {
		return order_address;
	}
	public void setOrder_address(String order_address) {
		this.order_address = order_address;
	}
	public Integer getPayment_status() {
		return payment_status;
	}
	public void setPayment_status(Integer payment_status) {
		this.payment_status = payment_status;
	}
	public Integer getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(Integer payment_method) {
		this.payment_method = payment_method;
	}
	public Integer getShipment_status() {
		return shipment_status;
	}
	public void setShipment_status(Integer shipment_status) {
		this.shipment_status = shipment_status;
	}
	public Timestamp getOrder_date() {
		return order_date;
	}
	public void setOrder_date(Timestamp order_date) {
		this.order_date = order_date;
	}
	public Integer getOrder_status() {
		return order_status;
	}
	public void setOrder_status(Integer order_status) {
		this.order_status = order_status;
	}
	public Integer getOrder_total() {
		return order_total;
	}
	public void setOrder_total(Integer order_total) {
		this.order_total = order_total;
	}
	public Integer getOrder_item() {
		return order_item;
	}
	public void setOrder_item(Integer order_item) {
		this.order_item = order_item;
	}
	public Integer getCancel_reason() {
		return cancel_reason;
	}
	public void setCancel_reason(Integer cancel_reason) {
		this.cancel_reason = cancel_reason;
	}
	public Integer getStob_rating() {
		return stob_rating;
	}
	public void setStob_rating(Integer stob_rating) {
		this.stob_rating = stob_rating;
	}
	public String getStob_rating_descr() {
		return stob_rating_descr;
	}
	public void setStob_rating_descr(String stob_rating_descr) {
		this.stob_rating_descr = stob_rating_descr;
	}
	public Integer getBtos_rating() {
		return btos_rating;
	}
	public void setBtos_rating(Integer btos_rating) {
		this.btos_rating = btos_rating;
	}
	public String getBtos_rating_descr() {
		return btos_rating_descr;
	}
	public void setBtos_rating_descr(String btos_rating_descr) {
		this.btos_rating_descr = btos_rating_descr;
	}
	public String getShipment_id() {
		return shipment_id;
	}
	public void setShipment_id(String shipment_id) {
		this.shipment_id = shipment_id;
	}
	public Integer getShipment_method() {
		return shipment_method;
	}
	public void setShipment_method(Integer shipment_method) {
		this.shipment_method = shipment_method;
	}
	public String getOrd_store_711_name() {
		return ord_store_711_name;
	}
	public void setOrd_store_711_name(String ord_store_711_name) {
		this.ord_store_711_name = ord_store_711_name;
	}
}
