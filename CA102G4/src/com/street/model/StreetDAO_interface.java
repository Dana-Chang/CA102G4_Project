package com.street.model;

import java.util.List;

public interface StreetDAO_interface {
	
	public List<String> listCity();
	public List<String> findCountryByCity(String city);
	public List<String> findRoadByCountry(String country);
	
}
