package com.blog_message.model;

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




public class blog_messageDAO implements blog_messageDAO_interface {
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	// 新增一個部落格留言
	private static final String INSERT_STMT = "INSERT INTO BLOG_MESSAGE(MESSAGE_ID,BLOG_ID,MEM_ID,BLOG_MESSAGE,BM_TIME,BM_STATUS) VALUES('BM'||LPAD(TO_CHAR(BM_SEQ.NEXTVAL), 6, '0'),?,?,?,SYSDATE,0)";
	// 修改部落格留言內容
	private static final String UPDATE_STMT = "UPDATE BLOG_MESSAGE SET BLOG_MESSAGE = ? WHERE MESSAGE_ID = ?";
	// 刪除部落格留言
	private static final String DELETE_STMT = "DELETE FROM BLOG_MESSAGE WHERE MESSAGE_ID = ?";
	// 取得該部落格留言
	private static final String GET_ALL_BY_BLOGID = "SELECT * FROM BLOG_MESSAGE WHERE BLOG_ID = ? AND BM_STATUS = 0 ORDER BY BM_TIME";
	// 修改部落格留言狀態-隱藏或顯示
	private static final String UPDATE_STATUS_STMT = "UPDATE BLOG_MESSAGE SET BM_STATUS = ? WHERE MESSAGE_ID = ? AND MEM_ID = ?";
	// 取得全部
	private static final String GET_ALL_STMT = "SELECT * FROM BLOG_MESSAGE";
	// 修改部落格留言狀態-隱藏或顯示 FOR BACKEND
	private static final String UPDATE_STATUS_FOR_BACKEND_STMT = "UPDATE BLOG_MESSAGE SET BM_STATUS = ? WHERE MESSAGE_ID = ?";

	@Override
	public int insert(blog_messageVO blog_messageVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			pstmt.setString(1, blog_messageVO.getBlog_id());
			pstmt.setString(2, blog_messageVO.getMem_id());
			pstmt.setString(3, blog_messageVO.getBlog_message());
			updateCount = pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return updateCount;
	}

	@Override
	public int update(blog_messageVO blog_messageVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);

			pstmt.setString(1, blog_messageVO.getBlog_message());
			pstmt.setString(2, blog_messageVO.getMessage_id());

			updateCount = pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return updateCount;
	}

	@Override
	public int delete(String message_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);

			pstmt.setString(1, message_id);

			updateCount = pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return updateCount;
	}
	public List<blog_messageVO> findByBlogId(String blog_id) {
		List<blog_messageVO> list = new ArrayList<blog_messageVO>();
		blog_messageVO blog_messageVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_BY_BLOGID);

			pstmt.setString(1, blog_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_messageVO = new blog_messageVO();
				blog_messageVO.setMessage_id(rs.getString("MESSAGE_ID"));
				blog_messageVO.setBlog_id(rs.getString("BLOG_ID"));
				blog_messageVO.setMem_id(rs.getString("MEM_ID"));
				blog_messageVO.setBlog_message(rs.getString("BLOG_MESSAGE"));
				blog_messageVO.setBm_time(rs.getTimestamp("BM_TIME"));
				list.add(blog_messageVO);
			}
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	
	@Override
	public int updateStatus(String message_id,String mem_id,Integer bm_status) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STATUS_STMT);

			pstmt.setInt(1, bm_status);
			pstmt.setString(2, message_id);
			pstmt.setString(3, mem_id);

			updateCount = pstmt.executeUpdate();
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return updateCount;
	}
	
	@Override
	public int updateStatusForBackEnd(String message_id,Integer bm_status) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STATUS_FOR_BACKEND_STMT);

			pstmt.setInt(1, bm_status);
			pstmt.setString(2, message_id);

			updateCount = pstmt.executeUpdate();
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return updateCount;
	}
	
	@Override
	public List<blog_messageVO> getAll() {
		List<blog_messageVO> list = new ArrayList<blog_messageVO>();
		blog_messageVO blog_messageVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_messageVO = new blog_messageVO();
				blog_messageVO.setMessage_id(rs.getString("MESSAGE_ID"));
				blog_messageVO.setBlog_id(rs.getString("BLOG_ID"));
				blog_messageVO.setMem_id(rs.getString("MEM_ID"));
				blog_messageVO.setBlog_message(rs.getString("BLOG_MESSAGE"));
				blog_messageVO.setBm_time(rs.getTimestamp("BM_TIME"));
				blog_messageVO.setBm_status(rs.getInt("BM_STATUS"));
				list.add(blog_messageVO);
			}
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	
}
