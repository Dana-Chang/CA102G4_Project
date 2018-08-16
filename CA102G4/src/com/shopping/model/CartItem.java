package com.shopping.model;

public class CartItem implements java.io.Serializable {

	private Integer product_id;
    private String product_name;
    private String product_mem_id;
    private Integer product_price;
    private Integer quantity;
    private Integer total_price;
    
    public CartItem() {
    	super();
    }
    public CartItem(Integer product_id, String product_name, String product_mem_id, Integer product_price, Integer quantity, Integer total_price) {
		super();
		this.product_id = product_id;
		this.product_name = product_name;
		this.product_mem_id = product_mem_id;
		this.product_price = product_price;
		this.quantity = quantity;
		this.total_price = total_price;
	}
     
    public Integer getProduct_id() {
        return product_id;
    }
    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }
    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    public Integer getProduct_price() {
        return product_price;
    }
    public void setProduct_price(Integer product_price) {
        this.product_price = product_price;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
    	this.quantity = quantity;
    }
 
	public String getProduct_mem_id() {
		return product_mem_id;
	}
	public void setProduct_mem_id(String product_mem_id) {
		this.product_mem_id = product_mem_id;
	}
	public Integer getTotal_price() {
		return total_price;
	}
	public void setTotal_price(Integer total_price) {
		this.total_price = total_price;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((product_id == null) ? 0 : product_id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartItem other = (CartItem) obj;
		if (product_id == null) {
			if (other.product_id != null)
				return false;
		} else if (!product_id.equals(other.product_id))
			return false;
		return true;
	}
	
}
