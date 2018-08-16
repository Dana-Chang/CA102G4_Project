package com.street.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class StreetDAO implements StreetDAO_interface{
	
	
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
	
	private static final String GET_COUNTRY_BY_CITY_STMT =
			"SELECT DISTINCT COUNTRY AS COUNTRY FROM STREETNAME WHERE CITY=?";

	private static final String GET_ROAD_BY_COUNTRY_STMT =
			"SELECT DISTINCT ROAD AS ROAD FROM STREETNAME WHERE COUNTRY=?";
	
	private static final String GET_ALL_CITY_STMT = 
			"SELECT DISTINCT CITY AS CITY FROM STREETNAME";


	@Override
	public List<String> listCity() {
		List<String> listAllCity = new ArrayList<String>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_CITY_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				String city = rs.getString("CITY");
				listAllCity.add(city);
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
		return listAllCity;
	}


	@Override
	public List<String> findCountryByCity(String city) {
	List<String> listCountry = new ArrayList<String>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_COUNTRY_BY_CITY_STMT);
			
			pstmt.setString(1, city);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				String country = rs.getString(1);
				listCountry.add(country);
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
		return listCountry;
	}


	@Override
	public List<String> findRoadByCountry(String country) {
		List<String> listRoad = new ArrayList<String>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ROAD_BY_COUNTRY_STMT);
			
			pstmt.setString(1, country);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				String road = rs.getString(1);
				listRoad.add(road);
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
		return listRoad;
	}

	}



