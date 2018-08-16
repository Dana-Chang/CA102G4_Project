package com.admin.model;

import java.util.List;

public interface AdminDAO_interface {
	public void insert_Admin(AdminVO adminVO);
	public void update_Admin(AdminVO adminVO);
	public void delete_Admin(String admin_Id);
	public AdminVO login_Admin(String admin_Account,String admin_Password);
	public List<AdminVO> getAll_keyword(String admin_Name) ;
	
	public List<AdminVO> getAll() ;
	public AdminVO findByPrimaryKey(String admin_Id);

}
