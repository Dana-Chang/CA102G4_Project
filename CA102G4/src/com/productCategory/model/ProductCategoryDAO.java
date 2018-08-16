package com.productCategory.model;

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


public class ProductCategoryDAO implements ProductCategoryDAO_interface {
	private static DataSource ds = null;
	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 1L;
	private static final String INSERT_STMT = 
			"INSERT INTO PRODUCT_CATEGORY (PRODUCT_CATEGORY_ID,PRODUCT_CATEGORY_NAME)"
			+ " VALUES (product_category_seq.NEXTVAL, ?)";
			
	private static final String UPDATE_STMT = 
			"UPDATE PRODUCT_CATEGORY set PRODUCT_CATEGORY_NAME = ? where PRODUCT_CATEGORY_ID = ? ";
	
	private static final String DELETE_STMT = 
			"DELETE FROM  PRODUCT_CATEGORY where PRODUCT_CATEGORY_ID = ?";
	
	private static final String FIND_BY_PK =
			"SELECT PRODUCT_CATEGORY_ID,PRODUCT_CATEGORY_NAME FROM PRODUCT_CATEGORY where PRODUCT_CATEGORY_ID = ?";

	
	private static final String GET_ALL_STMT = 
			"SELECT PRODUCT_CATEGORY_ID,PRODUCT_CATEGORY_NAME FROM PRODUCT_CATEGORY order by PRODUCT_CATEGORY_ID";

	@Override
	public void insert(ProductCategoryVO productCategoryVO) {
	
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
				
				
				pstmt.setString(1,productCategoryVO.getProduct_category_name());
	
				pstmt.executeUpdate();

			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		}finally {
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
	public void update(ProductCategoryVO productCategoryVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);
				
		    	pstmt.setString(1,productCategoryVO.getProduct_category_name());
				pstmt.setInt(2,productCategoryVO.getProduct_category_id());
	
				pstmt.executeUpdate();

			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		}finally {
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
	public void delete(Integer product_category_id) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);
				
		    	
				pstmt.setInt(1,product_category_id);
	
				pstmt.executeUpdate();

			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		}finally {
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
	public List<ProductCategoryVO> getAll() {
		List<ProductCategoryVO> listAll = new ArrayList<ProductCategoryVO>();
		ProductCategoryVO productCategoryVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				productCategoryVO = new ProductCategoryVO();
				
				productCategoryVO.setProduct_category_id(rs.getInt("PRODUCT_CATEGORY_ID"));
				productCategoryVO.setProduct_category_name(rs.getString("PRODUCT_CATEGORY_NAME"));
				
				
				listAll.add(productCategoryVO); // Store the row in the list
			}

			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		}finally {
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
	public ProductCategoryVO findByPK(Integer product_category_id) {
		ProductCategoryVO productCategoryVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK);
				
		    	
				pstmt.setInt(1,product_category_id);
	
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					productCategoryVO = new ProductCategoryVO();
					
					
					productCategoryVO.setProduct_category_id(rs.getInt("PRODUCT_CATEGORY_ID"));
					productCategoryVO.setProduct_category_name(rs.getString("PRODUCT_CATEGORY_NAME"));
					
					
				}


			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return productCategoryVO;		
		
	}

}
