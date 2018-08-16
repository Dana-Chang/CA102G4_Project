package com.qa_reply.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.question.model.QuestionVO;

public class Qa_replyDAO implements Qa_replyDAO_interface{
	
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	private static final String INSERT_STMT = "INSERT INTO QA_REPLY (REPLY_ID,QUESTION_ID,MEM_ID,REPLY_CONTENT,REPLY_DATE,R_STATE)"
			 +  "values ('RE'||LPAD(TO_CHAR(QA_REPLY_SEQ.NEXTVAL), 9, '0'),?,?,?,?,0)";
		
	private static final String UPDATE_STMT = "UPDATE QA_REPLY SET QUESTION_ID=?, MEM_ID=?, REPLY_CONTENT=? ,REPLY_DATE=? R_STATE=? WHERE REPLY_ID = ?";
	
	private static final String UPDATE = "UPDATE QA_REPLY SET R_STATE=? WHERE REPLY_ID = ?";
			
	private static final String DELETE_STMT = "DELETE FROM QA_REPLY WHERE REPLY_ID = ?";
	private static final String FIND_BY_PK = "SELECT * FROM QA_REPLY WHERE REPLY_ID = ?";
	private static final String FIND_BY_PK1 = "SELECT * FROM QA_REPLY WHERE QUESTION_ID = ? AND R_STATE =0";
	private static final String GET_ALL = "SELECT * FROM QA_REPLY";
	
	private static final String FIND_BY_STATE = "SELECT * FROM QA_REPLY WHERE R_STATE =0";
		@Override
		public int insert(Qa_replyVO Qa_replyVO) {
			int updateCount = 0;
			Connection con = null;
			PreparedStatement pstmt = null;
			System.out.println("Connecting to database successfully! (連線成功！)");

			try {

				
				con = ds.getConnection();
				pstmt = con.prepareStatement(INSERT_STMT);
				
				pstmt.setString(1, Qa_replyVO.getQuestion_id());
				pstmt.setString(2, Qa_replyVO.getMem_id());
				pstmt.setString(3, Qa_replyVO.getReply_content());
				pstmt.setDate(4, Qa_replyVO.getReply_date());
				
				updateCount = pstmt.executeUpdate();


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
				
				
				con = ds.getConnection();
				pstmt = con.prepareStatement(UPDATE_STMT);
				
				pstmt.setString(1, Qa_replyVO.getQuestion_id());
				pstmt.setString(2, Qa_replyVO.getMem_id());
				pstmt.setString(3, Qa_replyVO.getReply_content());
				pstmt.setDate(4, Qa_replyVO.getReply_date());
				pstmt.setInt(5, Qa_replyVO.getR_state());
				pstmt.setString(6, Qa_replyVO.getReply_id());
				
				updateCount = pstmt.executeUpdate();


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
				
				
				con = ds.getConnection();
				pstmt = con.prepareStatement(DELETE_STMT);
				
				pstmt.setString(1, reply_id);

				updateCount = pstmt.executeUpdate();

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

				
				con = ds.getConnection();
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
				qa_reply.setR_state(rs.getInt("r_state"));

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
		
				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ALL);
				
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					qa_reply = new Qa_replyVO();
					qa_reply.setReply_id(rs.getString("reply_id"));
					qa_reply.setMem_id(rs.getString("mem_id"));
					qa_reply.setReply_content(rs.getString("reply_content"));
					qa_reply.setReply_date(rs.getDate("reply_date"));
					qa_reply.setQuestion_id(rs.getString("question_id"));
					qa_reply.setR_state(rs.getInt("r_state"));
					list.add(qa_reply);
				}
				
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
		
		
		@Override
		public List<Qa_replyVO> findByPrimaryKey1(String question_id) {
			Qa_replyVO qa_reply = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			List<Qa_replyVO> list = null;
			try {

				
				con = ds.getConnection();
				pstmt = con.prepareStatement(FIND_BY_PK1);
				
				pstmt.setString(1, question_id);
				
				rs = pstmt.executeQuery();
				list = new ArrayList<>();
				while (rs.next()) {
					qa_reply = new Qa_replyVO();
					qa_reply.setReply_id(rs.getString("reply_id"));
					qa_reply.setMem_id(rs.getString("mem_id"));
					qa_reply.setReply_content(rs.getString("reply_content"));
					qa_reply.setReply_date(rs.getDate("reply_date"));
					qa_reply.setQuestion_id(rs.getString("question_id"));
					qa_reply.setR_state(rs.getInt("r_state"));
					list.add(qa_reply);
				}

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
		
		@Override
		public List<Qa_replyVO> find_by_State() {
			List<Qa_replyVO> list = new ArrayList<Qa_replyVO>();
			Qa_replyVO qa_reply = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
		
				con = ds.getConnection();
				pstmt = con.prepareStatement(FIND_BY_STATE);
				
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					qa_reply = new Qa_replyVO();
					qa_reply.setReply_id(rs.getString("reply_id"));
					qa_reply.setMem_id(rs.getString("mem_id"));
					qa_reply.setReply_content(rs.getString("reply_content"));
					qa_reply.setReply_date(rs.getDate("reply_date"));
					qa_reply.setQuestion_id(rs.getString("question_id"));
					qa_reply.setR_state(rs.getInt("r_state"));
					list.add(qa_reply);
				}
				
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
		
		@Override
		public int updateR(Qa_replyVO Qa_replyVO) {
			int updateCount = 0;
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				

				con = ds.getConnection();
				pstmt = con.prepareStatement(UPDATE);
				
				
				pstmt.setInt(1, Qa_replyVO.getR_state());
				pstmt.setString(2, Qa_replyVO.getReply_id());

				
				updateCount = pstmt.executeUpdate();

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
	}
