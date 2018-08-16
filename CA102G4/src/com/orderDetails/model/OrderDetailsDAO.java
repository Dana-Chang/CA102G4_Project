package com.orderDetails.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.product.model.ProductService;
import com.product.model.ProductVO;

public class OrderDetailsDAO implements OrderDetailsDAO_interface {
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

	private static final String INSERT_STMT = "INSERT INTO ORDER_DETAILS (DETAILS_ORDER_ID, DETAILS_PRODUCT_ID,DETAILS_ORDER_QTY,DETAILS_ORDER_TOTAL)"
			+ " VALUES (to_char(sysdate,'yyyymmdd')||'-'||LPAD(to_char(ord_seq.CURRVAL), 6, '0'), ?, ?, ?)";
	private static final String INSERT_STMT2 = "INSERT INTO ORDER_DETAILS (DETAILS_ORDER_ID, DETAILS_PRODUCT_ID,DETAILS_ORDER_QTY,DETAILS_ORDER_TOTAL)" + " VALUES (?, ?, ?, ?)";

	private static final String UPDATE_STMT = "UPDATE ORDER_DETAILS set DETAILS_ORDER_QTY=?,DETAILS_ORDER_TOTAL=?" + "where DETAILS_ORDER_ID = ? and DETAILS_PRODUCT_ID = ?  ";

	private static final String FIND_BY_PK = "SELECT DETAILS_ORDER_ID, DETAILS_PRODUCT_ID,DETAILS_ORDER_QTY,DETAILS_ORDER_TOTAL"
			+ " FROM ORDER_DETAILS where DETAILS_ORDER_ID = ? and DETAILS_PRODUCT_ID = ?";

	private static final String GET_ALL_STMT = "SELECT DETAILS_ORDER_ID, DETAILS_PRODUCT_ID,DETAILS_ORDER_QTY,DETAILS_ORDER_TOTAL" + " FROM ORDER_DETAILS order by DETAILS_ORDER_ID";
	
	private static final String FIND_BY_FK1 = "SELECT DETAILS_ORDER_ID, DETAILS_PRODUCT_ID,DETAILS_ORDER_QTY,DETAILS_ORDER_TOTAL"
			+ " FROM ORDER_DETAILS where DETAILS_ORDER_ID = ?";
	@Override
	public void insert(OrderDetailsVO orderDetailsVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setInt(1, orderDetailsVO.getDetails_product_id());
			pstmt.setInt(2, orderDetailsVO.getDetails_order_qty());
			pstmt.setInt(3, orderDetailsVO.getDetails_order_total());

			pstmt.executeUpdate();

			// Handle any driver errors
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
	}

	@Override
	public void update(OrderDetailsVO orderDetailsVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STMT);

			pstmt.setInt(1, orderDetailsVO.getDetails_order_qty());
			pstmt.setInt(2, orderDetailsVO.getDetails_order_total());
			pstmt.setString(3, orderDetailsVO.getDetails_order_id());
			pstmt.setInt(4, orderDetailsVO.getDetails_product_id());

			pstmt.executeUpdate();

			// Handle any driver errors
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

	}

	@Override
	public List<OrderDetailsVO> getAll() {
		List<OrderDetailsVO> listAll = new ArrayList<OrderDetailsVO>();
		OrderDetailsVO orderDetailsVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				orderDetailsVO = new OrderDetailsVO();

				orderDetailsVO.setDetails_order_id(rs.getString("DETAILS_ORDER_ID"));
				orderDetailsVO.setDetails_product_id(rs.getInt("DETAILS_PRODUCT_ID"));
				orderDetailsVO.setDetails_order_qty(rs.getInt("DETAILS_ORDER_QTY"));
				orderDetailsVO.setDetails_order_total(rs.getInt("DETAILS_ORDER_TOTAL"));

				listAll.add(orderDetailsVO); // Store the row in the list
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public OrderDetailsVO findByPK(String DETAILS_ORDER_ID, Integer DETAILS_PRODUCT_ID) {
		OrderDetailsVO orderDetailsVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK);

			pstmt.setString(1, DETAILS_ORDER_ID);
			pstmt.setInt(2, DETAILS_PRODUCT_ID);

			rs = pstmt.executeQuery();
			while (rs.next()) {

				orderDetailsVO = new OrderDetailsVO();

				orderDetailsVO.setDetails_order_id(rs.getString("DETAILS_ORDER_ID"));
				orderDetailsVO.setDetails_product_id(rs.getInt("DETAILS_PRODUCT_ID"));
				orderDetailsVO.setDetails_order_qty(rs.getInt("DETAILS_ORDER_QTY"));
				orderDetailsVO.setDetails_order_total(rs.getInt("DETAILS_ORDER_TOTAL"));

			}

			// Handle any driver errors
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
		return orderDetailsVO;
	}

	@Override
	synchronized public void insert2(OrderDetailsVO orderDetailsVO, Connection con) {
		
		ProductService prodSvc = new ProductService();
		ProductVO prodVO= prodSvc.getOneProduct(orderDetailsVO.getDetails_product_id());
    	int newstock = prodVO.getProduct_stock()-orderDetailsVO.getDetails_order_qty();
    	if(newstock<0) {
    		try {
    			System.err.print("Transaction is being ");
				System.err.println("rolled back-由-OrderDetails new stock <0");
				con.rollback();
			} catch (SQLException e) {
				throw new RuntimeException("rollback error occured. " + e.getMessage());
			}
    	}else if(newstock>=0) {
    		prodVO.setProduct_stock(newstock);
    		if(newstock==0) {
    			prodVO.setProduct_status(3);//3>已出售
    		}
    		prodSvc.updateProduct(prodVO);
    		PreparedStatement pstmt = null;
			try {
				pstmt = con.prepareStatement(INSERT_STMT2);
				pstmt.setString(1, orderDetailsVO.getDetails_order_id());
				pstmt.setInt(2, orderDetailsVO.getDetails_product_id());
				pstmt.setInt(3, orderDetailsVO.getDetails_order_qty());
				pstmt.setInt(4, orderDetailsVO.getDetails_order_total());
	
				pstmt.executeUpdate();
	
				// Handle any driver errors
			} catch (SQLException se) {
				if (con != null) {
					try {
						// 3●設定於當有exception發生時之catch區塊內
						System.err.print("Transaction is being ");
						System.err.println("rolled back-由-OrderDetails");
						con.rollback();
					} catch (SQLException excep) {
						throw new RuntimeException("rollback error occured. " + excep.getMessage());
					}
				}
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
			}
    	}
	}

	@Override
	public List<OrderDetailsVO> getOrderDetialsByOrdId(String details_order_id) {
		List<OrderDetailsVO> listAll = new ArrayList<OrderDetailsVO>();
		OrderDetailsVO orderDetailsVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_FK1);

			pstmt.setString(1, details_order_id);
			

			rs = pstmt.executeQuery();
			while (rs.next()) {

				orderDetailsVO = new OrderDetailsVO();

				orderDetailsVO.setDetails_order_id(rs.getString("DETAILS_ORDER_ID"));
				orderDetailsVO.setDetails_product_id(rs.getInt("DETAILS_PRODUCT_ID"));
				orderDetailsVO.setDetails_order_qty(rs.getInt("DETAILS_ORDER_QTY"));
				orderDetailsVO.setDetails_order_total(rs.getInt("DETAILS_ORDER_TOTAL"));

				listAll.add(orderDetailsVO); // Store the row in the list

			}

			// Handle any driver errors
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
		return listAll;
	}

}
