//package com.photo_report.model;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Photo_reportJDBCDAO implements Photo_reportDAO_interface {
//	String driver = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String userid = "CA102G4";
//	String passwd = "12345678";
//	
//	private static final String INSERT_STMT =
//			"Insert into PHOTO_REPORT (PHOTO_NO,MEM_ID,REPORT_REASON,REPORT_TIME,PHO_REP_STATS) VALUES (?,?,?,?,?)";
//	private static final String UPDATE = 
//			"UPDATE PHOTO_REPORT SET REPORT_REASON= ?,REPORT_TIME=?,PHO_REP_STATS=? WHERE PHOTO_NO =? AND MEM_ID = ?";
//	private static final String DELETE_PHOTO_REPORT = 
//			"DELETE FROM PHOTO_REPORT WHERE PHOTO_NO =? AND MEM_ID = ?";
//	private static final String GET_ONE = 
//			"SELECT * FROM PHOTO_REPORT WHERE PHOTO_NO = ? AND MEM_ID=?";
//	private static final String GET_ALL = 
//			"SELECT * FROM PHOTO_REPORT ORDER BY PHOTO_NO,MEM_ID";
//	
//	
//	@Override
//	public void insert(Photo_reportVO photo_reportVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(INSERT_STMT);
//			
//			pstmt.setString(1, photo_reportVO.getPhoto_No());
//			pstmt.setString(2, photo_reportVO.getMem_Id());
//			pstmt.setString(3, photo_reportVO.getReport_Reason());
//			pstmt.setTimestamp(4, photo_reportVO.getReport_Time());
//			pstmt.setInt(5, photo_reportVO.getPho_Rep_Stats());
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
//	public void update(Photo_reportVO photo_reportVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(UPDATE);
//
//			pstmt.setString(1, photo_reportVO.getReport_Reason());
//			pstmt.setTimestamp(2, photo_reportVO.getReport_Time());
//			pstmt.setInt(3, photo_reportVO.getPho_Rep_Stats());
//			pstmt.setString(4, photo_reportVO.getPhoto_No());
//			pstmt.setString(5, photo_reportVO.getMem_Id());
//			pstmt.executeUpdate();
//
//			// Handle any driver errors
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
//			// Handle any SQL errors
//		} catch (SQLException se) {
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
//	public void delete(String photo_Id, String mem_Id) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//
//			pstmt = con.prepareStatement(DELETE_PHOTO_REPORT);
//
//			pstmt.setString(1, photo_Id);
//			pstmt.setString(2, mem_Id);
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
//	@Override
//	public Photo_reportVO findByPrimaryKey(String photo_No, String mem_Id) {
//		Photo_reportVO photo_reportVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ONE);
//
//			pstmt.setString(1, photo_No);
//			pstmt.setString(2, mem_Id);
//			
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				photo_reportVO = new Photo_reportVO();
//				photo_reportVO.setPhoto_No(rs.getString("PHOTO_NO"));
//				photo_reportVO.setMem_Id(rs.getString("MEM_ID"));
//				photo_reportVO.setReport_Reason(rs.getString("REPORT_REASON"));
//				photo_reportVO.setReport_Time(rs.getTimestamp("REPORT_TIME"));
//				photo_reportVO.setPho_Rep_Stats(rs.getInt("PHO_REP_STATS"));
//
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
//		return photo_reportVO;
//	}
//	@Override
//	public List<Photo_reportVO> getAll() {
//		List<Photo_reportVO> list = new ArrayList<Photo_reportVO>();
//		Photo_reportVO photo_reportVO = null;
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
//				photo_reportVO = new Photo_reportVO();
//				photo_reportVO.setPhoto_No(rs.getString("PHOTO_NO"));
//				photo_reportVO.setMem_Id(rs.getString("MEM_ID"));
//				photo_reportVO.setReport_Reason(rs.getString("REPORT_REASON"));
//				photo_reportVO.setReport_Time(rs.getTimestamp("REPORT_TIME"));
//				photo_reportVO.setPho_Rep_Stats(rs.getInt("PHO_REP_STATS"));
//				list.add(photo_reportVO); // Store the row in the list
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
//	public static void main(String[] args) throws IOException {
//
//		Photo_reportJDBCDAO dao = new Photo_reportJDBCDAO();
//		//insert
////		Photo_reportVO photo_reportVO = new Photo_reportVO();
////		photo_reportVO.setPhoto_No("P000005");
////		photo_reportVO.setMem_Id("M000001");
////		photo_reportVO.setReport_Reason("888");
////		photo_reportVO.setReport_Time(Timestamp.valueOf("2018-5-17 12:12:12"));
////		photo_reportVO.setPho_Rep_Stats(1);
////		dao.insert(photo_reportVO);
//		
//		//update
////		Photo_reportVO photo_reportVO = new Photo_reportVO();
////		photo_reportVO.setPhoto_No("P000005");
////		photo_reportVO.setMem_Id("M000001");
////		photo_reportVO.setReport_Reason("777");
////		photo_reportVO.setReport_Time(Timestamp.valueOf("2018-5-17 12:12:12"));
////		photo_reportVO.setPho_Rep_Stats(2);
////		dao.update(photo_reportVO);
//		
//		//delete()
////		dao.delete("P000005","M000001");
//		
//		//findByPrimaryKey()
////		Photo_reportVO photo_reportVO = dao.findByPrimaryKey("P000007","M000007");
////		System.out.print(photo_reportVO.getPhoto_No() + ", ");
////		System.out.print(photo_reportVO.getMem_Id() + ", ");
////		System.out.print(photo_reportVO.getReport_Reason() + ", ");
////		System.out.print(photo_reportVO.getReport_Time() + ", ");
////		System.out.print(photo_reportVO.getPho_Rep_Stats() + ", ");
//		
//		List<Photo_reportVO> list = dao.getAll(); for (Photo_reportVO aphoto_reportVO : list) {
//			System.out.print(aphoto_reportVO.getPhoto_No() + ", ");
//			System.out.print(aphoto_reportVO.getMem_Id() + ", ");
//			System.out.print(aphoto_reportVO.getReport_Reason() + ", ");
//			System.out.print(aphoto_reportVO.getReport_Time() + ", ");
//			System.out.print(aphoto_reportVO.getPho_Rep_Stats() + ", ");
//			}
//		
//		System.out.println("OK");
//	}
//	
//}
