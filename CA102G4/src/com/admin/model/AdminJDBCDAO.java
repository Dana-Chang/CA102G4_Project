//package com.admin.model;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AdminJDBCDAO implements AdminDAO_interface {
//	
//	String driver = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String userid = "CA102G4";
//	String passwd = "12345678";
//	
//	private static final String INSERT_STMT =
//			"Insert into ADMIN (ADMIN_ID,ADMIN_ACCOUNT,ADMIN_PASSWORD,ADMIN_NAME,ADMIN_MAIL,ADMIN_PHONE) VALUES ('ADMIN'||LPAD(to_char(ADMIN_seq.NEXTVAL), 3, '0'),?,?,?,?,?)";
//	private static final String UPDATE =
//			"UPDATE ADMIN SET ADMIN_ACCOUNT=?,ADMIN_PASSWORD=?,ADMIN_NAME=?,ADMIN_MAIL=?,ADMIN_PHONE=? WHERE ADMIN_ID =? ";
//	private static final String DELETE_ADMIN =
//			"DELETE FROM ADMIN WHERE ADMIN_ID = ?";
//	private static final String GET_ONE = 
//			"SELECT * FROM ADMIN WHERE ADMIN_ID = ?";
//	private static final String GET_ALL = 
//			"SELECT * FROM ADMIN ORDER BY ADMIN_ID";
//	
//	
//
//	@Override
//	public void insert(AdminVO adminVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(INSERT_STMT);
//			
//			pstmt.setString(1, adminVO.getAdmin_Account());
//			pstmt.setString(2, adminVO.getAdmin_Password());
//			pstmt.setString(3, adminVO.getAdmin_Name());
//			pstmt.setString(4, adminVO.getAdmin_Mail());
//			pstmt.setString(5, adminVO.getAdmin_Phone());
//			pstmt.executeUpdate();
//			
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. "
//					+ e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. "
//					+ se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//	}
//
//	@Override
//	public void update(AdminVO adminVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(UPDATE);
//			
//			pstmt.setString(1, adminVO.getAdmin_Account());
//			pstmt.setString(2, adminVO.getAdmin_Password());
//			pstmt.setString(3, adminVO.getAdmin_Name());
//			pstmt.setString(4, adminVO.getAdmin_Mail());
//			pstmt.setString(5, adminVO.getAdmin_Phone());
//			pstmt.setString(6, adminVO.getAdmin_Id());
//			pstmt.executeUpdate();
//			
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. "
//					+ e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. "
//					+ se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//	}
//
//	@Override
//	public void delete(String admin_Id) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//			
//			con.setAutoCommit(false);
//			
//			pstmt = con.prepareStatement(DELETE_ADMIN);
//
//			pstmt.setString(1, admin_Id);
//
//			pstmt.executeUpdate();
//
//			con.commit();
//			con.setAutoCommit(true);
//			
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			if (con != null) {
//				try {
//					con.rollback();
//				} catch (SQLException excep) {
//					throw new RuntimeException("rollback error occured. " + excep.getMessage());
//				}
//			}
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//	}
//
//	@Override
//	public AdminVO findByPrimaryKey(String admin_Id) {
//		AdminVO adminVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ONE);
//
//			pstmt.setString(1, admin_Id);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				
//				adminVO = new AdminVO();
//				adminVO.setAdmin_Id(rs.getString("ADMIN_ID"));
//				adminVO.setAdmin_Account(rs.getString("ADMIN_ACCOUNT"));
//				adminVO.setAdmin_Password(rs.getString("ADMIN_PASSWORD"));
//				adminVO.setAdmin_Name(rs.getString("ADMIN_NAME"));
//				adminVO.setAdmin_Mail(rs.getString("ADMIN_MAIL"));
//				adminVO.setAdmin_Phone(rs.getString("ADMIN_PHONE"));		
//			}
//			
//
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//		return adminVO;
//	}
//
//	@Override
//	public List<AdminVO> getAll() {
//		List<AdminVO> list = new ArrayList<AdminVO>();
//		AdminVO adminVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ALL);
//			
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				
//			adminVO = new AdminVO();
//			adminVO.setAdmin_Id(rs.getString("ADMIN_ID"));
//			adminVO.setAdmin_Account(rs.getString("ADMIN_ACCOUNT"));
//			adminVO.setAdmin_Password(rs.getString("ADMIN_PASSWORD"));
//			adminVO.setAdmin_Name(rs.getString("ADMIN_NAME"));
//			adminVO.setAdmin_Mail(rs.getString("ADMIN_MAIL"));
//			adminVO.setAdmin_Phone(rs.getString("ADMIN_PHONE"));		
//			list.add(adminVO);
//			}
//			
//
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. " + se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}
//		return list;
//	}
//	
//	public static void main(String[] args) throws IOException {
//
//		AdminJDBCDAO dao = new AdminJDBCDAO();
//		
//		//insert
////		AdminVO adminVO = new AdminVO();
////		adminVO.setAdmin_Account("A123456");
////		adminVO.setAdmin_Password("A123456");
////		adminVO.setAdmin_Name("2266");
////		adminVO.setAdmin_Mail("A123456");
////		adminVO.setAdmin_Phone("A123456");
////		dao.insert(adminVO);
//		
//		//update
////		AdminVO adminVO = new AdminVO();
////		adminVO.setAdmin_Account("A888123");
////		adminVO.setAdmin_Password("A888888");
////		adminVO.setAdmin_Name("546546");
////		adminVO.setAdmin_Mail("A466");
////		adminVO.setAdmin_Phone("A1464656");
////		adminVO.setAdmin_Id("ADMIN006");
////		dao.update(adminVO);
//		
//		//delete
////		dao.delete("ADMIN006");
//		
//		
//		//findByPrimaryKey() 
////		 AdminVO adminVO = dao.findByPrimaryKey("ADMIN005");
////		 System.out.print(adminVO.getAdmin_Id() + ", ");
////		 System.out.print(adminVO.getAdmin_Account() + ", ");
////		 System.out.print(adminVO.getAdmin_Password() + ", ");
////		 System.out.print(adminVO.getAdmin_Name() + ", ");
////		 System.out.print(adminVO.getAdmin_Mail() + ", ");
////		 System.out.print(adminVO.getAdmin_Phone() + ", ");
//
//		//getALL()
////		List<AdminVO> list = dao.getAll(); for (AdminVO adminVO : list) {
////		System.out.print(adminVO.getAdmin_Id() + ", ");
////		System.out.print(adminVO.getAdmin_Account() + ", ");
////		System.out.print(adminVO.getAdmin_Password() + ", ");
////		System.out.print(adminVO.getAdmin_Name() + ", ");
////		System.out.print(adminVO.getAdmin_Mail() + ", ");
////		System.out.print(adminVO.getAdmin_Phone() + ", ");
////		
////		}
//		System.out.println("OK");
//	}
//
//}
