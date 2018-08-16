package com.tripCollect.model;

import java.sql.*;
import java.util.*;

public class TripCollectJDBCDAO implements TripCollectDAO_interface {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "CA102G4";
	private static final String PASSWORD = "12345678";
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

	private static final String SQL_INSERT = "insert into TRIP_COLLECT (TRIP_NO,MEM_ID) values (?,?)";
	private static final String SQL_UPDATE = "update TRIP_COLLECT set "
			+ "TRIP_NO = ?,MEM_ID = ? where TRIP_NO = ? and MEM_ID = ?";
	private static final String SQL_DELETE = "delete from TRIP_COLLECT where TRIP_NO = ? and MEM_ID = ?";
	private static final String SQL_QUERY = "select * from TRIP_COLLECT where TRIP_NO = ? and MEM_ID = ?";
	private static final String SQL_QUERY_ALL = "select * from TRIP_COLLECT";
	private static final String SQL_QUERY_MEM = "select * from TRIP_COLLECT where MEM_ID = ?";
	
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		}
	}

	@Override
	public int insert(TripCollectVO tripCollectVO) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_INSERT);
			pstmt.setString(1, tripCollectVO.getTrip_no());
			pstmt.setString(2, tripCollectVO.getMem_id());

			updateCount = pstmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return updateCount;
	}

	@Override
	public int update(TripCollectVO tripCollectVO_old, TripCollectVO tripCollectVO_new) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_UPDATE);
			pstmt.setString(1, tripCollectVO_new.getTrip_no());
			pstmt.setString(2, tripCollectVO_new.getMem_id());
			pstmt.setString(3, tripCollectVO_old.getTrip_no());
			pstmt.setString(4, tripCollectVO_old.getMem_id());

			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return updateCount;
	}

	@Override
	public int delete(String trip_no, String mem_id) {
		int updateCount = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_DELETE);
			pstmt.setString(1, trip_no);
			pstmt.setString(2, mem_id);

			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return updateCount;
	}

	@Override
	public TripCollectVO findByPrimaryKey(String trip_no, String mem_id) {
		TripCollectVO tripCollectVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY);
			pstmt.setString(1, trip_no);
			pstmt.setString(2, mem_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tripCollectVO = new TripCollectVO();
				tripCollectVO.setTrip_no(rs.getString("TRIP_NO"));
				tripCollectVO.setMem_id(rs.getString("MEM_ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return tripCollectVO;
	}

	@Override
	public List<TripCollectVO> getAll() {
		List<TripCollectVO> list = new ArrayList<TripCollectVO>();
		TripCollectVO tripCollectVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_ALL);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				tripCollectVO = new TripCollectVO();
				tripCollectVO.setTrip_no(rs.getString("TRIP_NO"));
				tripCollectVO.setMem_id(rs.getString("MEM_ID"));
				
				list.add(tripCollectVO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	@Override
	public List<TripCollectVO> getByMem_id(String mem_id) {
		List<TripCollectVO> list = new ArrayList<TripCollectVO>();
		TripCollectVO tripCollectVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt = con.prepareStatement(SQL_QUERY_MEM);
			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				tripCollectVO = new TripCollectVO();
				tripCollectVO.setTrip_no(rs.getString("TRIP_NO"));
				tripCollectVO.setMem_id(rs.getString("MEM_ID"));

				list.add(tripCollectVO);
			}
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
}
