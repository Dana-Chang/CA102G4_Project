package com.blog_report.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class blog_reportDAO implements blog_reportDAO_interface {
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
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
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, blog_reportVO.getBlog_id());
			pstmt.setString(2, blog_reportVO.getMem_id());
			pstmt.setString(3, blog_reportVO.getBr_reason());

			updateCount = pstmt.executeUpdate();
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
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);

			pstmt.setInt(1, blog_reportVO.getBr_status());
			pstmt.setString(2, blog_reportVO.getBlog_id());
			pstmt.setString(3, blog_reportVO.getMem_id());

			updateCount = pstmt.executeUpdate();
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
			con = ds.getConnection();
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
			con = ds.getConnection();
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
			con = ds.getConnection();
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
}
