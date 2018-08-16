package com.productReport.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.orderDetails.model.OrderDetailsVO;


public class ProductReportJDBCDAO implements ProductReportDAO_interface {
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";

	private static final long serialVersionUID = 1L;
	
	private static final String INSERT_STMT = 
			"INSERT INTO PRODUCT_REPORT (PROD_REPORT_PRODUCT_ID,PROD_REPORT_MEM_ID,PROD_REPORT_REASON,PROD_REPORT_TIME"
			+",PROD_REPORT_STATUS) VALUES (?, ?, ?,?, ?)";
			
	private static final String UPDATE_STMT = 
			"UPDATE PRODUCT_REPORT set PROD_REPORT_REASON=?,PROD_REPORT_TIME=?,PROD_REPORT_STATUS=?"
			+ " where PROD_REPORT_PRODUCT_ID = ? and PROD_REPORT_MEM_ID = ? ";
	
	private static final String DELETE_STMT = 
			"DELETE FROM PRODUCT_REPORT where PROD_REPORT_PRODUCT_ID = ? and PROD_REPORT_MEM_ID = ? ";
	
	private static final String FIND_BY_PK =
			"SELECT PROD_REPORT_PRODUCT_ID,PROD_REPORT_MEM_ID,PROD_REPORT_REASON,PROD_REPORT_TIME"
			+ ",PROD_REPORT_STATUS FROM PRODUCT_REPORT where PROD_REPORT_PRODUCT_ID = ? and PROD_REPORT_MEM_ID = ?";

	
	private static final String GET_ALL_STMT = 
			"SELECT PROD_REPORT_PRODUCT_ID,PROD_REPORT_MEM_ID,PROD_REPORT_REASON,PROD_REPORT_TIME"
			+ ",PROD_REPORT_STATUS FROM PRODUCT_REPORT order by PROD_REPORT_PRODUCT_ID";
	

	@Override
	public void insert(ProductReportVO productReportVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);
				
				
			pstmt.setInt(1,productReportVO.getProd_report_product_id());
			pstmt.setString(2,productReportVO.getProd_report_mem_id());
			pstmt.setString(3,productReportVO.getProd_report_reason());
			pstmt.setTimestamp(4,productReportVO.getProd_report_time());
			pstmt.setInt(5,productReportVO.getProd_report_status());
			
				pstmt.executeUpdate();

			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
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
	public void update(ProductReportVO productReportVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
				
				
			pstmt.setString(1,productReportVO.getProd_report_reason());
			pstmt.setTimestamp(2,productReportVO.getProd_report_time());
			pstmt.setInt(3,productReportVO.getProd_report_status());
        	pstmt.setInt(4,productReportVO.getProd_report_product_id());
			pstmt.setString(5,productReportVO.getProd_report_mem_id());
			
				pstmt.executeUpdate();

			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
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
	public void delete(Integer prod_report_product_id, String prod_report_mem_id) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
				
				pstmt.setInt(1,prod_report_product_id);
				pstmt.setString(2,prod_report_mem_id);
				
				pstmt.executeUpdate();

			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
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
	public List<ProductReportVO> getAll() {
		List<ProductReportVO> listAll = new ArrayList<ProductReportVO>();
		ProductReportVO productReportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				productReportVO = new ProductReportVO();
				
				
				productReportVO.setProd_report_product_id(rs.getInt("PROD_REPORT_PRODUCT_ID"));
				productReportVO.setProd_report_mem_id(rs.getString("PROD_REPORT_MEM_ID"));
				productReportVO.setProd_report_reason(rs.getString("PROD_REPORT_REASON"));
				productReportVO.setProd_report_time(rs.getTimestamp("PROD_REPORT_TIME"));
				productReportVO.setProd_report_status(rs.getInt("PROD_REPORT_STATUS"));
				
				listAll.add(productReportVO); // Store the row in the list
			}

			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
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
		return listAll;
	}

	@Override
	public ProductReportVO findByPK(Integer prod_report_product_id, String prod_report_mem_id) {
		ProductReportVO productReportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_PK);
				
			    
				pstmt.setInt(1,prod_report_product_id);
				pstmt.setString(2,prod_report_mem_id);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					
					productReportVO = new ProductReportVO();
					
					
					productReportVO.setProd_report_product_id(rs.getInt("PROD_REPORT_PRODUCT_ID"));
					productReportVO.setProd_report_mem_id(rs.getString("PROD_REPORT_MEM_ID"));
					productReportVO.setProd_report_reason(rs.getString("PROD_REPORT_REASON"));
					productReportVO.setProd_report_time(rs.getTimestamp("PROD_REPORT_TIME"));
					productReportVO.setProd_report_status(rs.getInt("PROD_REPORT_STATUS"));
				
				}


			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
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
		return productReportVO;		
	}

}
