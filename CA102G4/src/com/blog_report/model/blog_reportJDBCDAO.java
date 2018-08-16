package com.blog_report.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class blog_reportJDBCDAO implements blog_reportDAO_interface {
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";

	// 新增一個部落格被一個會員檢舉
	private static final String INSERT_STMT = "INSERT INTO BLOG_REPORT(BLOG_ID,MEM_ID,BR_REASON,BR_TIME,BR_STATUS) VALUES(?,?,?,SYSDATE,0)";
	// 修改檢舉處理狀態
	private static final String UPDATE_STMT = "UPDATE BLOG_REPORT SET BR_STATUS = ? WHERE BLOG_ID = ? AND MEM_ID = ?";
	// 傳回全部檢舉清單根據檢舉處理狀況排序，未處理的排上面
	private static final String GET_ALL_STMT = "SELECT * FROM BLOG_REPORT ORDER BY BR_STATUS";
	// 傳回單筆
	private static final String GET_ONE_STMT ="SELECT * FROM BLOG_REPORT WHERE BLOG_ID = ? AND MEM_ID = ?";
	// 育萱+++++++++
	private static final String GET_BR_BYSTATUS ="SELECT * FROM BLOG_REPORT WHERE BR_STATUS = ?";
	@Override
	public int insert(blog_reportVO blog_reportVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, blog_reportVO.getBlog_id());
			pstmt.setString(2, blog_reportVO.getMem_id());
			pstmt.setString(3, blog_reportVO.getBr_reason());

			updateCount = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver." + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured." + se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return updateCount;
	}

	@Override
	public int update(blog_reportVO blog_reportVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);

			pstmt.setInt(1, blog_reportVO.getBr_status());
			pstmt.setString(2, blog_reportVO.getBlog_id());
			pstmt.setString(3, blog_reportVO.getMem_id());

			updateCount = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver." + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured." + se.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		}
		return updateCount;
	}

	@Override
	public List<blog_reportVO> getAll() {
		List<blog_reportVO> list = new ArrayList<blog_reportVO>();
		blog_reportVO blog_reportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_reportVO = new blog_reportVO();
				blog_reportVO.setBlog_id(rs.getString("BLOG_ID"));
				blog_reportVO.setMem_id(rs.getString("MEM_ID"));
				blog_reportVO.setBr_reason(rs.getString("BR_REASON"));
				blog_reportVO.setBr_time(rs.getTimestamp("BR_TIME"));
				blog_reportVO.setBr_status(rs.getInt("BR_STATUS"));
				list.add(blog_reportVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured." + se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public blog_reportVO getOne(String blog_id,String mem_id) {
		blog_reportVO blog_reportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, blog_id);
			pstmt.setString(2, mem_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				blog_reportVO = new blog_reportVO();
				blog_reportVO.setBlog_id(rs.getString("BLOG_ID"));
				blog_reportVO.setMem_id(rs.getString("MEM_ID"));
				blog_reportVO.setBr_reason(rs.getString("BR_REASON"));
				blog_reportVO.setBr_time(rs.getTimestamp("BR_TIME"));
				blog_reportVO.setBr_status(rs.getInt("BR_STATUS"));
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver." + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured." + se.getMessage());
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
		return blog_reportVO;
	}
	
	@Override
	public List<blog_reportVO> getBR_BySTATUS(Integer br_status) {
		List<blog_reportVO> list = new ArrayList<blog_reportVO>();
		blog_reportVO blog_reportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_BR_BYSTATUS);
			pstmt.setInt(1, br_status);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_reportVO = new blog_reportVO();
				blog_reportVO.setBlog_id(rs.getString("BLOG_ID"));
				blog_reportVO.setMem_id(rs.getString("MEM_ID"));
				blog_reportVO.setBr_reason(rs.getString("BR_REASON"));
				blog_reportVO.setBr_time(rs.getTimestamp("BR_TIME"));
				blog_reportVO.setBr_status(rs.getInt("BR_STATUS"));
				list.add(blog_reportVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured." + se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	

	public static void main(String args[]) {
		blog_reportJDBCDAO dao = new blog_reportJDBCDAO();

		// 新增一個部落格被一個會員檢舉
//		blog_reportVO blog_reportVO = new blog_reportVO();
//		blog_reportVO.setBlog_id("B000001");
//		blog_reportVO.setMem_id("M000010");
//		blog_reportVO.setBr_reason("抄襲我的內容~~");
//		int updateCount_insert = dao.insert(blog_reportVO);
//		System.out.println(updateCount_insert);
		
		// 修改檢舉處理狀態
//		blog_reportVO blog_reportVO2 = new blog_reportVO();
//		blog_reportVO2.setBr_status(1);
//		blog_reportVO2.setBlog_id("B000003");
//		blog_reportVO2.setMem_id("M000003");
//		int updateCount_update = dao.update(blog_reportVO2);
//		System.out.println(updateCount_update);
		
		// 傳回全部檢舉清單根據檢舉處理狀況排序，未處理的排上面
//		List<blog_reportVO> list = dao.getAll();
//
//		for (blog_reportVO br : list) {
//			System.out.print(br.getBlog_id() + "\t");
//			System.out.print(br.getMem_id() + "\t");
//			System.out.println(br.getBr_status());
//		}
		
		//查詢單筆
//		blog_reportVO blog_reportVO3 = dao.getOne("B000005", "M000010");
//		System.out.print(blog_reportVO3.getBlog_id() + "\t");
//		System.out.print(blog_reportVO3.getMem_id() + "\t");
//		System.out.print(blog_reportVO3.getBr_reason() + "\t");
//		System.out.print(blog_reportVO3.getBr_time() + "\t");
//		System.out.print(blog_reportVO3.getBr_status() + "\t");
		
		
		// 傳回全部檢舉清單根據檢舉處理狀況排序，未處理的排上面
		List<blog_reportVO> list = dao.getBR_BySTATUS(0);

		for (blog_reportVO br : list) {
			System.out.print(br.getBlog_id() + "\t");
			System.out.print(br.getMem_id() + "\t");
			System.out.println(br.getBr_status());
		}
		
	}


}
