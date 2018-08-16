package com.product.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.productCategory.model.ProductCategoryVO;

import jdbc.util.CompositeQuery.jdbcUtil_CompositeQuery_Product;

public class ProductJDBCDAO implements ProductDAO_interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";

	private static final long serialVersionUID = 1L;
	
	private static final String INSERT_STMT = 
			"INSERT INTO PRODUCT (PRODUCT_ID,PRODUCT_CATEGORY_ID,PRODUCT_MEM_ID,PRODUCT_NAME,PRODUCT_PRICE"
			+",PRODUCT_DESCR,PRODUCT_STOCK,PRODUCT_STATUS,PRODUCT_DATE,PRODUCT_PHOTO_1,PRODUCT_PHOTO_2,PRODUCT_PHOTO_3"
			+ ",PRODUCT_PHOTO_4,PRODUCT_PHOTO_5) VALUES (product_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
	private static final String UPDATE_STMT = 
			"UPDATE PRODUCT set PRODUCT_CATEGORY_ID=?,PRODUCT_MEM_ID=?,PRODUCT_NAME=?,PRODUCT_PRICE=?"
			+",PRODUCT_DESCR=?,PRODUCT_STOCK=?,PRODUCT_STATUS=?,PRODUCT_DATE=?,PRODUCT_PHOTO_1=?,PRODUCT_PHOTO_2=?,PRODUCT_PHOTO_3=?"
			+ ",PRODUCT_PHOTO_4=?,PRODUCT_PHOTO_5=? where PRODUCT_ID = ?";
	
	private static final String DELETE_STMT = 
			"DELETE FROM  PRODUCT where PRODUCT_ID = ?";
	
	private static final String FIND_BY_PK =
			"SELECT PRODUCT_ID,PRODUCT_CATEGORY_ID,PRODUCT_MEM_ID,PRODUCT_NAME,PRODUCT_PRICE"
			+",PRODUCT_DESCR,PRODUCT_STOCK,PRODUCT_STATUS,PRODUCT_DATE,PRODUCT_PHOTO_1,PRODUCT_PHOTO_2,PRODUCT_PHOTO_3"
			+ ",PRODUCT_PHOTO_4,PRODUCT_PHOTO_5 FROM PRODUCT where PRODUCT_ID = ?";

	
	private static final String GET_ALL_STMT = 
			"SELECT PRODUCT_ID,PRODUCT_CATEGORY_ID,PRODUCT_MEM_ID,PRODUCT_NAME,PRODUCT_PRICE"
					+",PRODUCT_DESCR,PRODUCT_STOCK,PRODUCT_STATUS,PRODUCT_DATE,PRODUCT_PHOTO_1,PRODUCT_PHOTO_2,PRODUCT_PHOTO_3"
					+ ",PRODUCT_PHOTO_4,PRODUCT_PHOTO_5 FROM PRODUCT order by PRODUCT_ID";
	
	private static final String FIND_BY_FK1 =
			"SELECT PRODUCT_ID,PRODUCT_CATEGORY_ID,PRODUCT_MEM_ID,PRODUCT_NAME,PRODUCT_PRICE"
			+",PRODUCT_DESCR,PRODUCT_STOCK,PRODUCT_STATUS,PRODUCT_DATE,PRODUCT_PHOTO_1,PRODUCT_PHOTO_2,PRODUCT_PHOTO_3"
			+ ",PRODUCT_PHOTO_4,PRODUCT_PHOTO_5 FROM PRODUCT where PRODUCT_MEM_ID = ?";
	
	
	@Override
	public void insert(ProductVO productVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(INSERT_STMT);
				
				
				pstmt.setInt(1,productVO.getProduct_category_id());
				pstmt.setString(2,productVO.getProduct_mem_id());
				pstmt.setString(3,productVO.getProduct_name());
				pstmt.setInt(4,productVO.getProduct_price());
				pstmt.setString(5,productVO.getProduct_descr());
				pstmt.setInt(6,productVO.getProduct_stock());
				pstmt.setInt(7,productVO.getProduct_status());
				pstmt.setTimestamp(8,productVO.getProduct_date());
				pstmt.setBytes(9,productVO.getProduct_photo_1());
				pstmt.setBytes(10,productVO.getProduct_photo_2());
				pstmt.setBytes(11,productVO.getProduct_photo_3());
				pstmt.setBytes(12,productVO.getProduct_photo_4());
				pstmt.setBytes(13,productVO.getProduct_photo_5());
				
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
	public void update(ProductVO productVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(UPDATE_STMT);
				
				
				pstmt.setInt(1,productVO.getProduct_category_id());
				pstmt.setString(2,productVO.getProduct_mem_id());
				pstmt.setString(3,productVO.getProduct_name());
				pstmt.setInt(4,productVO.getProduct_price());
				pstmt.setString(5,productVO.getProduct_descr());
				pstmt.setInt(6,productVO.getProduct_stock());
				pstmt.setInt(7,productVO.getProduct_status());
				pstmt.setTimestamp(8,productVO.getProduct_date());
				pstmt.setBytes(9,productVO.getProduct_photo_1());
				pstmt.setBytes(10,productVO.getProduct_photo_2());
				pstmt.setBytes(11,productVO.getProduct_photo_3());
				pstmt.setBytes(12,productVO.getProduct_photo_4());
				pstmt.setBytes(13,productVO.getProduct_photo_5());
				pstmt.setInt(14,productVO.getProduct_id());
				
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
	public void delete(Integer product_id) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(DELETE_STMT);
				
		    	
				pstmt.setInt(1,product_id);
	
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
	public List<ProductVO> getAll() {
		List<ProductVO> listAll = new ArrayList<ProductVO>();
		ProductVO productVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				productVO = new ProductVO();
				
				
				productVO.setProduct_id(rs.getInt("PRODUCT_ID"));
				productVO.setProduct_category_id(rs.getInt("PRODUCT_CATEGORY_ID"));
				productVO.setProduct_mem_id(rs.getString("PRODUCT_MEM_ID"));
				productVO.setProduct_name(rs.getString("PRODUCT_NAME"));
				productVO.setProduct_price(rs.getInt("PRODUCT_PRICE"));
				productVO.setProduct_descr(rs.getString("PRODUCT_DESCR"));
				productVO.setProduct_stock(rs.getInt("PRODUCT_STOCK"));
				productVO.setProduct_status(rs.getInt("PRODUCT_STATUS"));
				productVO.setProduct_date(rs.getTimestamp("PRODUCT_DATE"));
				productVO.setProduct_photo_1(rs.getBytes("PRODUCT_PHOTO_1"));
				productVO.setProduct_photo_2(rs.getBytes("PRODUCT_PHOTO_2"));
				productVO.setProduct_photo_3(rs.getBytes("PRODUCT_PHOTO_3"));
				productVO.setProduct_photo_4(rs.getBytes("PRODUCT_PHOTO_4"));
				productVO.setProduct_photo_5(rs.getBytes("PRODUCT_PHOTO_5"));
				
				listAll.add(productVO); // Store the row in the list
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
	public ProductVO findByPK(Integer product_id) {
		ProductVO productVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_PK);
				
		    	
				pstmt.setInt(1,product_id);
	
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					productVO = new ProductVO();
					
					
					productVO.setProduct_id(rs.getInt("PRODUCT_ID"));
					productVO.setProduct_category_id(rs.getInt("PRODUCT_CATEGORY_ID"));
					productVO.setProduct_mem_id(rs.getString("PRODUCT_MEM_ID"));
					productVO.setProduct_name(rs.getString("PRODUCT_NAME"));
					productVO.setProduct_price(rs.getInt("PRODUCT_PRICE"));
					productVO.setProduct_descr(rs.getString("PRODUCT_DESCR"));
					productVO.setProduct_stock(rs.getInt("PRODUCT_STOCK"));
					productVO.setProduct_status(rs.getInt("PRODUCT_STATUS"));
					productVO.setProduct_date(rs.getTimestamp("PRODUCT_DATE"));
					productVO.setProduct_photo_1(rs.getBytes("PRODUCT_PHOTO_1"));
					productVO.setProduct_photo_2(rs.getBytes("PRODUCT_PHOTO_2"));
					productVO.setProduct_photo_3(rs.getBytes("PRODUCT_PHOTO_3"));
					productVO.setProduct_photo_4(rs.getBytes("PRODUCT_PHOTO_4"));
					productVO.setProduct_photo_5(rs.getBytes("PRODUCT_PHOTO_5"));
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
		return productVO;		
		
	}
	
	@Override
	public Set<ProductVO> getProductsBySellerid(String product_mem_id) {
		Set<ProductVO> set = new LinkedHashSet<ProductVO>();
		ProductVO productVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
	
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_FK1);
			pstmt.setString(1, product_mem_id);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {		
				productVO = new ProductVO();
	
				productVO.setProduct_id(rs.getInt("PRODUCT_ID"));
				productVO.setProduct_category_id(rs.getInt("PRODUCT_CATEGORY_ID"));
				productVO.setProduct_mem_id(rs.getString("PRODUCT_MEM_ID"));
				productVO.setProduct_name(rs.getString("PRODUCT_NAME"));
				productVO.setProduct_price(rs.getInt("PRODUCT_PRICE"));
				productVO.setProduct_descr(rs.getString("PRODUCT_DESCR"));
				productVO.setProduct_stock(rs.getInt("PRODUCT_STOCK"));
				productVO.setProduct_status(rs.getInt("PRODUCT_STATUS"));
				productVO.setProduct_date(rs.getTimestamp("PRODUCT_DATE"));
				productVO.setProduct_photo_1(rs.getBytes("PRODUCT_PHOTO_1"));
				productVO.setProduct_photo_2(rs.getBytes("PRODUCT_PHOTO_2"));
				productVO.setProduct_photo_3(rs.getBytes("PRODUCT_PHOTO_3"));
				productVO.setProduct_photo_4(rs.getBytes("PRODUCT_PHOTO_4"));
				productVO.setProduct_photo_5(rs.getBytes("PRODUCT_PHOTO_5"));
				
				set.add(productVO);
			}
	
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return set;
	}

	@Override
	public List<ProductVO> getAll(Map<String, String[]> map) {
		List<ProductVO> list = new ArrayList<ProductVO>();
		ProductVO productVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
			
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			String finalSQL = "select * from product "
		          + jdbcUtil_CompositeQuery_Product.get_WhereCondition(map);
			pstmt = con.prepareStatement(finalSQL);
			System.out.println("●●finalSQL(by DAO) = "+finalSQL);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				productVO = new ProductVO();
		
				productVO.setProduct_id(rs.getInt("PRODUCT_ID"));
				productVO.setProduct_category_id(rs.getInt("PRODUCT_CATEGORY_ID"));
				productVO.setProduct_mem_id(rs.getString("PRODUCT_MEM_ID"));
				productVO.setProduct_name(rs.getString("PRODUCT_NAME"));
				productVO.setProduct_price(rs.getInt("PRODUCT_PRICE"));
				productVO.setProduct_descr(rs.getString("PRODUCT_DESCR"));
				productVO.setProduct_stock(rs.getInt("PRODUCT_STOCK"));
				productVO.setProduct_status(rs.getInt("PRODUCT_STATUS"));
				productVO.setProduct_date(rs.getTimestamp("PRODUCT_DATE"));
				productVO.setProduct_photo_1(rs.getBytes("PRODUCT_PHOTO_1"));
				productVO.setProduct_photo_2(rs.getBytes("PRODUCT_PHOTO_2"));
				productVO.setProduct_photo_3(rs.getBytes("PRODUCT_PHOTO_3"));
				productVO.setProduct_photo_4(rs.getBytes("PRODUCT_PHOTO_4"));
				productVO.setProduct_photo_5(rs.getBytes("PRODUCT_PHOTO_5"));
				list.add(productVO); // Store the row in the List
			}
	
			// Handle any SQL errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
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
		return list;
	}

}
