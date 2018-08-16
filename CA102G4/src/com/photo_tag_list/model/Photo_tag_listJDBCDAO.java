//package com.photo_tag_list.model;
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
//import com.mem_report.model.Member_reportVO;
//
//
//public class Photo_tag_listJDBCDAO implements Photo_tag_listDAO_interface{
//
//	String driver = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String userid = "CA102G4";
//	String passwd = "12345678";
//	
//	private static final String INSERT_STMT =
//			"Insert into PHOTO_TAG_LIST (PHOTO_TAG_NO,PHOTO_NO) VALUES"
//			+ " (?,?)";
//	private static final String DELETE_PHOTO_TAG_LIST = 
//			"DELETE FROM PHOTO_TAG_LIST WHERE PHOTO_TAG_NO= ?";
//	private static final String GET_ONE_STMT =
//			"SELECT * FROM PHOTO_TAG_LIST WHERE PHOTO_TAG_NO = ?";	
//	private static final String GET_ALL_STMT =
//			"SELECT * FROM PHOTO_TAG_LIST ORDER BY PHOTO_TAG_NO";
//	private static final String GET_PHOTO_NO =
//			"SELECT PHOTO_NO FROM PHOTO_TAG_LIST WHERE PHOTO_TAG_NO = ?";
//	
//	
//	
//	@Override
//	public void insert(Photo_tag_listVO photo_tag_listVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(INSERT_STMT);
//			
//			pstmt.setString(1, photo_tag_listVO.getPhoto_Tag_No());
//			pstmt.setString(2, photo_tag_listVO.getPhoto_No());
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
//	public void delete(String photo_Tag_No) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//
//			pstmt = con.prepareStatement(DELETE_PHOTO_TAG_LIST);
//
//			pstmt.setString(1, photo_Tag_No);
//
//			pstmt.executeUpdate();
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
//	public Photo_tag_listVO findByPrimaryKey(String photo_Tag_No,String photo_No) {
//		Photo_tag_listVO photo_tag_listVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ONE_STMT);
//
//			pstmt.setString(1, photo_Tag_No);
//			pstmt.setString(2, photo_No);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				photo_tag_listVO = new Photo_tag_listVO();
//				photo_tag_listVO.setPhoto_Tag_No(rs.getString("PHOTO_TAG_NO"));
//				photo_tag_listVO.setPhoto_No(rs.getString("PHOTO_NO"));
//			}
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
//		return photo_tag_listVO;
//	}
//		
//	@Override
//	public List<Photo_tag_listVO> getAll() {
//		List<Photo_tag_listVO> list = new ArrayList<Photo_tag_listVO>();
//		Photo_tag_listVO photo_tag_listVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ALL_STMT);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				photo_tag_listVO = new Photo_tag_listVO();
//				photo_tag_listVO.setPhoto_Tag_No(rs.getString("PHOTO_TAG_NO"));
//				photo_tag_listVO.setPhoto_No(rs.getString("PHOTO_NO"));
//				list.add(photo_tag_listVO); // Store the row in the list
//			}
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
//	@Override
//	public List<Photo_tag_listVO> getAll_Photo_No(String photo_Tag_No) {
//		List<Photo_tag_listVO> list = new ArrayList<Photo_tag_listVO>();
//		Photo_tag_listVO photo_tag_listVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//			try {
//				Class.forName(driver);
//				con = DriverManager.getConnection(url, userid, passwd);
//				pstmt = con.prepareStatement(GET_PHOTO_NO);
//				
//				pstmt.setString(1, photo_Tag_No);
//				
//				rs = pstmt.executeQuery();
//
//				while (rs.next()) {
//					photo_tag_listVO = new Photo_tag_listVO();
////					photo_tag_listVO.setPhoto_Tag_No(rs.getString("PHOTO_TAG_NO"));
//					photo_tag_listVO.setPhoto_No(rs.getString("PHOTO_NO"));
//
//					list.add(photo_tag_listVO); // Store the row in the list
//				}
//
//				// Handle any driver errors
//			} catch (ClassNotFoundException e) {
//				throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//				// Handle any SQL errors
//			} catch (SQLException se) {
//				throw new RuntimeException("A database error occured. " + se.getMessage());
//				// Clean up JDBC resources
//			} finally {
//				if (rs != null) {
//					try {
//						rs.close();
//					} catch (SQLException se) {
//						se.printStackTrace(System.err);
//					}
//				}
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
//			return list;
//		}
//	
//	
//	public static void main(String[] args) throws IOException {
//
//		Photo_tag_listJDBCDAO dao = new Photo_tag_listJDBCDAO();
//		
//		//insert
////		Photo_tag_listVO photo_tag_listVO = new Photo_tag_listVO();
////		photo_tag_listVO.setPhoto_Tag_No("PTN000001");
////		photo_tag_listVO.setPhoto_No("P000007");
////		dao.insert(photo_tag_listVO);
//
//		//delete()
////		dao.delete("PTN000005");
//		
//		//findByPrimaryKey()
////		Photo_tag_listVO photo_tag_listVO = dao.findByPrimaryKey("PTN000001","P000001");
////		System.out.print(photo_tag_listVO.getPhoto_Tag_No() + ", ");
////		System.out.print(photo_tag_listVO.getPhoto_No() + ", ");
//		
//		//getAll()
////		List<Photo_tag_listVO> list = dao.getAll(); for (Photo_tag_listVO aphoto_tag_listVO : list) {
////		System.out.print(aphoto_tag_listVO.getPhoto_Tag_No() + ", ");
////		System.out.print(aphoto_tag_listVO.getPhoto_No() + ", ");
////		}
//		
//		//getAll_Photo_No()
//		List<Photo_tag_listVO> list = dao.getAll_Photo_No("PTN000001"); for (Photo_tag_listVO aphoto_tag_listVO : list) {
//		System.out.print(aphoto_tag_listVO.getPhoto_No() + "\n");
////		System.out.print(aphoto_tag_listVO.getPhoto_Tag_No() + ", ");
//		}
//		
//		System.out.println("OK");
//	}
//
//	
//
//}
