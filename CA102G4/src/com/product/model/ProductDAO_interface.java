package com.product.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductDAO_interface {
	public void insert(ProductVO productVO);
    public void update(ProductVO productVO);
    public void delete(Integer product_id);
	public List<ProductVO> getAll();
	public ProductVO findByPK(Integer product_id);
	  //查詢某商品的like數量(一對多)(回傳 Set)
    public Set<ProductVO> getProductsBySellerid(String product_mem_id);
    
    //萬用複合查詢(傳入參數型態Map)(回傳 List)
    public List<ProductVO> getAll(Map<String, String[]> map); 
}
