package com.productWishlist.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.productReport.model.ProductReportVO;

public class ProductWishlistDAO implements ProductWishlistDAO_interface {
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
			"INSERT INTO PRODUCT_WISHLIST (WISHLIST_PRODUCT_ID,WISHLIST_MEM_ID,PRODUCT_WISHLIST_TIME)"
			+"VALUES (?, ?,?)";
			
	private static final String DELETE_STMT = 
			"DELETE FROM PRODUCT_WISHLIST where WISHLIST_PRODUCT_ID = ? and WISHLIST_MEM_ID = ? ";
	
	private static final String FIND_BY_PK =
			"SELECT WISHLIST_PRODUCT_ID,WISHLIST_MEM_ID,PRODUCT_WISHLIST_TIME"
			+ " FROM PRODUCT_WISHLIST where WISHLIST_PRODUCT_ID = ? and WISHLIST_MEM_ID = ?";

	
	private static final String GET_ALL_STMT = 
			"SELECT WISHLIST_PRODUCT_ID,WISHLIST_MEM_ID,PRODUCT_WISHLIST_TIME"
			+ " FROM PRODUCT_WISHLIST order by WISHLIST_PRODUCT_ID";
	
	private static final String FIND_BY_FK1 =
			"SELECT WISHLIST_PRODUCT_ID,WISHLIST_MEM_ID,PRODUCT_WISHLIST_TIME"
			+ " FROM PRODUCT_WISHLIST where WISHLIST_PRODUCT_ID = ?";
	
	private static final String FIND_BY_FK2 =
			"SELECT WISHLIST_PRODUCT_ID,WISHLIST_MEM_ID,PRODUCT_WISHLIST_TIME"
			+ " FROM PRODUCT_WISHLIST where WISHLIST_MEM_ID = ? order by PRODUCT_WISHLIST_TIME";
	
	@Override
	public void insert(ProductWishlistVO productWishlistVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
				
				
				pstmt.setInt(1,productWishlistVO.getWishlist_product_id());
				pstmt.setString(2,productWishlistVO.getWishlist_mem_id());
				pstmt.setTimestamp(3,productWishlistVO.getProduct_wishlist_time());
		
				pstmt.executeUpdate();

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
		
		
	}

	@Override
	public void delete(Integer wishlist_product_id, String wishlist_mem_id) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_STMT);
				
				
				pstmt.setInt(1,wishlist_product_id);
				pstmt.setString(2,wishlist_mem_id);
		
				pstmt.executeUpdate();

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
		
	}

	@Override
	public List<ProductWishlistVO> getAll() {
		List<ProductWishlistVO> listAll = new ArrayList<ProductWishlistVO>();
		ProductWishlistVO productWishlistVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				productWishlistVO = new ProductWishlistVO();
				
				
				productWishlistVO.setWishlist_product_id(rs.getInt("WISHLIST_PRODUCT_ID"));
				productWishlistVO.setWishlist_mem_id(rs.getString("WISHLIST_MEM_ID"));
				productWishlistVO.setProduct_wishlist_time(rs.getTimestamp("PRODUCT_WISHLIST_TIME"));

				
				listAll.add(productWishlistVO); // Store the row in the list
			}

			// Handle any driver errors
		}  catch (SQLException se) {
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
		return listAll;
	}

	@Override
	public ProductWishlistVO findByPK(Integer wishlist_product_id, String wishlist_mem_id) {
		ProductWishlistVO productWishlistVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK);
				
			    
				pstmt.setInt(1,wishlist_product_id);
				pstmt.setString(2,wishlist_mem_id);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					
					productWishlistVO = new ProductWishlistVO();
					
					
					productWishlistVO.setWishlist_product_id(rs.getInt("WISHLIST_PRODUCT_ID"));
					productWishlistVO.setWishlist_mem_id(rs.getString("WISHLIST_MEM_ID"));
					productWishlistVO.setProduct_wishlist_time(rs.getTimestamp("PRODUCT_WISHLIST_TIME"));
				
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
		return productWishlistVO;		
	}

	@Override
	public Set<ProductWishlistVO> getLikesByProductid(Integer wishlist_product_id) {
		Set<ProductWishlistVO> set = new LinkedHashSet<ProductWishlistVO>();
		ProductWishlistVO productWishlistVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
	
			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_FK1);
			pstmt.setInt(1, wishlist_product_id);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				productWishlistVO = new ProductWishlistVO();
				productWishlistVO.setWishlist_product_id(rs.getInt("WISHLIST_PRODUCT_ID"));
				productWishlistVO.setWishlist_mem_id(rs.getString("WISHLIST_MEM_ID"));
				productWishlistVO.setProduct_wishlist_time(rs.getTimestamp("PRODUCT_WISHLIST_TIME"));
				set.add(productWishlistVO); // Store the row in the vector
			}
	
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return set;
	}

	@Override
	public Set<ProductWishlistVO> getLikesByMemid(String wishlist_mem_id) {
		Set<ProductWishlistVO> set = new LinkedHashSet<ProductWishlistVO>();
		ProductWishlistVO productWishlistVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
	
			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_FK2);
			pstmt.setString(1, wishlist_mem_id);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				productWishlistVO = new ProductWishlistVO();
				productWishlistVO.setWishlist_product_id(rs.getInt("WISHLIST_PRODUCT_ID"));
				productWishlistVO.setWishlist_mem_id(rs.getString("WISHLIST_MEM_ID"));
				productWishlistVO.setProduct_wishlist_time(rs.getTimestamp("PRODUCT_WISHLIST_TIME"));
				set.add(productWishlistVO); // Store the row in the vector
			}
	
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return set;
	}

}
