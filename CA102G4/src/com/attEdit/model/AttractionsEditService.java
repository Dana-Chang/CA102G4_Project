package com.attEdit.model;

import java.sql.*;
import java.util.*;

public class AttractionsEditService {
	
	private AttractionsEditDAO_interface dao;
	
	public AttractionsEditService(){
		dao = new AttractionsEditDAO();
	}
	
	public AttractionsEditVO addAttEdit(String mem_id, String att_no, String att_name,
			Double att_lat, Double att_lon, String country,String administrative_area,String att_information, byte[] att_picture,
			String att_address) {
		
		AttractionsEditVO attEditVO = new AttractionsEditVO();
		attEditVO.setMem_id(mem_id);
		attEditVO.setAtt_no(att_no);
		attEditVO.setAtt_name(att_name);
		attEditVO.setAtt_lat(att_lat);
		attEditVO.setAtt_lon(att_lon);
		attEditVO.setCountry(country);
		attEditVO.setAdministrative_area(administrative_area);
		attEditVO.setAtt_information(att_information);
		attEditVO.setAtt_picture(att_picture);
		attEditVO.setAtt_address(att_address);
		
		dao.insert(attEditVO);
		return attEditVO;
	}
	
	public AttractionsEditVO getOneAttEditByPK(String attEdit_no) {
		return dao.findByPrimaryKey(attEdit_no);
	}
	
	public List<AttractionsEditVO> getAll(){
		return dao.getAll();
	}
	
	public List<AttractionsEditVO> getAllOrderByDate(){
		return dao.getAllOrderByDate();
	}
	
	public int att_update(AttractionsEditVO attEditVO) {
		return dao.att_update(attEditVO);
	}
	
	public int delete(String attEdit_no) {
		return dao.delete(attEdit_no);
	}
}
