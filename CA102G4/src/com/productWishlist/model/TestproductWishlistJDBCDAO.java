package com.productWishlist.model;

import java.sql.Timestamp;
import java.util.List;

public class TestproductWishlistJDBCDAO {

	public static void main(String[] args) {
		ProductWishlistJDBCDAO dao = new ProductWishlistJDBCDAO();
		
		// Insert
		ProductWishlistVO productWishlist1 = new ProductWishlistVO();
		productWishlist1.setWishlist_product_id(1001);
		productWishlist1.setWishlist_mem_id("M000003");
		productWishlist1.setProduct_wishlist_time(new Timestamp(System.currentTimeMillis()));
		dao.insert(productWishlist1);
		System.out.println("---------------------");
		
		// �R��
		dao.delete(1001,"M000003");
		

		// �d��
		ProductWishlistVO productWishlist2 = dao.findByPK(1001,"M000001");
		System.out.print(productWishlist2.getWishlist_product_id() + ",");
		System.out.print(productWishlist2.getWishlist_mem_id() + ",");
		System.out.print(productWishlist2.getProduct_wishlist_time());
		System.out.println();
		System.out.println("---------------------");
		
		// �d��
		List<ProductWishlistVO> list = dao.getAll();
		for (ProductWishlistVO productWishlist : list) {
			System.out.print(productWishlist.getWishlist_product_id() + ",");
			System.out.print(productWishlist.getWishlist_mem_id() + ",");
			System.out.print(productWishlist.getProduct_wishlist_time());
			System.out.println();
		}

	}

}
