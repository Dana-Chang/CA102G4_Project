package com.admin.model;

import java.util.List;

public class AdminService {
	
	private AdminDAO_interface dao;

	public AdminService() {
		dao = new AdminDAO();
	}

	public AdminVO insert_Admin(String admin_Account, String admin_Password,String admin_Name,String admin_Mail,String admin_Phone) {

		AdminVO adminVO = new AdminVO();
		adminVO.setAdmin_Account(admin_Account);
		adminVO.setAdmin_Password(admin_Password);
		adminVO.setAdmin_Name(admin_Name);
		adminVO.setAdmin_Mail(admin_Mail);
		adminVO.setAdmin_Phone(admin_Phone);

		dao.insert_Admin(adminVO);

		return adminVO;
	}
	
	public AdminVO upadte_Admin(String admin_Id , String admin_Password, String admin_Name, String admin_Mail,String admin_Phone) {

		AdminVO adminVO = new AdminVO();
		adminVO.setAdmin_Id(admin_Id);
		adminVO.setAdmin_Password(admin_Password);
		adminVO.setAdmin_Name(admin_Name);
		adminVO.setAdmin_Mail(admin_Mail);
		adminVO.setAdmin_Phone(admin_Phone);

		dao.update_Admin(adminVO);

		return adminVO;
	}
	
	public List<AdminVO> getAll_keyword(String admin_Name){
		return dao.getAll_keyword(admin_Name);
	}
	
	
	public List<AdminVO> getAll(){
		return dao.getAll();
	}
	
	

}
