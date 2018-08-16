package com.attractions.model;

import java.util.*;

public class AttractionsService {

	private AttractionsDAO_interface dao;

	public AttractionsService() {
		dao = new AttractionsDAO();
	}

	public AttractionsVO addAttractions(String att_name, Double att_lat, Double att_lon, String country,
			String administrative_area,String att_information, byte[] att_picture, String att_address, Integer att_status) {

		AttractionsVO attractionsVO = new AttractionsVO();

		attractionsVO.setAtt_name(att_name);
		attractionsVO.setAtt_lat(att_lat);
		attractionsVO.setAtt_lon(att_lon);
		attractionsVO.setCountry(country);
		attractionsVO.setAdministrative_area(administrative_area);
		attractionsVO.setAtt_information(att_information);
		attractionsVO.setAtt_picture(att_picture);
		attractionsVO.setAtt_address(att_address);
		attractionsVO.setAtt_status(att_status);
		dao.insert(attractionsVO);
		
		return attractionsVO;
	}
	
	public AttractionsVO updateAttractions(String att_no, String att_name, Double att_lat, Double att_lon, String country,
			String administrative_area,String att_information, byte[] att_picture, String att_address, Integer att_status) {
		
		AttractionsVO attractionsVO = new AttractionsVO();
		
		attractionsVO.setAtt_no(att_no);
		attractionsVO.setAtt_name(att_name);
		attractionsVO.setAtt_lat(att_lat);
		attractionsVO.setAtt_lon(att_lon);
		attractionsVO.setCountry(country);
		attractionsVO.setAdministrative_area(administrative_area);
		attractionsVO.setAtt_information(att_information);
		attractionsVO.setAtt_picture(att_picture);
		attractionsVO.setAtt_address(att_address);
		attractionsVO.setAtt_status(att_status);
		dao.update(attractionsVO);
		
		return attractionsVO;
	}
	
	public void deleteAttractions(String att_no) {
		dao.delete(att_no);
	}
	
	public AttractionsVO getOneAttByPK(String att_no) {
		return dao.findByPrimaryKey(att_no);
	}
	
	public List<AttractionsVO> getAll(){
		return dao.getAll();
	}
	
	public byte[] getPicture(String att_no) {
		return dao.getAttPicture(att_no);
	}
	
	public List<AttractionsVO> getAll(Map<String, String[]> map){
		return dao.getAll(map);
	}
	public List<AttractionsVO> getAllRandom() {
		return dao.getAllRandom();
	}
}
