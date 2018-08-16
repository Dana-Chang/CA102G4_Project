package com.store711.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.store711Dist.model.Store711DistDAO_interface;
import com.store711Dist.model.Store711DistVO;

public class Store711JDBCDAO implements Store711DAO_interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";

	private static final long serialVersionUID = 1L;
	
	private static final String FIND_BY_PK =
			"SELECT STORE_711_ID,STORE_711_DIST_ID,STORE_711_NAME,STORE_711_ADDR,STORE_711_CITY_NAME,STORE_711_DIST_NAME FROM STORE_711 where STORE_711_ID = ?";

	
	private static final String GET_ALL_STMT = 
			"SELECT STORE_711_ID,STORE_711_DIST_ID,STORE_711_NAME,STORE_711_ADDR,STORE_711_CITY_NAME,STORE_711_DIST_NAME FROM STORE_711 order by STORE_711_ID";

	
	
	
	@Override
	public List<Store711VO> getAll() {
		List<Store711VO> listAll = new ArrayList<Store711VO>();
		Store711VO store711VO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				store711VO = new Store711VO();
				
				store711VO.setStore_711_id(rs.getInt("STORE_711_ID"));
				store711VO.setStore_711_dist_id(rs.getInt("STORE_711_DIST_ID"));
				store711VO.setStore_711_name(rs.getString("STORE_711_NAME"));
				store711VO.setStore_711_addr(rs.getString("STORE_711_ADDR"));
				store711VO.setStore_711_city_name(rs.getString("STORE_711_CITY_NAME"));
				store711VO.setStore_711_dist_name(rs.getString("STORE_711_DIST_NAME"));
				
				listAll.add(store711VO); // Store the row in the list
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
	public Store711VO findByPK(Integer store_711_id) {
		Store711VO store711VO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_PK);

			pstmt.setInt(1, store_711_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				store711VO = new Store711VO();
				
				store711VO.setStore_711_id(rs.getInt("STORE_711_ID"));
				store711VO.setStore_711_dist_id(rs.getInt("STORE_711_DIST_ID"));
				store711VO.setStore_711_name(rs.getString("STORE_711_NAME"));
				store711VO.setStore_711_addr(rs.getString("STORE_711_ADDR"));
				store711VO.setStore_711_city_name(rs.getString("STORE_711_CITY_NAME"));
				store711VO.setStore_711_dist_name(rs.getString("STORE_711_DIST_NAME"));
				
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
		return store711VO;
	}
	


}
