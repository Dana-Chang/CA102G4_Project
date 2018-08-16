package com.qa_reply.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qa_reply.model.Qa_replyVO;

public class Qa_replyJDBCDAO implements Qa_replyDAO_interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String	USER = "CA102G4";
	private static final String	PASSWORD = "12345678";
	
	private static final String INSERT_STMT = "INSERT INTO QA_REPLY (REPLY_ID,QUESTION_ID,MEM_ID,REPLY_CONTENT,REPLY_DATE)"
		 +  "values ('RE'||LPAD(TO_CHAR(QA_REPLY_SEQ.NEXTVAL), 9, '0'),?,?,?,?)";
	
	private static final String UPDATE_STMT = "UPDATE QA_REPLY SET QUESTION_ID=?, MEM_ID=?, REPLY_CONTENT=? ,REPLY_DATE=? WHERE REPLY_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM QA_REPLY WHERE REPLY_ID = ?";
	private static final String FIND_BY_PK = "SELECT * FROM QA_REPLY WHERE REPLY_ID = ?";
	private static final String GET_ALL = "SELECT * FROM QA_REPLY";
	@Override
	public int insert(Qa_replyVO Qa_replyVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, Qa_replyVO.getQuestion_id());
			pstmt.setString(2, Qa_replyVO.getMem_id());
			pstmt.setString(3, Qa_replyVO.getReply_content());
			pstmt.setDate(4, Qa_replyVO.getReply_date());
			updateCount = pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException ce) {
			throw new RuntimeException("Couldn't load database driver. " + ce.getMessage());
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
		return updateCount;

	}
	@Override
	public int update(Qa_replyVO Qa_replyVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
			
			pstmt.setString(1, Qa_replyVO.getQuestion_id());
			pstmt.setString(2, Qa_replyVO.getMem_id());
			pstmt.setString(3, Qa_replyVO.getReply_content());
			pstmt.setDate(4, Qa_replyVO.getReply_date());
			pstmt.setString(5, Qa_replyVO.getReply_id());
			
			updateCount = pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException ce) {
			throw new RuntimeException("Couldn't load database driver. " + ce.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		}
		finally {
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
	public int delete(String reply_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1, reply_id);

			updateCount = pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException ce) {
			throw new RuntimeException("Couldn't load database driver. " + ce.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		}
		finally {
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
	public Qa_replyVO findByPrimaryKey(String reply_id) {
		Qa_replyVO qa_reply = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_PK);
			
			pstmt.setString(1, reply_id);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			qa_reply = new Qa_replyVO();
			System.out.println(rs.getString("reply_id"));
			qa_reply.setReply_id(rs.getString("reply_id"));
			qa_reply.setMem_id(rs.getString("mem_id"));
			qa_reply.setReply_content(rs.getString("reply_content"));
			qa_reply.setReply_date(rs.getDate("reply_date"));
			qa_reply.setQuestion_id(rs.getString("question_id"));

			
			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return qa_reply;

	}
	@Override
	public List<Qa_replyVO> getAll() {
		List<Qa_replyVO> list = new ArrayList<Qa_replyVO>();
		Qa_replyVO qa_reply = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				qa_reply = new Qa_replyVO();
				qa_reply.setReply_id(rs.getString("reply_id"));
				qa_reply.setMem_id(rs.getString("mem_id"));
				qa_reply.setReply_content(rs.getString("reply_content"));
				qa_reply.setReply_date(rs.getDate("reply_date"));
				qa_reply.setQuestion_id(rs.getString("question_id"));
				list.add(qa_reply);
			}
			
			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public static void main(String[] args) {
		Qa_replyJDBCDAO	dao = new Qa_replyJDBCDAO();
		
		//新增
		
//		Qa_replyVO qa_reply1 = new Qa_replyVO();
//		qa_reply1.setQuestion_id("QU000000003");
//		qa_reply1.setMem_id("M000003");
//		qa_reply1.setReply_content("今天去玩不會下雨");
//		qa_reply1.setReply_date(java.sql.Date.valueOf("2018-11-17"));
//		
//		dao.insert(qa_reply1);
		
		// 修改
	
//		Qa_replyVO qa_reply2 = new Qa_replyVO();
//		qa_reply2.setQuestion_id("QU000000003");
//		qa_reply2.setMem_id("M000003");
//		qa_reply2.setReply_content("今天去應該會放晴");
//		qa_reply2.setReply_date(java.sql.Date.valueOf("2017-1-17"));
//		qa_reply2.setReply_id("RE000000004");
//     	dao.update(qa_reply2);
		
		
		// 刪除
//		dao.delete("RE000000004");
		
		// 查詢
		
		Qa_replyVO qa_reply3 = new Qa_replyVO();
		qa_reply3 = dao.findByPrimaryKey("RE000000001");
		System.out.println(qa_reply3.getReply_id());
		System.out.println(qa_reply3.getMem_id());
		System.out.println(qa_reply3.getReply_content());
		System.out.println(qa_reply3.getReply_date());

		// 查詢
		List<Qa_replyVO> list = new ArrayList<Qa_replyVO>();
		list = dao.getAll();
		for (Qa_replyVO qa_reply4 : list){
			System.out.println(qa_reply4.getReply_id());
		}
		
	}
	@Override
	public List<Qa_replyVO> findByPrimaryKey1(String question_id) {
		return null;
	}
	@Override
	public List<Qa_replyVO> find_by_State() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int updateR(Qa_replyVO Qa_replyVO) {
		// TODO Auto-generated method stub
		return 0;
	}

}

