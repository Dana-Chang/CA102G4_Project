package com.productWishlist.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;


public class ProductWishlistService {
	private ProductWishlistDAO_interface dao;

	public ProductWishlistService() {
		dao = new ProductWishlistDAO();
	}

	public ProductWishlistVO addProductWishlist(Integer wishlist_product_id,String wishlist_mem_id,Timestamp product_wishlist_time) {

		ProductWishlistVO productWishlistVO = new ProductWishlistVO();

		productWishlistVO.setWishlist_product_id(wishlist_product_id);
		productWishlistVO.setWishlist_mem_id(wishlist_mem_id);
		productWishlistVO.setProduct_wishlist_time(product_wishlist_time);

		
		dao.insert(productWishlistVO);

		return productWishlistVO;
	}

	public void deleteProductWishlist(Integer wishlist_product_id, String wishlist_mem_id) {
		dao.delete(wishlist_product_id,wishlist_mem_id);
	}

	public ProductWishlistVO getOneProductWishlist(Integer wishlist_product_id, String wishlist_mem_id) {
		return dao.findByPK(wishlist_product_id,wishlist_mem_id);
	}

	public List<ProductWishlistVO> getAll() {
		return dao.getAll();
	}
	
	public Set<ProductWishlistVO> getLikesByProductid(Integer wishlist_product_id){
		return dao.getLikesByProductid(wishlist_product_id);
	}
	
	public Set<ProductWishlistVO> getLikesByMemid(String wishlist_mem_id){
		return dao.getLikesByMemid(wishlist_mem_id);
	}

}
