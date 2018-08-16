package com.question.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.qa_classification.model.Qa_classificationVO;

public class QuestionJNDI implements QuestionDAO_interface{
	
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
		private static final String INSERT_STMT = "Insert into QUESTION (QUESTION_ID, MEM_ID, QUESTION_CONTENT, BUILD_DATE,Q_STATE)"
			 +  "values ('QU'||LPAD(to_char(QUESTION_SEQ.NEXTVAL), 9, '0'),?,?,?,0)";
	
		private static final String INSERT_STMT1 = "Insert into QA_CLASSIFICATION (LIST_ID,QUESTION_ID)"
			+ " values (?,?)";
		
		private static final String UPDATE_STMT = "UPDATE QUESTION SET MEM_ID=?, QUESTION_CONTENT=?, BUILD_DATE=? Q_STATE=? WHERE QUESTION_ID = ?";
		
		private static final String UPDATE = "UPDATE QUESTION SET  Q_STATE=? WHERE QUESTION_ID = ?";
		
		private static final String DELETE_STMT = "DELETE FROM QUESTION WHERE QUESTION_ID=? ";
		private static final String FIND_BY_PK = "SELECT * FROM QUESTION WHERE QUESTION_ID = ?";
		private static final String GET_ALL = "SELECT * FROM QUESTION";
		
		private static final String FIND_BY_STATE = "SELECT * FROM QUESTION WHERE Q_STATE= 0";
		
		// 世銘打的
		private static final String FIND_BY_KEYWORD_STMT = "SELECT * FROM QUESTION WHERE UPPER(QUESTION_CONTENT) LIKE UPPER(?)";

		@Override
		public int insert(QuestionVO questionVO) {
			int updateCount = 0;
			Connection con = null;
			PreparedStatement pstmt = null;
			System.out.println("Connecting to database successfully! (連線成功！)");

			try {


				con = ds.getConnection();
				pstmt = con.prepareStatement(INSERT_STMT);

				pstmt.setString(1, questionVO.getMem_id());
				pstmt.setString(2, questionVO.getQuestion_content());
				pstmt.setDate(3, questionVO.getBuild_date());
				pstmt.setInt(4, questionVO.getQ_state());
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
		public int update(QuestionVO questionVO) {
			int updateCount = 0;
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				

				con = ds.getConnection();
				pstmt = con.prepareStatement(UPDATE_STMT);
				
				
				pstmt.setString(1, questionVO.getMem_id());
				pstmt.setString(2,questionVO.getQuestion_content());
				pstmt.setDate(3, questionVO.getBuild_date());
				pstmt.setInt(4, questionVO.getQ_state());
				pstmt.setString(5, questionVO.getQuestion_id());

				
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
		public int delete(String question_id) {
			int updateCount = 0;
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				

				con = ds.getConnection();
				pstmt = con.prepareStatement(DELETE_STMT);
				
				pstmt.setString(1, question_id);
				
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
		public QuestionVO findByPrimaryKey(String question_id) {
			QuestionVO question = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {


				con = ds.getConnection();
				pstmt = con.prepareStatement(FIND_BY_PK);
				
				pstmt.setString(1, question_id);
				
				rs = pstmt.executeQuery();
				
				rs.next();
				question = new QuestionVO();
				System.out.println(rs.getString("question_id"));
				question.setQuestion_id(rs.getString("question_id"));
				question.setMem_id(rs.getString("mem_id"));
				question.setQuestion_content(rs.getString("question_content"));
				question.setBuild_date(rs.getDate("build_date"));
				question.setQ_state(rs.getInt("q_state"));

				
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
			return question;

		}
		@Override
		public List<QuestionVO> getAll() {
			List<QuestionVO> list = new ArrayList<QuestionVO>();
			QuestionVO question = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {

				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ALL);
				
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					question = new QuestionVO();
					question.setQuestion_id(rs.getString("question_id"));
					question.setMem_id(rs.getString("mem_id"));
					question.setQuestion_content(rs.getString("question_content"));
					question.setBuild_date(rs.getDate("build_date"));
					question.setQ_state(rs.getInt("q_state"));
					list.add(question);
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
		
		public void insertQ(QuestionVO questionVO,List<Qa_classificationVO> list) {
			int updateCount = 0;

			Connection con = null;
			PreparedStatement pstmt = null;

			try {

				con = ds.getConnection();

				// 1●設定於 pstm.executeUpdate()之前
				con.setAutoCommit(false);

				// 先新增問題
				String[] colname= {"QUESTION_ID"};
				pstmt = con.prepareStatement(INSERT_STMT,colname);
				
				pstmt.setString(1, questionVO.getMem_id());
				pstmt.setString(2, questionVO.getQuestion_content());
				pstmt.setDate(3, questionVO.getBuild_date());
				pstmt.setInt(4, questionVO.getQ_state());
				
				updateCount = pstmt.executeUpdate();
				ResultSet rs = pstmt.getGeneratedKeys();
				String questionVOkey = null;
				while(rs.next()){
					questionVOkey = rs.getString(1);
				}
				System.out.println(questionVOkey);
				// 再新增分類清單
				for(Qa_classificationVO qa_classificationVO :list) {
					pstmt = con.prepareStatement(INSERT_STMT1);
					pstmt.setString(1,qa_classificationVO.getList_id());
					pstmt.setString(2,questionVOkey);					
					pstmt.executeUpdate();
				}
				

				// 2●設定於 pstm.executeUpdate()之後
				con.commit();
				con.setAutoCommit(true);

				
				// Handle any SQL errors
			} catch (SQLException se) {
				if (con != null) {
					try {
						// 3●設定於當有exception發生時之catch區塊內
						con.rollback();
					} catch (SQLException excep) {
						throw new RuntimeException("rollback error occured. "
								+ excep.getMessage());
					}
				}
				throw new RuntimeException("A database error occured. "
						+ se.getMessage());
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
		public List<QuestionVO> find_by_State() {
			List<QuestionVO> list = new ArrayList<QuestionVO>();
			QuestionVO question = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {

				con = ds.getConnection();
				pstmt = con.prepareStatement(FIND_BY_STATE);
				
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					question = new QuestionVO();
					question.setQuestion_id(rs.getString("question_id"));
					question.setMem_id(rs.getString("mem_id"));
					question.setQuestion_content(rs.getString("question_content"));
					question.setBuild_date(rs.getDate("build_date"));
					question.setQ_state(rs.getInt("q_state"));
					list.add(question);
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
		public int updateQ(QuestionVO questionVO) {
			int updateCount = 0;
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				

				con = ds.getConnection();
				pstmt = con.prepareStatement(UPDATE);
				
				
				pstmt.setInt(1, questionVO.getQ_state());
				pstmt.setString(2, questionVO.getQuestion_id());

				
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
		
		// 世銘打的
		@Override
		public List<QuestionVO> findByKeyword(String keyword) {
			List<QuestionVO> list = new ArrayList<QuestionVO>();
			QuestionVO question = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {

				con = ds.getConnection();
				pstmt = con.prepareStatement(FIND_BY_KEYWORD_STMT);
				pstmt.setString(1, "%"+keyword+"%");
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					question = new QuestionVO();
					question.setQuestion_id(rs.getString("question_id"));
					question.setMem_id(rs.getString("mem_id"));
					question.setQuestion_content(rs.getString("question_content"));
					question.setBuild_date(rs.getDate("build_date"));
					question.setQ_state(rs.getInt("q_state"));
					list.add(question);
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
	}
