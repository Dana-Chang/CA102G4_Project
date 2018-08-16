package com.blog_message_report.model;

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

import com.blog_report.model.blog_reportVO;

public class blog_message_reportDAO implements blog_message_reportDAO_interface {
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	// 新增一個部落格留言被檢舉
	private static final String INSERT_STMT = "INSERT INTO BLOG_MESSAGE_REPORT(MEM_ID,MESSAGE_ID,BMR_REASON,BMR_TIME,BMR_STATUS) VALUES(?,?,?,SYSDATE,0)";
	// 修改部落格留言檢舉狀態
	private static final String UPDATE_STMT = "UPDATE BLOG_MESSAGE_REPORT SET BMR_STATUS = ? WHERE MEM_ID = ? AND MESSAGE_ID = ?";
	// 傳回全部檢舉清單根據檢舉處理狀況排序，未處理的排上面
	private static final String GET_ALL_STMT = "SELECT * FROM BLOG_MESSAGE_REPORT ORDER BY BMR_STATUS";
	// 傳回單筆
	private static final String GET_ONE_STMT ="SELECT * FROM BLOG_MESSAGE_REPORT WHERE MEM_ID = ? AND MESSAGE_ID = ?";
		
	//****************************傳回處理狀況的List
	private static final String GETALL_BYSTATUS_STMT ="SELECT * FROM BLOG_MESSAGE_REPORT WHERE BMR_STATUS = ?";
	
	@Override
	public int insert(blog_message_reportVO blog_message_reportVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, blog_message_reportVO.getMem_id());
			pstmt.setString(2, blog_message_reportVO.getMessage_id());
			pstmt.setString(3, blog_message_reportVO.getBmr_reason());

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
	public int update(blog_message_reportVO blog_message_reportVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);

			pstmt.setInt(1, blog_message_reportVO.getBmr_status());
			pstmt.setString(2, blog_message_reportVO.getMem_id());
			pstmt.setString(3, blog_message_reportVO.getMessage_id());

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
	public List<blog_message_reportVO> getAll() {
		List<blog_message_reportVO> list = new ArrayList<blog_message_reportVO>();
		blog_message_reportVO blog_message_reportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_message_reportVO = new blog_message_reportVO();
				blog_message_reportVO.setMem_id(rs.getString("MEM_ID"));
				blog_message_reportVO.setMessage_id(rs.getString("MESSAGE_ID"));
				blog_message_reportVO.setBmr_reason(rs.getString("BMR_REASON"));
				blog_message_reportVO.setBmr_time(rs.getTimestamp("BMR_TIME"));
				blog_message_reportVO.setBmr_status(rs.getInt("BMR_STATUS"));
				list.add(blog_message_reportVO);
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
	public blog_message_reportVO getOne(String mem_id,String message_id) {
		blog_message_reportVO blog_message_reportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, mem_id);
			pstmt.setString(2, message_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				blog_message_reportVO = new blog_message_reportVO();
				blog_message_reportVO.setMem_id(rs.getString("MEM_ID"));
				blog_message_reportVO.setMessage_id(rs.getString("MESSAGE_ID"));
				blog_message_reportVO.setBmr_reason(rs.getString("BMR_REASON"));
				blog_message_reportVO.setBmr_time(rs.getTimestamp("BMR_TIME"));
				blog_message_reportVO.setBmr_status(rs.getInt("BMR_STATUS"));
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
		return blog_message_reportVO;
	}

	@Override
	public List<blog_message_reportVO> getBlogMsgReport_ByStatus(Integer bmr_status) {

		List<blog_message_reportVO> list = new ArrayList<blog_message_reportVO>();
		blog_message_reportVO blog_message_reportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GETALL_BYSTATUS_STMT);
			pstmt.setInt(1, bmr_status);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_message_reportVO = new blog_message_reportVO();
				blog_message_reportVO.setMem_id(rs.getString("MEM_ID"));
				blog_message_reportVO.setMessage_id(rs.getString("MESSAGE_ID"));
				blog_message_reportVO.setBmr_reason(rs.getString("BMR_REASON"));
				blog_message_reportVO.setBmr_time(rs.getTimestamp("BMR_TIME"));
				blog_message_reportVO.setBmr_status(rs.getInt("BMR_STATUS"));
				list.add(blog_message_reportVO);
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
