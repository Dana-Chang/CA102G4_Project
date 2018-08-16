package com.trip.model;

import java.io.*;
import java.util.*;

public class TripDAO_Test {
	public static void main(String[] args) {
		TripDAO_interface dao = new TripJDBCDAO();
		
		TripVO vo1 = new TripVO();
		vo1.setMem_id("M000001");
		vo1.setTrip_name("test");
		vo1.setTrip_days(3);
		vo1.setTrip_views(1);
		vo1.setTrip_status(2);
		dao.insert(vo1);
		
		List<TripVO> list = dao.getAll();
		for(TripVO eTripVO : list) {
			System.out.println(eTripVO.toString());
		}
		
	}
}
