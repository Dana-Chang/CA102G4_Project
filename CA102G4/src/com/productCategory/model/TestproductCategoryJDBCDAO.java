package com.productCategory.model;

import java.util.List;



public class TestproductCategoryJDBCDAO {

	public static void main(String[] args) {
		ProductCategoryJDBCDAO dao = new ProductCategoryJDBCDAO();
		
		// Insert
		ProductCategoryVO productCategory1 = new ProductCategoryVO();
		productCategory1.setProduct_category_name("行李箱");
		dao.insert(productCategory1);
		System.out.println("---------------------");
		
		// 刪除
		dao.delete(40);
		
		// 修改
		ProductCategoryVO productCategory2 = new ProductCategoryVO();
		productCategory2.setProduct_category_id(10);
		productCategory2.setProduct_category_name("CDE");

		dao.update(productCategory2);

		// 查詢
		ProductCategoryVO productCategory3 = dao.findByPK(10);
		System.out.print(productCategory3.getProduct_category_id() + ",");
		System.out.print(productCategory3.getProduct_category_name() + ",");
		System.out.println();
		System.out.println("---------------------");
		
		// 查詢
		List<ProductCategoryVO> list = dao.getAll();
		for (ProductCategoryVO productCategory : list) {
			System.out.print(productCategory.getProduct_category_id() + ",");
			System.out.print(productCategory.getProduct_category_name());
	
			System.out.println();
		}


	}

}
