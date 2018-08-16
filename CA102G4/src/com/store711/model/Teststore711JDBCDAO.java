package com.store711.model;

import java.util.List;

public class Teststore711JDBCDAO {

	public static void main(String[] args) {
		Store711JDBCDAO dao = new Store711JDBCDAO();
		
		
		List<Store711VO> list = dao.getAll();
		for (Store711VO store711 : list) {
			System.out.print(store711.getStore_711_id() + ",");
			System.out.print(store711.getStore_711_dist_id() + ",");
			System.out.print(store711.getStore_711_name() + ",");
			System.out.print(store711.getStore_711_addr() + ",");
			System.out.print(store711.getStore_711_city_name() + ",");
			System.out.print(store711.getStore_711_dist_name() + ",");
			System.out.println();
		}
		
		System.out.println("---------------------");
		
		
		Store711VO store711 = dao.findByPK(110242);
		System.out.print(store711.getStore_711_id() + ",");
		System.out.print(store711.getStore_711_dist_id() + ",");
		System.out.print(store711.getStore_711_name() + ",");
		System.out.print(store711.getStore_711_addr() + ",");
		System.out.print(store711.getStore_711_city_name() + ",");
		System.out.print(store711.getStore_711_dist_name() + ",");
		System.out.println();
		System.out.println("---------------------");
		
		
		

	}

}
