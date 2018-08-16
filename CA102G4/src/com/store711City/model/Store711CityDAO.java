package com.store711City.model;

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

import com.ord.model.OrdVO;

public class Store711CityDAO implements Store711CityDAO_interface{
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
	
	private static final String FIND_BY_PK =
			"SELECT STORE_711_CITY_ID,STORE_711_CITY_NAME FROM STORE_711_CITY where STORE_711_CITY_ID = ?";

	
	private static final String GET_ALL_STMT = 
			"SELECT STORE_711_CITY_ID,STORE_711_CITY_NAME FROM STORE_711_CITY order by STORE_711_CITY_ID";

	
	
	@Override
	public List<Store711CityVO> getAll() {
		List<Store711CityVO> listAll = new ArrayList<Store711CityVO>();
		Store711CityVO store711CityVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				store711CityVO = new Store711CityVO();
				
				store711CityVO.setStore_711_city_id(rs.getInt("STORE_711_CITY_ID"));
				store711CityVO.setStore_711_city_name(rs.getString("STORE_711_CITY_NAME"));
				listAll.add(store711CityVO); // Store the row in the list
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
	public Store711CityVO findByPK(Integer store_711_city_id) {
		Store711CityVO store711CityVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_BY_PK);

			pstmt.setInt(1, store_711_city_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				store711CityVO = new Store711CityVO();
				
				store711CityVO.setStore_711_city_id(rs.getInt("STORE_711_CITY_ID"));
				store711CityVO.setStore_711_city_name(rs.getString("STORE_711_CITY_NAME"));
			
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
		return store711CityVO;
	}

}
