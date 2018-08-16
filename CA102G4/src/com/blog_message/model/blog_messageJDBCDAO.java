package com.blog_message.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class blog_messageJDBCDAO implements blog_messageDAO_interface {
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";
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
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, blog_messageVO.getBlog_id());
			pstmt.setString(2, blog_messageVO.getMem_id());
			pstmt.setString(3, blog_messageVO.getBlog_message());

			updateCount = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);

			pstmt.setString(1, blog_messageVO.getBlog_message());
			pstmt.setString(2, blog_messageVO.getMessage_id());

			updateCount = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);

			pstmt.setString(1, message_id);

			updateCount = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_BY_BLOGID);

			pstmt.setString(1, blog_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				blog_messageVO = new blog_messageVO();
				blog_messageVO.setMessage_id(rs.getString("MESSAGE_ID"));
				blog_messageVO.setBlog_id(rs.getString("BLOG_ID"));
				blog_messageVO.setMem_id(rs.getString("MEM_ID"));
				blog_messageVO.setBlog_message(rs.getString("BLOG_MESSAGE"));
				list.add(blog_messageVO);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STATUS_STMT);

			pstmt.setInt(1, bm_status);
			pstmt.setString(2, message_id);
			pstmt.setString(3, mem_id);


			updateCount = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STATUS_FOR_BACKEND_STMT);

			pstmt.setInt(1, bm_status);
			pstmt.setString(2, message_id);

			updateCount = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
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
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
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
	
	public static void main(String[] args) {

		blog_messageJDBCDAO dao = new blog_messageJDBCDAO();

		// 新增一個部落格留言
//		blog_messageVO blog_messageVO = new blog_messageVO();
//		blog_messageVO.setBlog_id("B000001");
//		blog_messageVO.setMem_id("M000009");
//		blog_messageVO.setBlog_message("新增一個部落格留言內容新增一個部落格留言內容新增一個部落格留言內容新增一個部落格留言內容");
//		int updateCount_insert = dao.insert(blog_messageVO);
//		System.out.println(updateCount_insert);

		// 修改部落格留言內容
//		blog_messageVO blog_messageVO2 = new blog_messageVO();
//		blog_messageVO2.setBlog_message("修改一個部落格留言內容修改一個部落格留言內容修改一個部落格留言內容修改一個部落格留言內容");
//		blog_messageVO2.setMessage_id("BM000001");
//		int updateCount_update = dao.update(blog_messageVO2);
//		System.out.println(updateCount_update);

		// 刪除部落格留言
//		int updateCount_update = dao.delete("BM000006");
//		System.out.println(updateCount_update);
		
		// 更新部落格留言狀態--顯示或隱藏
//		int updateCount_status = dao.updateStatus("BM000023", "M000009",1);
//		System.out.println(updateCount_status);
		
		int updateCount_status = dao.updateStatusForBackEnd("BM000003",1);
		System.out.println(updateCount_status);
	}
}
