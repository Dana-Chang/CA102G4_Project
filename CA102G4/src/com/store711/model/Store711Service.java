package com.store711.model;

import java.util.List;


public class Store711Service {
	private Store711DAO_interface dao;

	public Store711Service() {
		dao = new Store711DAO();
	}

	
	public Store711VO getOneStore711(Integer store_711_id) {
		return dao.findByPK(store_711_id);
	}

	public List<Store711VO> getAll() {
		return dao.getAll();
	}
}
