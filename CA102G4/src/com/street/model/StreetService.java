package com.street.model;

import java.util.List;

public class StreetService {
	private StreetDAO_interface dao;

	public StreetService() {
		dao = new StreetDAO();
	}

	public List<String> getAllCity() {
		return dao.listCity();
	}
	
	public List<String> getCountryByCity(String city) {
		return dao.findCountryByCity(city);
	}
	
	public List<String> getRoadByCountry(String country) {
		return dao.findRoadByCountry(country);
	}
}
