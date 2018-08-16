package com.store711.model;

import java.util.List;


public interface Store711DAO_interface {
	public List<Store711VO> getAll();
	public Store711VO findByPK(Integer store_711_id);
	
}
