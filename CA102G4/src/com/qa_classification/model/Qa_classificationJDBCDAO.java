package com.qa_classification.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.qa_classification.model.Qa_classificationVO;

public class Qa_classificationJDBCDAO implements Qa_classificationDAO_interface{
	
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String	USER = "CA102G4";
	private static final String	PASSWORD = "12345678";
	
	private static final String INSERT_STMT = "Insert into QA_CLASSIFICATION (LIST_ID,QUESTION_ID)"
			+ " values (?,?)";
	
	private static final String UPDATE_STMT = "UPDATE QA_CLASSIFICATION SET QUESTION_ID = ? WHERE LIST_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM QA_CLASSIFICATION WHERE LIST_ID = ?";
	private static final String FIND_BY_PK1 = "SELECT * FROM QA_CLASSIFICATION WHERE LIST_ID = ?";
	private static final String FIND_BY_PK2 = "SELECT * FROM QA_CLASSIFICATION WHERE QUESTION_ID = ?";
	private static final String GET_ALL = "SELECT * FROM QA_CLASSIFICATION";
	private Statement ds;
	
	@Override
	public int insert(Qa_classificationVO Qa_classificationVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, Qa_classificationVO.getList_id());
			pstmt.setString(2, Qa_classificationVO.getQuestion_id());

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
	public int update(Qa_classificationVO Qa_classificationVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
			

			pstmt.setString(1, Qa_classificationVO.getQuestion_id());
			pstmt.setString(2, Qa_classificationVO.getList_id());

			
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
			
			pstmt.setString(1,question_id);

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
	public List<Qa_classificationVO> findByQuestion_id(String question_id) {
		List<Qa_classificationVO> list = new ArrayList<>();
		Qa_classificationVO Qa_classification = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK2);
			
			pstmt.setString(1, question_id);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Qa_classification = new Qa_classificationVO();
				System.out.println(rs.getString("list_id"));
				Qa_classification.setList_id(rs.getString("list_id"));
				Qa_classification.setQuestion_id(rs.getString("question_id"));
				list.add(Qa_classification);
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
	public List<Qa_classificationVO> findByList_id(String list_id) {
		List<Qa_classificationVO> list = new ArrayList<>();
		Qa_classificationVO Qa_classification = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK1);
			
			pstmt.setString(1, list_id);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Qa_classification = new Qa_classificationVO();
				System.out.println(rs.getString("question_id"));
				Qa_classification.setList_id(rs.getString("list_id"));
				Qa_classification.setQuestion_id(rs.getString("question_id"));
				list.add(Qa_classification);
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
	public List<Qa_classificationVO> getAll() {
		List<Qa_classificationVO> list = new ArrayList<Qa_classificationVO>();
		Qa_classificationVO Qa_classification = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Qa_classification = new Qa_classificationVO();
				Qa_classification.setQuestion_id(rs.getString("question_id"));
				Qa_classification.setList_id(rs.getString("list_id"));
				list.add(Qa_classification);
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
//		Qa_classificationJDBCDAO dao = new Qa_classificationJDBCDAO();
		
		//新增
		
//		Qa_classificationVO qa_classification1 = new Qa_classificationVO();
//		
//		qa_classification1.setList_id("LI000000004");
//		qa_classification1.setQuestion_id("QU000000001");
//		
//		dao.insert(qa_classification1);
				
		// 修改
			
//		Qa_classificationVO qa_classification2 = new Qa_classificationVO();
//		qa_classification2.setList_id("LI000000002");
//		qa_classification2.setQuestion_id("QU000000001");
//     	dao.update(qa_classification2);

     	// 刪除
//		dao.delete("LI000000004");
				
		// 查詢
				
//		Qa_classificationVO qa_classification3 = new Qa_classificationVO();
//		qa_classification3 = dao.findByPrimaryKey("LI000000001");
//		System.out.println(qa_classification3.getList_id());
//		System.out.println(qa_classification3.getQuestion_id());
//
//
//		// 查詢
//		List<Qa_classificationVO> list = new ArrayList<Qa_classificationVO>();
//		list = dao.getAll();
//		for (Qa_classificationVO qa_classification4 : list){
//			System.out.println(qa_classification4.getQuestion_id());
//		}
		
		
	}
}
