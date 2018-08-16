package com.qa_list.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.qa_list.model.Qa_listVO;

public class Qa_listJDBCDAO implements Qa_listDAO_interface{
	
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String	USER = "CA102G4";
	private static final String	PASSWORD = "12345678";
	
	private static final String INSERT_STMT = "INSERT INTO QA_LIST (LIST_ID,LIST_NAME)"
			+ " values (?,?)";
	
	private static final String UPDATE_STMT = "UPDATE QA_LIST SET LIST_NAME= ? WHERE LIST_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM QA_LIST WHERE LIST_ID = ?";
	private static final String FIND_BY_PK = "SELECT * FROM QA_LIST WHERE LIST_ID = ?";
	private static final String GET_ALL = "SELECT * FROM QA_LIST";
	@Override
	public int insert(Qa_listVO Qa_listVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, Qa_listVO.getList_id());
			pstmt.setString(2, Qa_listVO.getList_name());
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
	public int update(Qa_listVO Qa_listVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
			
			pstmt.setString(1, Qa_listVO.getList_name());
			pstmt.setString(2, Qa_listVO.getList_id());


			
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
	public int delete(String list_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1,list_id);

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
	public Qa_listVO findByPrimaryKey(String list_id) {
		Qa_listVO Qa_list = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_PK);
			
			pstmt.setString(1, list_id);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			Qa_list = new Qa_listVO();
			System.out.println(rs.getString("list_id"));
			Qa_list.setList_id(rs.getString("list_id"));
			Qa_list.setList_name(rs.getString("list_name"));


			
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
		return Qa_list;

	}
	@Override
	public List<Qa_listVO> getAll() {
		List<Qa_listVO> list = new ArrayList<Qa_listVO>();
		Qa_listVO Qa_list = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Qa_list = new Qa_listVO();
				Qa_list.setList_id(rs.getString("list_id"));
				Qa_list.setList_name(rs.getString("list_name"));
				list.add(Qa_list);
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
		Qa_listJDBCDAO dao = new Qa_listJDBCDAO();
		//新增
				
//			Qa_listVO qa_list1 = new Qa_listVO();
//			qa_list1.setList_id("LI000000005");	
//			qa_list1.setList_name("台灣");	
//			dao.insert(qa_list1);
						
		// 修改
					
//			Qa_listVO qa_list2 = new Qa_listVO();
//			qa_list2.setList_name("日本");
//			qa_list2.setList_id("LI000000005");
//		    dao.update(qa_list2);
					
		// 刪除
//			dao.delete("LI000000005");
						
		// 查詢
						
			Qa_listVO qa_list3 = new Qa_listVO();
			qa_list3 = dao.findByPrimaryKey("LI000000001");
			System.out.println(qa_list3.getList_id());
			System.out.println(qa_list3.getList_name());


		// 查詢
			
			List<Qa_listVO> list = new ArrayList<Qa_listVO>();
			list = dao.getAll();
			for (Qa_listVO qa_list4 : list){
				System.out.println(qa_list4.getList_id());
			}
				
				
	}
}
