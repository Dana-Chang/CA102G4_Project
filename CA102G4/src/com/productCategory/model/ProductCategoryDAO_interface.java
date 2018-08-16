package com.productCategory.model;

import java.util.List;

public interface ProductCategoryDAO_interface {
	public void insert(ProductCategoryVO productCategoryVO);
    public void update(ProductCategoryVO productCategoryVO);
    public void delete(Integer PRODUCT_CATEGORY_ID);
	public List<ProductCategoryVO> getAll();
	public ProductCategoryVO findByPK(Integer PRODUCT_CATEGORY_ID);
	
}
