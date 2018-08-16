package com.product.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ProductService {
	private ProductDAO_interface dao;

	public ProductService() {
		dao = new ProductDAO();
	}

	public ProductVO addProduct(Integer product_category_id,String product_mem_id,String product_name,Integer product_price,String product_descr,Integer product_stock,
			Integer product_status,Timestamp product_date,byte[] product_photo_1,byte[] product_photo_2,byte[] product_photo_3,byte[] product_photo_4,byte[] product_photo_5) {

		ProductVO productVO = new ProductVO();

	
		productVO.setProduct_category_id(product_category_id);
		productVO.setProduct_mem_id(product_mem_id);
		productVO.setProduct_name(product_name);
		productVO.setProduct_price(product_price);
		productVO.setProduct_descr(product_descr);
		productVO.setProduct_stock(product_stock);
		productVO.setProduct_status(product_status);
		productVO.setProduct_date(product_date);
		productVO.setProduct_photo_1(product_photo_1);
		productVO.setProduct_photo_2(product_photo_2);
		productVO.setProduct_photo_3(product_photo_3);
		productVO.setProduct_photo_4(product_photo_4);
		productVO.setProduct_photo_5(product_photo_5);

		dao.insert(productVO);

		return productVO;
	}

	public ProductVO updateProduct(Integer product_category_id,String product_mem_id,String product_name,Integer product_price,String product_descr,Integer product_stock,
			Integer product_status,Timestamp product_date,byte[] product_photo_1,byte[] product_photo_2,byte[] product_photo_3,byte[] product_photo_4,byte[] product_photo_5,Integer product_id) {

		ProductVO productVO = new ProductVO();

		
		productVO.setProduct_category_id(product_category_id);
		productVO.setProduct_mem_id(product_mem_id);
		productVO.setProduct_name(product_name);
		productVO.setProduct_price(product_price);
		productVO.setProduct_descr(product_descr);
		productVO.setProduct_stock(product_stock);
		productVO.setProduct_status(product_status);
		productVO.setProduct_date(product_date);
		productVO.setProduct_photo_1(product_photo_1);
		productVO.setProduct_photo_2(product_photo_2);
		productVO.setProduct_photo_3(product_photo_3);
		productVO.setProduct_photo_4(product_photo_4);
		productVO.setProduct_photo_5(product_photo_5);
		productVO.setProduct_id(product_id);
		
		dao.update(productVO);

		return productVO;
	}
	
	public void updateProduct(ProductVO productVO) {
		dao.update(productVO);
	}

	public void deleteProduct(Integer product_id) {
		dao.delete(product_id);
	}

	public ProductVO getOneProduct(Integer product_id) {
		return dao.findByPK(product_id);
	}

	public List<ProductVO> getAll() {
		return dao.getAll();
	}
	
	public Set<ProductVO> getSellerProducts(String product_mem_id) {
		return dao.getProductsBySellerid(product_mem_id);
	}
	
	public List<ProductVO> getAll(Map<String, String[]> map) {
		return dao.getAll(map);
	}
}
