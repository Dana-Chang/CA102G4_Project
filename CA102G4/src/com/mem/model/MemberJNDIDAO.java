//package com.mem.model;
//
//import java.util.*;
//import java.sql.*;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.sql.DataSource;
//
//
//
//public class MemberJNDIDAO implements MemberDAO_interface {
//	
//	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
//	private static DataSource ds = null;
//	static {
//		try {
//			Context ctx = new InitialContext();
//			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//	}
//
//
////private static final String REGIGST_STMT = 
////	"Insert into MEMBER (MEM_ID,MEM_ACCOUNT,MEM_PASSWORD,MEM_NAME) "
////	+ "VALUES ('M'||LPAD(to_char(MEMBER_seq.NEXTVAL), 6, '0'),?,?,?)";
//
//private static final String INSERT_STMT = 
//"Insert into MEMBER (MEM_ID,MEM_ACCOUNT,MEM_PASSWORD,MEM_NAME,MEM_STATE) "
//+ "VALUES ('M'||LPAD(to_char(MEMBER_seq.NEXTVAL), 6, '0'),?,?,?,?)";
//
//private static final String UPDATE = 
//"UPDATE MEMBER SET MEM_ACCOUNT=?,MEM_PASSWORD= ?, MEM_NAME= ? ,MEM_SEX= ?,MEM_ADDRESS= ?,MEM_BIRTHDAY= ?,MEM_PHONE= ?,MEM_PROFILE= ?,MEM_PHOTO= ?,MEM_STATE= ?,DELIVERY_ADDRESS_1= ?,DELIVERY_ADDRESS_2=?,DELIVERY_ADDRESS_3=?,STORE_ADDR_1=?,STORE_ADDR_2=?,STORE_ADDR_3=?,STORE_NAME_1=?,STORE_NAME_2=?,STORE_NAME_3=?,STORE_NO_1=?,STORE_NO_2=?,STORE_NO_3=? WHERE MEM_ID = ?";
//
//private static final String UPDATE_MEMBER =
//"UPDATE MEMBER SET MEM_NAME= ? , MEM_PHONE= ? ,MEM_SEX= ?,MEM_BIRTHDAY= ?,MEM_PHOTO= ?,MEM_PROFILE= ? WHERE MEM_ID = ?";
//
//private static final String GET_ALL_STMT = 
//	"SELECT MEM_ID,MEM_ACCOUNT,MEM_PASSWORD,MEM_NAME,MEM_SEX,MEM_ADDRESS,to_char(MEM_BIRTHDAY,'yyyy-mm-dd') MEM_BIRTHDAY,MEM_PHONE,MEM_PROFILE,MEM_PHOTO,MEM_STATE,DELIVERY_ADDRESS_1,DELIVERY_ADDRESS_2,DELIVERY_ADDRESS_3,STORE_ADDR_1,STORE_ADDR_2,STORE_ADDR_3,STORE_NAME_1,STORE_NAME_2,STORE_NAME_3,STORE_NO_1,STORE_NO_2,STORE_NO_3 FROM MEMBER ORDER BY MEM_ID";
//private static final String GET_ONE_STMT = 
//	"SELECT MEM_ID,MEM_ACCOUNT,MEM_PASSWORD,MEM_NAME,MEM_SEX,MEM_ADDRESS,to_char(MEM_BIRTHDAY,'yyyy-mm-dd') MEM_BIRTHDAY,MEM_PHONE,MEM_PROFILE,MEM_PHOTO,MEM_STATE,DELIVERY_ADDRESS_1,DELIVERY_ADDRESS_2,DELIVERY_ADDRESS_3,STORE_ADDR_1,STORE_ADDR_2,STORE_ADDR_3,STORE_NAME_1,STORE_NAME_2,STORE_NAME_3,STORE_NO_1,STORE_NO_2,STORE_NO_3 FROM MEMBER WHERE MEM_ID = ?";
//private static final String DELETE_MEMBER = 
//	"DELETE FROM MEMBER WHERE MEM_ID = ?";
//
//private static final String login_Member = "SELECT * FROM MEMBER WHERE MEM_ACCOUNT= ? AND MEM_PASSWORD = ?"; 
//
//
//
//
////private static final String Login_Member = "SELECT * FROM MEMBER WHERE MEM_ACCOUNT= ? AND MEM_PASSWORD = ?";
//
//@Override
//public void insert(MemberVO memberVO) {
//	
//	Connection con = null;
//	PreparedStatement pstmt = null;
//
//	try {
//		
//		con = ds.getConnection();
//		pstmt = con.prepareStatement(INSERT_STMT);
//		
//		pstmt.setString(1, memberVO.getMem_Account());
//		pstmt.setString(2, memberVO.getMem_Password());
//		pstmt.setString(3, memberVO.getMem_Name());
//		pstmt.setInt(4,3);
//		pstmt.executeUpdate();
//		
//		
//		// Handle any SQL errors
//	} catch (SQLException se) {
//		throw new RuntimeException("A database error occured. "
//				+ se.getMessage());
//		// Clean up JDBC resources
//	} finally {
//		if (pstmt != null) {
//			try {
//				pstmt.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (con != null) {
//			try {
//				con.close();
//			} catch (Exception e) {
//				e.printStackTrace(System.err);
//			}
//		}
//	}
//}
//
//@Override
//public void update(MemberVO memberVO) {
//	
//	Connection con = null;
//	PreparedStatement pstmt = null;
//
//	try {
//		con = ds.getConnection();
//		pstmt = con.prepareStatement(UPDATE);
//
//		pstmt.setString(1, memberVO.getMem_Account());
//		pstmt.setString(2, memberVO.getMem_Password());
//		pstmt.setString(3, memberVO.getMem_Name());
//		pstmt.setInt(4, memberVO.getMem_Sex());
//		pstmt.setString(5, memberVO.getMem_Address());
//		pstmt.setDate(6, memberVO.getMem_Birthday());
//		pstmt.setString(7, memberVO.getMem_Phone());
//		pstmt.setString(8, memberVO.getMem_Profile());
//		pstmt.setBytes(9, memberVO.getMem_Photo());
//		pstmt.setInt(10, memberVO.getMem_State());
//		pstmt.setString(11, memberVO.getDelivery_Address_1());
//		pstmt.setString(12, memberVO.getDelivery_Address_2());
//		pstmt.setString(13, memberVO.getDelivery_Address_3());
//		pstmt.setString(14, memberVO.getSTORE_ADDR_1());
//		pstmt.setString(15, memberVO.getSTORE_ADDR_2());
//		pstmt.setString(16, memberVO.getSTORE_ADDR_3());
//		pstmt.setString(17, memberVO.getSTORE_NAME_1());
//		pstmt.setString(18, memberVO.getSTORE_NAME_2());
//		pstmt.setString(19, memberVO.getSTORE_NAME_3());
//		pstmt.setInt(20, memberVO.getSTORE_NO_1());
//		pstmt.setInt(21, memberVO.getSTORE_NO_2());
//		pstmt.setInt(22, memberVO.getSTORE_NO_3());
//		pstmt.setString(23, memberVO.getMem_Id());
//		pstmt.executeUpdate();
//
//	} catch (SQLException se) {
//		throw new RuntimeException("A database error occured. " + se.getMessage());
//		// Clean up JDBC resources
//	} finally {
//		if (pstmt != null) {
//			try {
//				pstmt.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (con != null) {
//			try {
//				con.close();
//			} catch (Exception e) {
//				e.printStackTrace(System.err);
//			}
//		}
//	}
//}
//	
//
//
//
//@Override
//public void delete(String mem_Id) {
//	
//	Connection con = null;
//	PreparedStatement pstmt = null;
//	
//	try {
//		
//		con = ds.getConnection();
//		
//		con.setAutoCommit(false);
//		
//		pstmt = con.prepareStatement(DELETE_MEMBER);
//
//		pstmt.setString(1, mem_Id);
//
//		pstmt.executeUpdate();
//
//		con.commit();
//		con.setAutoCommit(true);
//		
//		// Handle any SQL errors
//
//	} catch (SQLException se) {
//		if (con != null) {
//			try {
//				con.rollback();
//			} catch (SQLException excep) {
//				throw new RuntimeException("rollback error occured. " + excep.getMessage());
//			}
//		}
//		throw new RuntimeException("A database error occured. " + se.getMessage());
//		// Clean up JDBC resources
//	} finally {
//		if (pstmt != null) {
//			try {
//				pstmt.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (con != null) {
//			try {
//				con.close();
//			} catch (Exception e) {
//				e.printStackTrace(System.err);
//			}
//		}
//	}
//}
//
//
//@Override
//public MemberVO findByPrimaryKey(String mem_Id) {
//	
//	MemberVO memberVO = null;
//	Connection con = null;
//	PreparedStatement pstmt = null;
//	ResultSet rs = null;
//	
//	try {
//		
//		con = ds.getConnection();
//		pstmt = con.prepareStatement(GET_ONE_STMT);
//
//		pstmt.setString(1, mem_Id);
//
//		rs = pstmt.executeQuery();
//
//		while (rs.next()) {
//			memberVO = new MemberVO();
//			memberVO.setMem_Id(rs.getString("MEM_ID"));
//			memberVO.setMem_Account(rs.getString("MEM_ACCOUNT"));
//			memberVO.setMem_Password(rs.getString("MEM_PASSWORD"));
//			memberVO.setMem_Name(rs.getString("MEM_NAME"));
//			memberVO.setMem_Sex(rs.getInt("MEM_SEX"));
//			memberVO.setMem_Address(rs.getString("MEM_ADDRESS"));
//			memberVO.setMem_Birthday(rs.getDate("MEM_BIRTHDAY"));
//			memberVO.setMem_Phone(rs.getString("MEM_PHONE"));
//			memberVO.setMem_Profile(rs.getString("MEM_PROFILE"));
//			memberVO.setMem_Photo(rs.getBytes("MEM_PHOTO"));
//			memberVO.setMem_State(rs.getInt("MEM_STATE"));
//			
//			memberVO.setDelivery_Address_1(rs.getString("DELIVERY_ADDRESS_1"));
//			memberVO.setDelivery_Address_2(rs.getString("DELIVERY_ADDRESS_2"));
//			memberVO.setDelivery_Address_3(rs.getString("DELIVERY_ADDRESS_3"));
//			
//			memberVO.setSTORE_ADDR_1(rs.getString("STORE_ADDR_1"));
//			memberVO.setSTORE_ADDR_2(rs.getString("STORE_ADDR_2"));
//			memberVO.setSTORE_ADDR_3(rs.getString("STORE_ADDR_3"));
//			
//			memberVO.setSTORE_NAME_1(rs.getString("STORE_NAME_1"));
//			memberVO.setSTORE_NAME_2(rs.getString("STORE_NAME_2"));
//			memberVO.setSTORE_NAME_3(rs.getString("STORE_NAME_3"));
//
//			memberVO.setSTORE_NO_1(rs.getInt("STORE_NO_1"));
//			memberVO.setSTORE_NO_2(rs.getInt("STORE_NO_2"));
//			memberVO.setSTORE_NO_3(rs.getInt("STORE_NO_3"));
//			pstmt.executeUpdate();
//
//
//		}
//		
//		// Handle any SQL errors
//	} catch (SQLException se) {
//		throw new RuntimeException("A database error occured. " + se.getMessage());
//		// Clean up JDBC resources
//	} finally {
//		if (rs != null) {
//			try {
//				rs.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (pstmt != null) {
//			try {
//				pstmt.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (con != null) {
//			try {
//				con.close();
//			} catch (Exception e) {
//				e.printStackTrace(System.err);
//			}
//		}
//	}
//	return memberVO;
//}
//@Override
//public List<MemberVO> getAll() {
//	List<MemberVO> list = new ArrayList<MemberVO>();
//	MemberVO memberVO = null;
//	Connection con = null;
//	PreparedStatement pstmt = null;
//	ResultSet rs = null;
//
//	try {
//		con = ds.getConnection();
//		pstmt = con.prepareStatement(GET_ALL_STMT);
//		rs = pstmt.executeQuery();
//
//		while (rs.next()) {
//			memberVO = new MemberVO();
//			memberVO.setMem_Id(rs.getString("MEM_ID"));
//			memberVO.setMem_Account(rs.getString("MEM_ACCOUNT"));
//			memberVO.setMem_Password(rs.getString("MEM_PASSWORD"));
//			memberVO.setMem_Name(rs.getString("MEM_NAME"));
//			memberVO.setMem_Sex(rs.getInt("MEM_SEX"));
//			memberVO.setMem_Address(rs.getString("MEM_ADDRESS"));
//			memberVO.setMem_Birthday(rs.getDate("MEM_BIRTHDAY"));
//			memberVO.setMem_Phone(rs.getString("MEM_PHONE"));
//			memberVO.setMem_Profile(rs.getString("MEM_PROFILE"));
//			memberVO.setMem_Photo(rs.getBytes("MEM_PHOTO"));
//			memberVO.setMem_State(rs.getInt("MEM_STATE"));
//			
//			memberVO.setDelivery_Address_1(rs.getString("DELIVERY_ADDRESS_1"));
//			memberVO.setDelivery_Address_2(rs.getString("DELIVERY_ADDRESS_2"));
//			memberVO.setDelivery_Address_3(rs.getString("DELIVERY_ADDRESS_3"));
//			
//			memberVO.setSTORE_ADDR_1(rs.getString("STORE_ADDR_1"));
//			memberVO.setSTORE_ADDR_2(rs.getString("STORE_ADDR_2"));
//			memberVO.setSTORE_ADDR_3(rs.getString("STORE_ADDR_3"));
//			
//			memberVO.setSTORE_NAME_1(rs.getString("STORE_NAME_1"));
//			memberVO.setSTORE_NAME_2(rs.getString("STORE_NAME_2"));
//			memberVO.setSTORE_NAME_3(rs.getString("STORE_NAME_3"));
//
//			memberVO.setSTORE_NO_1(rs.getInt("STORE_NO_1"));
//			memberVO.setSTORE_NO_2(rs.getInt("STORE_NO_2"));
//			memberVO.setSTORE_NO_3(rs.getInt("STORE_NO_3"));
//			list.add(memberVO); // Store the row in the list
//		}
//
//		// Handle any SQL errors
//	} catch (SQLException se) {
//		throw new RuntimeException("A database error occured. " + se.getMessage());
//		// Clean up JDBC resources
//	} finally {
//		if (rs != null) {
//			try {
//				rs.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (pstmt != null) {
//			try {
//				pstmt.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (con != null) {
//			try {
//				con.close();
//			} catch (Exception e) {
//				e.printStackTrace(System.err);
//			}
//		}
//	}
//	return list;
//}
//
//@Override
//public MemberVO login_Member(String mem_Account, String mem_Password) {
//	MemberVO memberVO_login = null;
//	Connection con = null;
//	PreparedStatement pstmt = null;
//	ResultSet rs = null;
//
//	try {
//		
//		con = ds.getConnection();
//		pstmt = con.prepareStatement(login_Member);
//		pstmt.setString(1, mem_Account);
//		pstmt.setString(2, mem_Password);
//
//		rs = pstmt.executeQuery();
//
//		while (rs.next()) {
//			memberVO_login = new MemberVO();
//			memberVO_login.setMem_Id(rs.getString("MEM_ID"));
//			memberVO_login.setMem_Account(rs.getString("MEM_ACCOUNT"));
//			memberVO_login.setMem_Password(rs.getString("MEM_PASSWORD"));
//			memberVO_login.setMem_Name(rs.getString("MEM_NAME"));
//			memberVO_login.setMem_Sex(rs.getInt("MEM_SEX"));
//			memberVO_login.setMem_Address(rs.getString("MEM_ADDRESS"));
//			memberVO_login.setMem_Birthday(rs.getDate("MEM_BIRTHDAY"));
//			memberVO_login.setMem_Phone(rs.getString("MEM_PHONE"));
//			memberVO_login.setMem_Profile(rs.getString("MEM_PROFILE"));
//			memberVO_login.setMem_Photo(rs.getBytes("MEM_PHOTO"));
//			memberVO_login.setMem_State(rs.getInt("MEM_STATE"));
//
//			pstmt.executeUpdate();
//
//		}
//		
//		// Handle any driver errors
//	} 
//	catch (SQLException se) {
//		throw new RuntimeException("A database error occured. " + se.getMessage());
//		// Clean up JDBC resources
//	} 
//	finally {
//		if (rs != null) {
//			try {
//				rs.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (pstmt != null) {
//			try {
//				pstmt.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (con != null) {
//			try {
//				con.close();
//			} catch (Exception e) {
//				e.printStackTrace(System.err);
//			}
//		}
//	}
//	return memberVO_login;
//	}
//
//@Override
//public void update_Member(MemberVO memberVO) {
//	Connection con = null;
//	PreparedStatement pstmt = null;
//
//	try {
//		con = ds.getConnection();
//		pstmt = con.prepareStatement(UPDATE_MEMBER);
//
//		pstmt.setString(1, memberVO.getMem_Name());
//		pstmt.setString(2, memberVO.getMem_Phone());
//		pstmt.setInt(3, memberVO.getMem_Sex());
//		pstmt.setDate(4, memberVO.getMem_Birthday());
//		pstmt.setBytes(5, memberVO.getMem_Photo());
//		pstmt.setString(6, memberVO.getMem_Profile());
//		pstmt.setString(7, memberVO.getMem_Id());
//	
//		pstmt.executeUpdate();
//
//	} catch (SQLException se) {
//		throw new RuntimeException("A database error occured. " + se.getMessage());
//		// Clean up JDBC resources
//	} finally {
//		if (pstmt != null) {
//			try {
//				pstmt.close();
//			} catch (SQLException se) {
//				se.printStackTrace(System.err);
//			}
//		}
//		if (con != null) {
//			try {
//				con.close();
//			} catch (Exception e) {
//				e.printStackTrace(System.err);
//			}
//		}
//	}
//}
//
////@Override
////public MemberVO login_Member(MemberVO memberVO) {
////	Connection con = null;
////	PreparedStatement pstmt = null;
////	ResultSet rs = null;
////	MemberVO memberVO_login = null;
////
////	try {
////		con = ds.getConnection();
////		pstmt = con.prepareStatement(Login_Member);
////		pstmt.setString(1, memberVO.getMem_Account());
////		pstmt.setString(2, memberVO.getMem_Password());
////
////		rs = pstmt.executeQuery();
////
////		while (rs.next()) {
////			memberVO_login = new MemberVO();
////			memberVO_login.setMem_Account(rs.getString("setMem_Account"));
////			memberVO_login.setMem_Password(rs.getString("setMem_Password"));
////
////		}
////		// Handle any SQL errors
////	} catch (SQLException se) {
////		throw new RuntimeException("A database error occured. " + se.getMessage());
////		// Clean up JDBC resources
////	} finally {
////		if (rs != null) {
////			try {
////				rs.close();
////			} catch (SQLException se) {
////				se.printStackTrace(System.err);
////			}
////		}
////		if (pstmt != null) {
////			try {
////				pstmt.close();
////			} catch (SQLException se) {
////				se.printStackTrace(System.err);
////			}
////		}
////		if (con != null) {
////			try {
////				con.close();
////			} catch (Exception e) {
////				e.printStackTrace(System.err);
////			}
////		}
////	}
////	return memberVO_login;
////}
//	
//}