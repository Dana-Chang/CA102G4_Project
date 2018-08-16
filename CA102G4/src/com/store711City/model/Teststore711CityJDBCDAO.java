package com.store711City.model;

import java.util.List;

import com.store711.model.Store711JDBCDAO;
import com.store711.model.Store711VO;

public class Teststore711CityJDBCDAO {

	public static void main(String[] args) {
		Store711CityJDBCDAO dao = new Store711CityJDBCDAO();
		
		
		List<Store711CityVO> list = dao.getAll();
		for (Store711CityVO store711City : list) {
			System.out.print(store711City.getStore_711_city_id() + ",");
			System.out.print(store711City.getStore_711_city_name());
			System.out.println();
		}
		
		System.out.println("---------------------");
		
		
		Store711CityVO store711City = dao.findByPK(10);
		System.out.print(store711City.getStore_711_city_id() + ",");
		System.out.print(store711City.getStore_711_city_name());
	
		System.out.println();
		System.out.println("---------------------");
		
	}

}
