package com.shopping.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.product.model.ProductService;
import com.product.model.ProductVO;

public class CartBean {
	private List<CartItem> allCartItems = new Vector<CartItem>();

	private Integer orderTotal;
	private Integer orderTotalItems;
	
	public int getLineItemCount() {
		return allCartItems.size();
	}

	// 刪除購物車的商品
	public void deleteCartItemById(Integer productId) {
			CartItem deleteObject = new CartItem();
			deleteObject.setProduct_id(productId);
			allCartItems.remove(deleteObject);
			calculateOrderTotal();
			calculateOrderTotalItems();
	}


	// 更新修改購物車的商品
	public void updateCartItemById(Integer productId, String quantity) {
		int total_price = 0;
		int product_price = 0;
		int iQuantity = 0;
		CartItem cartItem = new CartItem();
		try {
		   iQuantity = Integer.parseInt(quantity);
		   if (iQuantity > 0) {
				cartItem.setProduct_id(productId);
				int index = allCartItems.indexOf(cartItem);
				CartItem cartItemOld = allCartItems.get(index);
				product_price = cartItemOld.getProduct_price();
				total_price = product_price * iQuantity;
				cartItemOld.setQuantity(iQuantity);
				cartItemOld.setTotal_price(total_price);
				calculateOrderTotal();
				calculateOrderTotalItems();
			} 
		}catch (NumberFormatException nfe) {
			throw new RuntimeException("Error while deleting cart item:  " + nfe.getMessage());
		}
	}
	// 新增購物車的商品
	public void addCartItem(String product_id, String product_name, String product_price, String quantity,
			String product_mem_id) {
		int iProduct_id = 0;
		int total_price = 0;
		int iProduct_price = 0;
		int iQuantity = 0;
		CartItem cartItem = new CartItem();
		ProductService productSvc = new ProductService(); 
		try {
			iProduct_price = Integer.parseInt(product_price);
			iProduct_id = Integer.parseInt(product_id);
			iQuantity = Integer.parseInt(quantity);
			ProductVO productVO = productSvc.getOneProduct(iProduct_id);
			if (iQuantity > 0) {
				total_price = iProduct_price * iQuantity;
				cartItem.setProduct_id(iProduct_id);
				if(allCartItems.contains(cartItem)) {
					int index = allCartItems.indexOf(cartItem);
					CartItem cartItemOld = allCartItems.get(index);
					
					int qtyOld = cartItemOld.getQuantity();
					int totalOld= cartItemOld.getTotal_price();
					if(iQuantity+qtyOld > productVO.getProduct_stock()) {
						cartItemOld.setQuantity(productVO.getProduct_stock());
						cartItem.setTotal_price(productVO.getProduct_stock()*iProduct_price);
					}else {
						cartItemOld.setQuantity(iQuantity+qtyOld);
						cartItemOld.setTotal_price(total_price+totalOld);
					}
					calculateOrderTotal();
					calculateOrderTotalItems();
				}else {
					cartItem.setProduct_name(product_name);
					cartItem.setProduct_price(iProduct_price);
					cartItem.setQuantity(iQuantity);
					cartItem.setTotal_price(total_price);
					cartItem.setProduct_mem_id(product_mem_id);
					allCartItems.add(cartItem);
					calculateOrderTotal();
					calculateOrderTotalItems();
				}
			}

		} catch (NumberFormatException nfe) {
			throw new RuntimeException("Error while deleting cart item:  " + nfe.getMessage());
		}

	}

	public void addCartItem(CartItem cartItem) {
		allCartItems.add(cartItem);
	}

	public CartItem getCartItem(int itemIndex) {
		CartItem cartItem = null;
		if (allCartItems.size() > itemIndex) {
			cartItem = (CartItem) allCartItems.get(itemIndex - 1);
		}
		return cartItem;
	}

	public List<CartItem> getCartItems() {
		return allCartItems;
	}

	public void setCartItems(Vector<CartItem> allCartItems) {
		this.allCartItems = allCartItems;
	}

	public Integer getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Integer orderTotal) {
		this.orderTotal = orderTotal;
	}

	protected void calculateOrderTotal() {
		int orderTotal = 0;
		for (int counter = 0; counter < allCartItems.size(); counter++) {
			CartItem cartItem = (CartItem) allCartItems.get(counter);
			orderTotal += cartItem.getTotal_price();
		}
		setOrderTotal(orderTotal);
	}
	protected void calculateOrderTotalItems() {
		int orderTotalItems = 0;
		for (int counter = 0; counter < allCartItems.size(); counter++) {
			CartItem cartItem = (CartItem) allCartItems.get(counter);
			orderTotalItems += cartItem.getQuantity();
		}
		setOrderTotalItems(orderTotalItems);
	}
	
	public Integer getOrderTotalItems() {
		return orderTotalItems;
	}

	public void setOrderTotalItems(Integer orderTotalItems) {
		this.orderTotalItems = orderTotalItems;
	}

//	public static void main(String[] args) {
//		CartBean cartbean = new CartBean();
//		cartbean.addCartItem("2345", "AABBCC", "234567", "3", "mememe");
//		System.out.println(cartbean.getCartItems().size());
//		cartbean.deleteCartItemById(1234);
//		System.out.println(cartbean.getCartItems().size());
//		cartbean.deleteCartItemById(2345);
//		System.out.println(cartbean.getCartItems().size());
//	}

}
