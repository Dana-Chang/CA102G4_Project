package com.mem.model;

import java.sql.Date;
import java.util.List;

public class MemberService {

	private MemberDAO_interface dao;

	public MemberService() {
		dao = new MemberDAO();
	}
	
	
	
	//註冊會員
	public MemberVO addMember
	(String mem_Account, String mem_Password,String mem_Name,Integer mem_State,Date mem_Reg_Date,String mem_Activecode,byte[] mem_Photo) {

		MemberVO memberVO = new MemberVO();
		memberVO.setMem_Name(mem_Name);
		memberVO.setMem_Account(mem_Account);
		memberVO.setMem_Password(mem_Password);
		memberVO.setMem_State(mem_State);
		memberVO.setMem_Reg_Date(mem_Reg_Date);
		memberVO.setMem_Activecode(mem_Activecode);
		memberVO.setMem_Photo(mem_Photo);

		dao.insert(memberVO);

		return memberVO;
	}

	//預留給 Struts 2 用的
	public void addMember(MemberVO memberVO) {
		dao.insert(memberVO);
	}
	
	
	//更改會員資料
	public MemberVO updateMember(String mem_Id,String mem_Name
			,String mem_Phone,Integer mem_Sex,Date mem_Birthday
			,byte[] mem_Photo
			,String mem_Profile) {
		

		MemberVO memberVO = new MemberVO();
		memberVO.setMem_Id(mem_Id);
		memberVO.setMem_Name(mem_Name);
		memberVO.setMem_Phone(mem_Phone);
		memberVO.setMem_Sex(mem_Sex);
		memberVO.setMem_Birthday(mem_Birthday);
		memberVO.setMem_Photo(mem_Photo);
		memberVO.setMem_Profile(mem_Profile);

		dao.update_Member(memberVO);

		return memberVO;
	}

	//預留給 Struts 2 用的
	public void updateMember(MemberVO MemberVO) {
		dao.update_Member(MemberVO);
	}
	
	
	//更改密碼用
	public MemberVO mem_Update_Password(String mem_Account, String mem_Password){
		
		MemberVO memberVO = new MemberVO();
		
		memberVO.setMem_Account(mem_Account);
		memberVO.setMem_Password(mem_Password);

		dao.Mem_Update_Password(memberVO);
		
		return memberVO;
	}
	
	//更改會員狀態
	public void update_State(String mem_Id,Integer mem_State) {
		dao.update_State(mem_Id, mem_State);
	}
	
	//管理員查詢會員用
	public List<MemberVO> getAll_member(String mem_Name){
		return dao.getAll_member(mem_Name);
	}
	
	//以下都是目前還沒用到
	public MemberVO findByPrimaryKey(String mem_Id){
			
//			MemberVO memberVO = new MemberVO();
//			
//			memberVO.setMem_Id(mem_Id);
	
			
			
			return dao.findByPrimaryKey(mem_Id);
		}

	public void deleteMember(String mem_Id) {
		dao.delete(mem_Id);
	}

	public MemberVO getOneMember(String mem_Id) {
		return dao.findByPrimaryKey(mem_Id);
	}

	public List<MemberVO> getAll() {
		return dao.getAll();
	}
	
	
	
	
//	public MemberVO Login_Member(String mem_Account, String mem_Password) {
//
//		MemberVO memberVO = new MemberVO();
//		memberVO.setMem_Account(mem_Account);
//		memberVO.setMem_Password(mem_Password);
//		return dao.login_Member(memberVO);
//	}
	
	

	// 更改711地址 第一個 mem_Update_Store memId,storeNo,storeName,storeAddr

	public MemberVO mem_Update_Store1(String mem_Id, Integer storeNo, String storeName, String storeAddr) {

		MemberVO memberVO = new MemberVO();

		memberVO.setMem_Id(mem_Id);
		memberVO.setSTORE_ADDR_1(storeAddr);
		memberVO.setSTORE_NAME_1(storeName);
		memberVO.setSTORE_NO_1(storeNo);

		dao.Mem_Update_Store1(memberVO);

		return memberVO;
	}

