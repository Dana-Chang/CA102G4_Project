package com.photo_wall_like.model;



public class photo_wall_likeService {
	
	private photo_wall_likeDAO_interface dao;
	
	public photo_wall_likeService() {
		dao = new photo_wall_likeDAO();	
	}
	
	public photo_wall_likeVO addphoto_wall_like(String mem_Id, String photo_No) {
		photo_wall_likeVO photo_wall_likeVO = new photo_wall_likeVO();

		photo_wall_likeVO.setMem_Id(mem_Id);
		photo_wall_likeVO.setPhoto_No(photo_No);
		dao.insert(photo_wall_likeVO);

		return photo_wall_likeVO;
	}
	
	public void deletephoto_wall_like(String mem_Id, String photo_No) {
		photo_wall_likeVO photo_wall_likeVO = new photo_wall_likeVO();

		photo_wall_likeVO.setMem_Id(mem_Id);
		photo_wall_likeVO.setPhoto_No(photo_No);
		dao.delete(photo_wall_likeVO);
	}
	
	
	public int findByPrimaryKey(String mem_Id, String photo_No) {
		photo_wall_likeVO photo_wall_likeVO = new photo_wall_likeVO();
		photo_wall_likeVO.setMem_Id(mem_Id);
		photo_wall_likeVO.setPhoto_No(photo_No);
		int cnt = dao.findByPrimaryKey(photo_wall_likeVO);
		return cnt;
	}
	
	
}
