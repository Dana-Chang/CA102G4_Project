package com.grp_report.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class Grp_reportJDBCDAO implements Grp_reportDAO_interface {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String userid = "CA102G4";
	String passwd = "12345678";
	
	private static final String INSERT_STMT =
			"Insert into GRP_REPORT (GRP_ID,MEM_ID,REPORT_REASON,REPORT_TIME,GRP_REP_STA) VALUES (?,?,?,?,?)";
	private static final String UPDATE = 
			"UPDATE GRP_REPORT SET REPORT_REASON= ?,REPORT_TIME=?,GRP_REP_STA=? WHERE GRP_ID =? AND MEM_ID = ?";
	private static final String DELETE_GRP_REPORT = 
			"DELETE FROM GRP_REPORT WHERE GRP_ID =? AND MEM_ID = ?";
	private static final String GET_ONE = 
			"SELECT * FROM GRP_REPORT WHERE GRP_ID = ? AND MEM_ID=?";
	private static final String GET_ALL = 
			"SELECT * FROM GRP_REPORT ORDER BY GRP_ID,MEM_ID";
	
	
	@Override
	public void insert(Grp_reportVO grp_reportVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName(driver);
			
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, grp_reportVO.getGrp_Id());
			pstmt.setString(2, grp_reportVO.getMem_Id());
			pstmt.setString(3, grp_reportVO.getReport_Reason());
			pstmt.setTimestamp(4, grp_reportVO.getReport_Time());
			pstmt.setInt(5, grp_reportVO.getGrp_Rep_Sta());

			pstmt.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
	@Override
	public void update(Grp_reportVO grp_reportVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setString(1, grp_reportVO.getReport_Reason());
			pstmt.setTimestamp(2, grp_reportVO.getReport_Time());
			pstmt.setInt(3, grp_reportVO.getGrp_Rep_Sta());
			pstmt.setString(4, grp_reportVO.getGrp_Id());
			pstmt.setString(5, grp_reportVO.getMem_Id());
			
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
	@Override
	public void delete(String grp_Id, String mem_Id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName(driver);
			
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(DELETE_GRP_REPORT);

			pstmt.setString(1, grp_Id);
			pstmt.setString(2, mem_Id);

			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException excep) {
					throw new RuntimeException("rollback error occured. " + excep.getMessage());
				}
			}
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
	@Override
	public Grp_reportVO findByPrimaryKey(String grp_Id, String mem_Id) {
		
		Grp_reportVO grp_reportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE);

			pstmt.setString(1, grp_Id);
			pstmt.setString(2, mem_Id);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				grp_reportVO = new Grp_reportVO();
				grp_reportVO.setGrp_Id(rs.getString("GRP_ID"));
				grp_reportVO.setMem_Id(rs.getString("MEM_ID"));
				grp_reportVO.setReport_Reason(rs.getString("REPORT_REASON"));
				grp_reportVO.setReport_Time(rs.getTimestamp("REPORT_TIME"));
				grp_reportVO.setGrp_Rep_Sta(rs.getInt("GRP_REP_STA"));

			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return grp_reportVO;
	}
	@Override
	public List<Grp_reportVO> getAll() {
		
		
	List<Grp_reportVO> list = new ArrayList<Grp_reportVO>();
	Grp_reportVO grp_reportVO = null;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	try {
		Class.forName(driver);
		con = DriverManager.getConnection(url, userid, passwd);
		pstmt = con.prepareStatement(GET_ALL);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			grp_reportVO = new Grp_reportVO();
			grp_reportVO.setGrp_Id(rs.getString("GRP_ID"));
			grp_reportVO.setMem_Id(rs.getString("MEM_ID"));
			grp_reportVO.setReport_Reason(rs.getString("REPORT_REASON"));
			grp_reportVO.setReport_Time(rs.getTimestamp("REPORT_TIME"));
			grp_reportVO.setGrp_Rep_Sta(rs.getInt("GRP_REP_STA"));
			list.add(grp_reportVO); // Store the row in the list
		}

		// Handle any driver errors
	} catch (ClassNotFoundException e) {
		throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		// Handle any SQL errors
	} catch (SQLException se) {
		throw new RuntimeException("A database error occured. " + se.getMessage());
		// Clean up JDBC resources
	} finally {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException se) {
				se.printStackTrace(System.err);
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException se) {
				se.printStackTrace(System.err);
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}
	return list;
}
	
	public static void main(String[] args) throws IOException {

		Grp_reportJDBCDAO dao = new Grp_reportJDBCDAO();
		//insert
//		Grp_reportVO grp_reportVO = new Grp_reportVO();
//		grp_reportVO.setGrp_Id("GRP000005");
//		grp_reportVO.setMem_Id("M000001");
//		grp_reportVO.setReport_Reason("66666");
//		grp_reportVO.setReport_Time(Timestamp.valueOf("2018-5-17 12:12:12"));
//		grp_reportVO.setGrp_Rep_Sta(1);
//		dao.insert(grp_reportVO);
		
		//update
//		Grp_reportVO grp_reportVO = new Grp_reportVO();
//		grp_reportVO.setGrp_Id("GRP000005");
//		grp_reportVO.setMem_Id("M000001");
//		grp_reportVO.setReport_Reason("7777");
//		grp_reportVO.setReport_Time(Timestamp.valueOf("2018-5-17 12:32:50"));
//		grp_reportVO.setGrp_Rep_Sta(2);
//		dao.update(grp_reportVO);
		
		//delete()
//		dao.delete("GRP000005","M000001");
		
		//findByPrimaryKey()
//		Grp_reportVO grp_reportVO = dao.findByPrimaryKey("GRP000005","M000003");
//		System.out.print(grp_reportVO.getGrp_Id() + ", ");
//		System.out.print(grp_reportVO.getMem_Id() + ", ");
//		System.out.print(grp_reportVO.getReport_Reason() + ", ");
//		System.out.print(grp_reportVO.getReport_Time() + ", ");
//		System.out.print(grp_reportVO.getGrp_Rep_Sta() + ", ");
		//getAll()
		List<Grp_reportVO> list = dao.getAll(); for (Grp_reportVO agrp_reportVO : list) {
		System.out.print(agrp_reportVO.getGrp_Id() + ", ");
		System.out.print(agrp_reportVO.getMem_Id() + ", ");
		System.out.print(agrp_reportVO.getReport_Reason() + ", ");
		System.out.print(agrp_reportVO.getReport_Time() + ", ");
		System.out.print(agrp_reportVO.getGrp_Rep_Sta() + ", ");

		}
		System.out.println("OK");
	}
	
}