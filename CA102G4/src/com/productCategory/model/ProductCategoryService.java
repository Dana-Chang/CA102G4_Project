package com.productCategory.model;

import java.util.List;

public class ProductCategoryService {
	private ProductCategoryDAO_interface dao;

	public ProductCategoryService() {
		dao = new ProductCategoryDAO();
	}

	public ProductCategoryVO addProductCategory(String product_category_name) {

		ProductCategoryVO productCategoryVO = new ProductCategoryVO();

		productCategoryVO.setProduct_category_name(product_category_name);

		dao.insert(productCategoryVO);

		return productCategoryVO;
	}

	public ProductCategoryVO updateProductCategory(Integer product_category_id, String product_category_name) {

		ProductCategoryVO productCategoryVO = new ProductCategoryVO();
		
		productCategoryVO.setProduct_category_id(product_category_id);
		productCategoryVO.setProduct_category_name(product_category_name);
		
		dao.update(productCategoryVO);

		return productCategoryVO;
	}

	public void deleteProductCategory(Integer product_category_id) {
		dao.delete(product_category_id);
	}

	public ProductCategoryVO getOneProductCategory(Integer product_category_id) {
		return dao.findByPK(product_category_id);
	}

	public List<ProductCategoryVO> getAll() {
		return dao.getAll();
	}
}
