package com.product.model;

import java.sql.Timestamp;
import java.util.Base64;

public class ProductVO {
	private Integer product_id;
	private Integer product_category_id;
	private String product_mem_id;
	private String product_name;
	private Integer product_price;
	private String product_descr;
	private Integer product_stock;
	private Integer product_status;
	private Timestamp product_date;	
	private byte[] product_photo_1;
	private byte[] product_photo_2;
	private byte[] product_photo_3;
	private byte[] product_photo_4;
	private byte[] product_photo_5;
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public Integer getProduct_category_id() {
		return product_category_id;
	}
	public void setProduct_category_id(Integer product_category_id) {
		this.product_category_id = product_category_id;
	}
	public String getProduct_mem_id() {
		return product_mem_id;
	}
	public void setProduct_mem_id(String product_mem_id) {
		this.product_mem_id = product_mem_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public Integer getProduct_price() {
		return product_price;
	}
	public void setProduct_price(Integer product_price) {
		this.product_price = product_price;
	}
	public Integer getProduct_stock() {
		return product_stock;
	}
	public void setProduct_stock(Integer product_stock) {
		this.product_stock = product_stock;
	}
	public String getProduct_descr() {
		return product_descr;
	}
	public void setProduct_descr(String product_descr) {
		this.product_descr = product_descr;
	}
	public Integer getProduct_status() {
		return product_status;
	}
	public void setProduct_status(Integer product_status) {
		this.product_status = product_status;
	}
	public Timestamp getProduct_date() {
		return product_date;
	}
	public void setProduct_date(Timestamp product_date) {
		this.product_date = product_date;
	}
	public byte[] getProduct_photo_1() {
		return product_photo_1;
	}
	
	public String getProduct_photo_1_base() {
		if(product_photo_1!=null) {
		return getBaseString(product_photo_1);}
		else {
			return null;
		}
	}
	
	public void setProduct_photo_1(byte[] product_photo_1) {
		this.product_photo_1 = product_photo_1;
	}
	public byte[] getProduct_photo_2() {
		return product_photo_2;
	}
	
	public String getProduct_photo_2_base() {
		if(product_photo_2!=null) {
			return getBaseString(product_photo_2);}
		else {
			return null;
		}
	}
	
	public void setProduct_photo_2(byte[] product_photo_2) {
		this.product_photo_2 = product_photo_2;
	}
	public byte[] getProduct_photo_3() {
		return product_photo_3;
	}
	public String getProduct_photo_3_base() {
		if(product_photo_3!=null) {
			return getBaseString(product_photo_3);}
		else {
			return null;
		}
	}
	public void setProduct_photo_3(byte[] product_photo_3) {
		this.product_photo_3 = product_photo_3;
	}
	public byte[] getProduct_photo_4() {
		return product_photo_4;
	}
	public String getProduct_photo_4_base() {
		if(product_photo_4!=null) {
			return getBaseString(product_photo_4);}
		else {
			return null;
		}
	}
	public void setProduct_photo_4(byte[] product_photo_4) {
		this.product_photo_4 = product_photo_4;
	}
	public byte[] getProduct_photo_5() {
		return  product_photo_5;
	}
	public String getProduct_photo_5_base() {
		if(product_photo_5!=null) {
			return getBaseString(product_photo_5);}
		else {
			return null;
		}
	}
	public void setProduct_photo_5(byte[]  product_photo_5 ){
		this.product_photo_5 =  product_photo_5;
	}
	
	public String getBaseString(byte[] b) {
		Base64.Encoder encoder = Base64.getEncoder();
		String encodedText = encoder.encodeToString(b);
		return encodedText;
	}
}
