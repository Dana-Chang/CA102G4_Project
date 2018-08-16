package com.store711City.model;

import java.util.List;

import com.ord.model.OrdVO;

public interface Store711CityDAO_interface {
	public List<Store711CityVO> getAll();
	public Store711CityVO findByPK(Integer store_711_city_id);
	
}
