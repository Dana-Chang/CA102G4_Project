package com.question.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qa_classification.model.Qa_classificationVO;
import com.question.model.QuestionVO;

public class QuestionJDBCDAO implements QuestionDAO_interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String	USER = "CA102G4";
	private static final String	PASSWORD = "12345678";
	
	private static final String INSERT_STMT = "Insert into QUESTION (QUESTION_ID, MEM_ID, QUESTION_CONTENT, BUILD_DATE)"
		 +  "values ('QU'||LPAD(to_char(QUESTION_SEQ.NEXTVAL), 9, '0'),?,?,?)";
	
	private static final String UPDATE_STMT = "UPDATE QUESTION SET MEM_ID=?, QUESTION_CONTENT=?, BUILD_DATE=? WHERE QUESTION_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM QUESTION WHERE QUESTION_ID AND MEM_ID = ?";
	private static final String FIND_BY_PK = "SELECT * FROM QUESTION WHERE QUESTION_ID = ?";
	private static final String GET_ALL = "SELECT * FROM QUESTION";
	
	// 世銘打的
	private static final String FIND_BY_KEYWORD_STMT = "SELECT * FROM QUESTION WHERE UPPER(QUESTION_CONTENT) LIKE UPPER(?)";

	@Override
	public int insert(QuestionVO questionVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, questionVO.getMem_id());
			pstmt.setString(2, questionVO.getQuestion_content());
			pstmt.setDate(3, questionVO.getBuild_date());
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
	public int update(QuestionVO questionVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
			
			
			pstmt.setString(1, questionVO.getMem_id());
			pstmt.setString(2,questionVO.getQuestion_content());
			pstmt.setDate(3, questionVO.getBuild_date());
			pstmt.setString(4, questionVO.getQuestion_id());

			
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
	public int delete(String question_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1, question_id);

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
	public QuestionVO findByPrimaryKey(String question_id) {
		QuestionVO question = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
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

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				question = new QuestionVO();
				question.setQuestion_id(rs.getString("question_id"));
				question.setMem_id(rs.getString("mem_id"));
				question.setQuestion_content(rs.getString("question_content"));
				question.setBuild_date(rs.getDate("build_date"));
				list.add(question);
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
	
	@Override
	public void insertQ(QuestionVO questionVO, List<Qa_classificationVO> list) {
		
	}
	public static void main(String[] args) {
		QuestionJDBCDAO	dao = new QuestionJDBCDAO();
		
		//新增
		
		QuestionVO question1 = new QuestionVO();
		question1.setMem_id("M000004");
		question1.setQuestion_content("請問在JR美瑛車站搭乘JR普通車和JR富良野・美瑛ノロッコ号是同一條軌道嗎?若兩班列車班距只有4分鐘，來得及轉乘嗎?");
		question1.setBuild_date(java.sql.Date.valueOf("2018-11-17"));

		dao.insert(question1);
		
		// 修改
	
//		QuestionVO question2 = new QuestionVO();
//		question2.setMem_id("M000003");
//		question2.setQuestion_content("請問在JR美瑛車站搭乘JR普通車和JR富良野・美瑛ノロッコ号是不同一條軌道嗎?");
//		question2.setBuild_date(java.sql.Date.valueOf("2018-12-17"));
//		question2.setQuestion_id("QU000000003");
//		
//     	dao.update(question2);
		
		
     // 刪除
//		dao.delete("QU000000027");
		
     // 查詢
		
//		QuestionVO question3 = new QuestionVO();
//		question3 = dao.findByPrimaryKey("QU000000001");
//		System.out.println(question3.getQuestion_id());
//		System.out.println(question3.getQuestion_content());
//		System.out.println(question3.getBuild_date());

     // 查詢
//		List<QuestionVO> list = new ArrayList<QuestionVO>();
//		list = dao.getAll();
//		for (QuestionVO question4 : list){
//			System.out.println(question4.getQuestion_id());
//		}
		
	}
	@Override
	public List<QuestionVO> find_by_State() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int updateQ(QuestionVO questionVO) {
		// TODO Auto-generated method stub
		return 0;
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

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_KEYWORD_STMT);
			pstmt.setString(1, "%"+keyword+"%");
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				question = new QuestionVO();
				question.setQuestion_id(rs.getString("question_id"));
				question.setMem_id(rs.getString("mem_id"));
				question.setQuestion_content(rs.getString("question_content"));
				question.setBuild_date(rs.getDate("build_date"));
				list.add(question);
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

}

