package com.store711Dist.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.store711City.model.Store711CityVO;

public class Store711DistJDBCDAO implements Store711DistDAO_interface{
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";

	private static final long serialVersionUID = 1L;
	
	private static final String FIND_BY_PK =
			"SELECT STORE_711_DIST_ID,STORE_711_DIST_NAME,STORE_711_CITY_ID FROM STORE_711_DIST where STORE_711_DIST_ID = ?";

	
	private static final String GET_ALL_STMT = 
			"SELECT STORE_711_DIST_ID,STORE_711_DIST_NAME,STORE_711_CITY_ID FROM STORE_711_DIST order by STORE_711_DIST_ID";

	
	
	
	@Override
	public List<Store711DistVO> getAll() {
		List<Store711DistVO> listAll = new ArrayList<Store711DistVO>();
		Store711DistVO store711DistVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				store711DistVO = new Store711DistVO();
				
				store711DistVO.setStore_711_dist_id(rs.getInt("STORE_711_DIST_ID"));
				store711DistVO.setStore_711_dist_name(rs.getString("STORE_711_DIST_NAME"));
				store711DistVO.setStore_711_city_id(rs.getInt("STORE_711_CITY_ID"));
				
				listAll.add(store711DistVO); // Store the row in the list
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
	public Store711DistVO findByPK(Integer STORE_711_DIST_ID) {
		Store711DistVO store711DistVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(FIND_BY_PK);

			pstmt.setInt(1, STORE_711_DIST_ID);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				store711DistVO = new Store711DistVO();
				
				store711DistVO.setStore_711_dist_id(rs.getInt("STORE_711_DIST_ID"));
				store711DistVO.setStore_711_dist_name(rs.getString("STORE_711_DIST_NAME"));
				store711DistVO.setStore_711_city_id(rs.getInt("STORE_711_CITY_ID"));
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
		return store711DistVO;
	}
	
	

}
