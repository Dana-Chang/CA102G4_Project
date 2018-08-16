package com.productWishlist.model;

import java.util.List;
import java.util.Set;

public interface ProductWishlistDAO_interface {
	public void insert(ProductWishlistVO productWishlistVO);
    public void delete(Integer wishlist_product_id,String wishlist_mem_id);
	public List<ProductWishlistVO> getAll();
	public ProductWishlistVO findByPK(Integer wishlist_product_id,String wishlist_mem_id);
	  //查詢某商品的like數量(一對多)(回傳 Set)
    public Set<ProductWishlistVO> getLikesByProductid(Integer wishlist_product_id);
    
    //查詢某會員的like商品(一對多)(回傳 Set)
    public Set<ProductWishlistVO> getLikesByMemid(String wishlist_mem_id);
}
