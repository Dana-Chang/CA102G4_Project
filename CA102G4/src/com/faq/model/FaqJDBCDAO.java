package com.faq.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.faq.model.FaqVO;

public class FaqJDBCDAO implements FaqDAO_interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String	USER = "CA102G4";
	private static final String	PASSWORD = "12345678";
	
	private static final String INSERT_STMT = "Insert into FAQ (FAQ_ID,FAQ_CONTENT)"
			+ " values ('FA'||LPAD(to_char(FAQ_SEQ.NEXTVAL), 9, '0'),?)";
	
	private static final String UPDATE_STMT = "UPDATE FAQ SET FAQ_CONTENT=? WHERE FAQ_ID = ?";
	
	private static final String DELETE_STMT = "DELETE FROM FAQ WHERE FAQ_ID = ?";
	private static final String FIND_BY_PK = "SELECT * FROM FAQ WHERE FAQ_ID = ?";
	private static final String GET_ALL = "SELECT * FROM FAQ";
	@Override
	public int insert(FaqVO FaqVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("Connecting to database successfully! (連線成功！)");

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, FaqVO.getFaq_content());
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
	public int update(FaqVO FaqVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
			
			
			pstmt.setString(1, FaqVO.getFaq_content());
			pstmt.setString(2, FaqVO.getFaq_id());

			
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
	public int delete(String Faq_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
			
			pstmt.setString(1,Faq_id);

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
	public FaqVO findByPrimaryKey(String Faq_id) {
		FaqVO Faq = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_PK);
			
			pstmt.setString(1, Faq_id);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			Faq = new FaqVO();
			System.out.println(rs.getString("Faq_id"));
			Faq.setFaq_id(rs.getString("Faq_id"));
			Faq.setFaq_content(rs.getString("Faq_content"));


			
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
		return Faq;

	}

	@Override
	public List<FaqVO> getAll() {
		List<FaqVO> list = new ArrayList<FaqVO>();
		FaqVO Faq = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Faq = new FaqVO();
				Faq.setFaq_id(rs.getString("Faq_id"));
				Faq.setFaq_content(rs.getString("Faq_content"));
				list.add(Faq);
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
		FaqJDBCDAO dao = new FaqJDBCDAO();
//		 新增
		
//		FaqVO faq1 = new FaqVO();
//		faq1.setFaq_content("我如何註冊?點選右上「登入/註冊」。 您可直接透過社群帳號(Facebook、Google)直接登入。或以其他電子郵件註冊，設定密碼，並通過驗證。我們會寄送電子郵件認證碼，請記得確認你的信箱，有沒有收到認證碼。");	
//		dao.insert(faq1);
				
		// 修改
			
//		FaqVO faq2 = new FaqVO();
//		faq2.setFaq_content("點選左上「登入/註冊」。");
//		faq2.setFaq_id("FA000000004");
//     	dao.update(faq2);

				
				
     	// 刪除
//		dao.delete("FA000000004");
				
     	// 查詢
				
		FaqVO faq3 = new FaqVO();
		faq3 = dao.findByPrimaryKey("FA000000001");
		System.out.println(faq3.getFaq_id());
		System.out.println(faq3.getFaq_content());


     	// 查詢
     	
		List<FaqVO> list = new ArrayList<FaqVO>();
		list = dao.getAll();
		for (FaqVO faq4 : list){
			System.out.println(faq4.getFaq_id());
		}
		
		
	}
}