	// 更改711地址 第二個 mem_Update_Store memId,storeNo,storeName,storeAddr

	public MemberVO mem_Update_Store2(String mem_Id, Integer storeNo, String storeName, String storeAddr) {

		MemberVO memberVO = new MemberVO();

		memberVO.setMem_Id(mem_Id);
		memberVO.setSTORE_ADDR_2(storeAddr);
		memberVO.setSTORE_NAME_2(storeName);
		memberVO.setSTORE_NO_2(storeNo);

		dao.Mem_Update_Store2(memberVO);

		return memberVO;
	}

	// 更改711地址 第三個 mem_Update_Store memId,storeNo,storeName,storeAddr

	public MemberVO mem_Update_Store3(String mem_Id, Integer storeNo, String storeName, String storeAddr) {

		MemberVO memberVO = new MemberVO();

		memberVO.setMem_Id(mem_Id);
		memberVO.setSTORE_ADDR_3(storeAddr);
		memberVO.setSTORE_NAME_3(storeName);
		memberVO.setSTORE_NO_3(storeNo);

		dao.Mem_Update_Store3(memberVO);

		return memberVO;
	}

	// 刪除711地址 第一個 mem_Delete_Store1

	public void mem_Delete_Store1(String mem_Id) {
		dao.Mem_Delete_Store1(mem_Id);
	}

	// 刪除711地址 第二個 mem_Delete_Store2

	public void mem_Delete_Store2(String mem_Id) {
		dao.Mem_Delete_Store2(mem_Id);
	}

	// 刪除711地址 第三個 mem_Delete_Store3

	public void mem_Delete_Store3(String mem_Id) {
		dao.Mem_Delete_Store3(mem_Id);
	}

	// 更改宅配地址 第一個 mem_Update_Store memId,addr

	public MemberVO mem_Update_Home1(String mem_Id, String addr) {

		MemberVO memberVO = new MemberVO();

		memberVO.setMem_Id(mem_Id);
		memberVO.setDelivery_Address_1(addr);

		dao.Mem_Update_Home1(memberVO);

		return memberVO;
	}

	// 更改宅配地址 第二個 mem_Update_Store memId,addr

	public MemberVO mem_Update_Home2(String mem_Id, String addr) {

		MemberVO memberVO = new MemberVO();

		memberVO.setMem_Id(mem_Id);
		memberVO.setDelivery_Address_2(addr);

		dao.Mem_Update_Home2(memberVO);

		return memberVO;
	}

	// 更改宅配地址 第三個 mem_Update_Store memId,addr

	public MemberVO mem_Update_Home3(String mem_Id, String addr) {

		MemberVO memberVO = new MemberVO();

		memberVO.setMem_Id(mem_Id);
		memberVO.setDelivery_Address_3(addr);

		dao.Mem_Update_Home3(memberVO);

		return memberVO;
	}
	
	
	// 刪除宅配地址 第一個 mem_Delete_Home1
	public void mem_Delete_Home1(String mem_Id) {
		dao.Mem_Delete_Home1(mem_Id);
	}
	
	// 刪除宅配地址 第一個 mem_Delete_Home2
	public void mem_Delete_Home2(String mem_Id) {
		dao.Mem_Delete_Home2(mem_Id);
	}
	
	// 刪除宅配地址 第一個 mem_Delete_Home3
	public void mem_Delete_Home3(String mem_Id) {
		dao.Mem_Delete_Home3(mem_Id);
	}

	//世銘打的--全站搜尋用--根據會員姓名or會員介紹or旅遊記標題or旅遊記內容or旅遊記標籤類別or旅遊記標籤名稱or揪團目的地or揪團詳情or揪團標題...搜尋
	public List<MemberVO> SearchAll(String keyword) {
		return dao.SearchAll(keyword);
	}
}
