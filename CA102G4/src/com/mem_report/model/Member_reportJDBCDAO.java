//package com.mem_report.model;
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
//
//public class Member_reportJDBCDAO implements Member_reportDAO_interface {
//	String driver = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String userid = "CA102G4";
//	String passwd = "12345678";
//	
//	private static final String INSERT_STMT = 
//			"Insert into MEMBER_REPORT (MEM_ID_REPORT,MEM_ID_REPORTED,REPORT_TIME,REPORT_REASON,MEM_REP_STA) VALUES (?,?,?,?,?)";
//	private static final String GET_ALL_STMT =
//			"SELECT * FROM MEMBER_REPORT ORDER BY MEM_ID_REPORT,MEM_ID_REPORTED";
//	private static final String GET_ONE_STMT =
//			"SELECT * FROM MEMBER_REPORT WHERE MEM_ID_REPORT =? AND MEM_ID_REPORTED = ?";
//	private static final String DELETE_MEMBER_REPORT = 
//			"DELETE FROM MEMBER_REPORT WHERE MEM_ID_REPORT= ? AND MEM_ID_REPORTED= ?";
//	private static final String UPDATE = 
//			"UPDATE MEMBER_REPORT SET REPORT_TIME= ?,REPORT_REASON= ?,MEM_REP_STA= ? WHERE MEM_ID_REPORT =? AND MEM_ID_REPORTED = ?";
//
//	
//	
//	@Override
//	public void insert(Member_reportVO member_reportVO) {
//		
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(INSERT_STMT);
//			
//			pstmt.setString(1, member_reportVO.getMem_Id_report());
//			pstmt.setString(2, member_reportVO.getMem_Id_reported());
//			pstmt.setTimestamp(3, member_reportVO.getReport_Time());
//			pstmt.setString(4, member_reportVO.getReport_Reason());
//			pstmt.setInt(5, member_reportVO.getMem_Rep_Sta());
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
//	public void update(Member_reportVO member_reportVO) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(UPDATE);
//			
//			
//
//			pstmt.setTimestamp(1, member_reportVO.getReport_Time());
//			pstmt.setString(2, member_reportVO.getReport_Reason());
//			pstmt.setInt(3, member_reportVO.getMem_Rep_Sta());
//			pstmt.setString(4, member_reportVO.getMem_Id_report());
//			pstmt.setString(5, member_reportVO.getMem_Id_reported());
//			
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
//
//	@Override
//	public void delete(String mem_Id_Report, String mem_Id_Reported) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			Class.forName(driver);
//			
//			con = DriverManager.getConnection(url, userid, passwd);
//
//			pstmt = con.prepareStatement(DELETE_MEMBER_REPORT);
//
//			pstmt.setString(1, mem_Id_Report);
//			pstmt.setString(2, mem_Id_Reported);
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
//	public Member_reportVO findByPrimaryKey(String mem_Id_Report, String mem_Id_Reported) {
//		
//		Member_reportVO member_reportVO = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			Class.forName(driver);
//			con = DriverManager.getConnection(url, userid, passwd);
//			pstmt = con.prepareStatement(GET_ONE_STMT);
//
//			pstmt.setString(1, mem_Id_Report);
//			pstmt.setString(2, mem_Id_Reported);
//
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				member_reportVO = new Member_reportVO();
//				member_reportVO.setMem_Id_report(rs.getString("MEM_ID_REPORT"));
//				member_reportVO.setMem_Id_reported(rs.getString("MEM_ID_REPORTED"));
//				member_reportVO.setReport_Time(rs.getTimestamp("REPORT_TIME"));
//				member_reportVO.setReport_Reason(rs.getString("REPORT_REASON"));
//				member_reportVO.setMem_Rep_Sta(rs.getInt("MEM_REP_STA"));
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
//		return member_reportVO;
//	}
//
//	@Override
//	public List<Member_reportVO> getAll() {
//		
//		List<Member_reportVO> list = new ArrayList<Member_reportVO>();
//		Member_reportVO member_reportVO = null;
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
//				member_reportVO = new Member_reportVO();
//				member_reportVO.setMem_Id_report(rs.getString("MEM_ID_REPORT"));
//				member_reportVO.setMem_Id_reported(rs.getString("MEM_ID_REPORTED"));
//				member_reportVO.setReport_Time(rs.getTimestamp("REPORT_TIME"));
//				member_reportVO.setReport_Reason(rs.getString("REPORT_REASON"));
//				member_reportVO.setMem_Rep_Sta(rs.getInt("MEM_REP_STA"));
//				list.add(member_reportVO); // Store the row in the list
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
//
//	public static void main(String[] args) throws IOException {
//
//		Member_reportJDBCDAO dao = new Member_reportJDBCDAO();
//
////		insert
////		Member_reportVO member_reportVO = new Member_reportVO();
////		member_reportVO.setMem_Id_report("M000006");
////		member_reportVO.setMem_Id_reported("M000006");
////		member_reportVO.setReport_Time(Timestamp.valueOf("2018-3-15 10:00:00"));
////		member_reportVO.setReport_Reason("看你不爽");
////		member_reportVO.setMem_Rep_Sta(1);
////		dao.insert(member_reportVO);
//		
//		//update
////		Member_reportVO member_reportVO = new Member_reportVO();
////		member_reportVO.setMem_Id_report("M000006");
////		member_reportVO.setMem_Id_reported("M000006");
////		member_reportVO.setReport_Time(Timestamp.valueOf("2018-8-20 10:10:00"));
////		member_reportVO.setReport_Reason("看你爽");
////		member_reportVO.setMem_Rep_Sta(2);
////		dao.update(member_reportVO);
////		
//		//delete()
////		dao.delete("M000006","M000006");
//		
//		//findByPrimaryKey()
////		 Member_reportVO member_reportVO = dao.findByPrimaryKey("M000004","M000002");
////		 System.out.print(member_reportVO.getMem_Id_report() + ", ");
////		 System.out.print(member_reportVO.getMem_Id_reported() + ", ");
////		 System.out.print(member_reportVO.getReport_Time()+", ");
////		 System.out.print(member_reportVO.getReport_Reason()+", ");
////		 System.out.print(member_reportVO.getMem_Rep_Sta()+", ");
//		 
//		//getAll()
//	
//		List<Member_reportVO> list = dao.getAll(); for (Member_reportVO aMember_reportVO : list) {
//		System.out.print(aMember_reportVO.getMem_Id_report() + ", ");
//		System.out.print(aMember_reportVO.getMem_Id_reported() + ", ");
//		System.out.print(aMember_reportVO.getReport_Time()+", ");
//		System.out.print(aMember_reportVO.getReport_Reason()+", ");
//		System.out.print(aMember_reportVO.getMem_Rep_Sta()+", ");
//		}
//		 
//	}
//		
//}
