package com.store711Dist.model;

import java.util.List;

import com.store711City.model.Store711CityJDBCDAO;
import com.store711City.model.Store711CityVO;

public class Teststore711DistJDBCDAO {

	public static void main(String[] args) {
		Store711DistJDBCDAO dao = new Store711DistJDBCDAO();
		
		
		List<Store711DistVO> list = dao.getAll();
		for (Store711DistVO store711Dist : list) {
			System.out.print(store711Dist.getStore_711_dist_id() + ",");
			System.out.print(store711Dist.getStore_711_dist_name()+ ",");
			System.out.print(store711Dist.getStore_711_city_id());
			System.out.println();
		}
		
		System.out.println("---------------------");
		
		
		Store711DistVO store711Dist = dao.findByPK(101);
		System.out.print(store711Dist.getStore_711_dist_id() + ",");
		System.out.print(store711Dist.getStore_711_dist_name()+ ",");
		System.out.print(store711Dist.getStore_711_city_id());
	
		System.out.println();
		System.out.println("---------------------");
		

	}

}
