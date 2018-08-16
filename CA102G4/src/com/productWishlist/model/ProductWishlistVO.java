package com.productWishlist.model;

import java.sql.Timestamp;

public class ProductWishlistVO {
	private Integer wishlist_product_id;
	private String wishlist_mem_id;
	private Timestamp product_wishlist_time;
	
	public Integer getWishlist_product_id() {
		return wishlist_product_id;
	}
	public void setWishlist_product_id(Integer wishlist_product_id) {
		this.wishlist_product_id = wishlist_product_id;
	}
	public Timestamp getProduct_wishlist_time() {
		return product_wishlist_time;
	}
	public void setProduct_wishlist_time(Timestamp product_wishlist_time) {
		this.product_wishlist_time = product_wishlist_time;
	}
	public String getWishlist_mem_id() {
		return wishlist_mem_id;
	}
	public void setWishlist_mem_id(String wishlist_mem_id) {
		this.wishlist_mem_id = wishlist_mem_id;
	}
	
}
