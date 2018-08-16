package com.store711Dist.model;

import java.util.List;

import com.store711City.model.Store711CityVO;

public interface Store711DistDAO_interface {
	public List<Store711DistVO> getAll();
	public Store711DistVO findByPK(Integer STORE_711_DIST_ID);

}
