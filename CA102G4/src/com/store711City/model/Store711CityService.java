package com.store711City.model;

import java.util.List;

public class Store711CityService {
	private Store711CityDAO_interface dao;

	public Store711CityService() {
		dao = new Store711CityDAO();
	}

	
	public Store711CityVO getOneStore711City(Integer store_711_city_id) {
		return dao.findByPK(store_711_city_id);
	}

	public List<Store711CityVO> getAll() {
		return dao.getAll();
	}
}
