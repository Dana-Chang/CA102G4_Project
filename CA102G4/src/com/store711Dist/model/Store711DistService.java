package com.store711Dist.model;

import java.util.List;


public class Store711DistService {
	private Store711DistDAO_interface dao;

	public Store711DistService() {
		dao = new Store711DistDAO();
	}

	
	public Store711DistVO getOneStore711Dist(Integer store_711_dist_id) {
		return dao.findByPK(store_711_dist_id);
	}

	public List<Store711DistVO> getAll() {
		return dao.getAll();
	}
}
