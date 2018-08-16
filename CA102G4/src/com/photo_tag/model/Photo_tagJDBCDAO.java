//package com.photo_tag.model;
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
//public class Photo_tagJDBCDAO implements Photo_tagDAO_interface {
//	
//	String driver = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String userid = "CA102G4";
//	String passwd = "12345678";
//	
//	private static final String INSERT_STMT =
//			"Insert into PHOTO_TAG (PHOTO_TAG_NO,TAG_CONTENT) VALUES ('PTN'||LPAD(to_char(PHOTO_TAG_seq.NEXTVAL), 6, '0'),?)";
//	private static final String UPDATE =
//			"UPDATE PHOTO_TAG SET TAG_CONTENT=? WHERE PHOTO_TAG_NO =? ";
//	private static final String DELETE_PHOTO_TAG =
//			"DELETE FROM PHOTO_TAG WHERE PHOTO_TAG_NO = ?";
//	private static final String GET_ONE = 
//			"SELECT * FROM PHOTO_TAG WHERE PHOTO_TAG_NO = ?";
//	private static final String GET_ALL = 
//			"SELECT * FROM PHOTO_TAG ORDER BY PHOTO_TAG_NO";
//	
//	
//	@Override
//	public void insert(Photo_tagVO photo_tagVO) {
//			Connection con = null;
//			PreparedStatement pstmt = null;
//			
//			try {
//				Class.forName(driver);
//				
//				con = DriverManager.getConnection(url, userid, passwd);
//				pstmt = con.prepareStatement(INSERT_STMT);
//				
//				pstmt.setString(1, photo_tagVO.getTag_Content());
//				pstmt.executeUpdate();
//				
//			} catch (ClassNotFoundException e) {
//				throw new RuntimeException("Couldn't load database driver. "
//						+ e.getMessage());
//				// Handle any SQL errors
//			} catch (SQLException se) {
//				throw new RuntimeException("A database error occured. "
//						+ se.getMessage());
//				// Clean up JDBC resources
//			} finally {
//				if (pstmt != null) {
//					try {
//						pstmt.close();
//					} catch (SQLException se) {
//						se.printStackTrace(System.err);
//					}
//				}
//				if (con != null) {
//					try {
//						con.close();
//					} catch (Exception e) {
//						e.printStackTrace(System.err);
//					}
//				}
//			}
//		}
//	@Override
//	public void update(Photo_tagVO photo_tagVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(UPDATE);
//			
//			pstmt.setString(1, photo_tagVO.getTag_Content());
//			pstmt.setString(2, photo_tagVO.getPhoto_Tag_No());
//
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
//	@Override
//	public void delete(String photo_Tag_No) {
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
//			pstmt = con.prepareStatement(DELETE_PHOTO_TAG);
//
//			pstmt.setString(1, photo_Tag_No);
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
//	@Override
//	public Photo_tagVO findByPrimaryKey(String photo_Tag_No) {
//		Photo_tagVO photo_tagVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ONE);
//
//			pstmt.setString(1,photo_Tag_No);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				
//				photo_tagVO = new Photo_tagVO();
//				photo_tagVO.setPhoto_Tag_No(rs.getString("PHOTO_TAG_NO"));
//				photo_tagVO.setTag_Content(rs.getString("TAG_CONTENT"));
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
//		return photo_tagVO;
//	}
//	@Override
//	public List<Photo_tagVO> getAll() {
//		
//		List<Photo_tagVO> list = new ArrayList<Photo_tagVO>();
//		Photo_tagVO photo_tagVO = null;
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
//				photo_tagVO = new Photo_tagVO();
//				photo_tagVO.setPhoto_Tag_No(rs.getString("PHOTO_TAG_NO"));
//				photo_tagVO.setTag_Content(rs.getString("TAG_CONTENT"));
//			
//				list.add(photo_tagVO);
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
//		Photo_tagJDBCDAO dao = new Photo_tagJDBCDAO();
//		
//		//insert
////		Photo_tagVO photo_tagVO = new Photo_tagVO();
////		photo_tagVO.setTag_Content("台北");
////		dao.insert(photo_tagVO);
//		
//		//update
////		Photo_tagVO photo_tagVO = new Photo_tagVO();
////		photo_tagVO.setTag_Content("象山");
////		photo_tagVO.setPhoto_Tag_No("PTN000008");
////		dao.update(photo_tagVO);
//		
//		//delete
////		dao.delete("PTN000008");
//		
//		//findByPrimaryKey() 
////		 Photo_tagVO photo_tagVO = dao.findByPrimaryKey("PTN000005");
////		 System.out.print(photo_tagVO.getPhoto_Tag_No() + ", ");
////		 System.out.print(photo_tagVO.getTag_Content() + ", ");
////		
//		//getALL()
//		List<Photo_tagVO> list = dao.getAll(); for (Photo_tagVO aphoto_tagVO : list) {
//		System.out.print(aphoto_tagVO.getPhoto_Tag_No() + ", ");
//		System.out.print(aphoto_tagVO.getTag_Content() + ", ");
//		}
//		
//		System.out.println("ok");
//	}
//}
