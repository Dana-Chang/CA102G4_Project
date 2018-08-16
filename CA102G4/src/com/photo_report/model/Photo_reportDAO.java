package com.photo_report.model;

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

public class Photo_reportDAO implements Photo_reportDAO_interface {

	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT =
			"Insert into PHOTO_REPORT (PHOTO_NO,MEM_ID,REPORT_REASON,REPORT_TIME,PHO_REP_STATS) VALUES (?,?,?,?,?)";
	private static final String UPDATE = 
			"UPDATE PHOTO_REPORT SET REPORT_REASON= ?,REPORT_TIME=?,PHO_REP_STATS=? WHERE PHOTO_NO =? AND MEM_ID = ?";
	private static final String DELETE_PHOTO_REPORT = 
			"DELETE FROM PHOTO_REPORT WHERE PHOTO_NO =? AND MEM_ID = ?";
	private static final String GET_ONE = 
			"SELECT * FROM PHOTO_REPORT WHERE PHOTO_NO = ? AND MEM_ID=?";
	private static final String GET_ALL = 
			"SELECT * FROM PHOTO_REPORT ORDER BY PHOTO_NO,MEM_ID";
	
	//更改照片牆審核狀態
	private static final String UPDATE_ON_STATE_STMT=
			"UPDATE PHOTO_REPORT SET PHO_REP_STATS = 1 WHERE MEM_ID=? AND PHOTO_NO=?";
	private static final String UPDATE_OFF_STATE_STMT=
			"UPDATE PHOTO_REPORT SET PHO_REP_STATS = 2 WHERE MEM_ID=? AND PHOTO_NO=?";
		
	@Override
	public void insert(Photo_reportVO photo_reportVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, photo_reportVO.getPhoto_No());
			pstmt.setString(2, photo_reportVO.getMem_Id());
			pstmt.setString(3, photo_reportVO.getReport_Reason());
			pstmt.setTimestamp(4, photo_reportVO.getReport_Time());
			pstmt.setInt(5, photo_reportVO.getPho_Rep_Stats());

			pstmt.executeUpdate();
			
		} catch (SQLException se) {
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
	public void update(Photo_reportVO photo_reportVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, photo_reportVO.getReport_Reason());
			pstmt.setTimestamp(2, photo_reportVO.getReport_Time());
			pstmt.setInt(3, photo_reportVO.getPho_Rep_Stats());
			pstmt.setString(4, photo_reportVO.getPhoto_No());
			pstmt.setString(5, photo_reportVO.getMem_Id());
			pstmt.executeUpdate();

			// Handle any driver errors
		}catch (SQLException se) {
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
	public void delete(String photo_Id, String mem_Id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();

			pstmt = con.prepareStatement(DELETE_PHOTO_REPORT);

			pstmt.setString(1, photo_Id);
			pstmt.setString(2, mem_Id);

			pstmt.executeUpdate();

		}catch (SQLException se) {
			if (con != null) {
				try {
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
	public Photo_reportVO findByPrimaryKey(String photo_No, String mem_Id) {
		Photo_reportVO photo_reportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ONE);

			pstmt.setString(1, photo_No);
			pstmt.setString(2, mem_Id);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				photo_reportVO = new Photo_reportVO();
				photo_reportVO.setPhoto_No(rs.getString("PHOTO_NO"));
				photo_reportVO.setMem_Id(rs.getString("MEM_ID"));
				photo_reportVO.setReport_Reason(rs.getString("REPORT_REASON"));
				photo_reportVO.setReport_Time(rs.getTimestamp("REPORT_TIME"));
				photo_reportVO.setPho_Rep_Stats(rs.getInt("PHO_REP_STATS"));

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
		return photo_reportVO;
	}
	@Override
	public List<Photo_reportVO> getAll() {
		List<Photo_reportVO> list = new ArrayList<Photo_reportVO>();
		Photo_reportVO photo_reportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ALL);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				photo_reportVO = new Photo_reportVO();
				photo_reportVO.setPhoto_No(rs.getString("PHOTO_NO"));
				photo_reportVO.setMem_Id(rs.getString("MEM_ID"));
				photo_reportVO.setReport_Reason(rs.getString("REPORT_REASON"));
				photo_reportVO.setReport_Time(rs.getTimestamp("REPORT_TIME"));
				photo_reportVO.setPho_Rep_Stats(rs.getInt("PHO_REP_STATS"));
				list.add(photo_reportVO); // Store the row in the list
			}

			// Handle any driver errors
		}catch (SQLException se) {
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
		return list;
	}
	
	@Override
	public int update_review_State(String mem_Id, String photo_No, Integer pho_Rep_Stats) {
		Connection con = null ;
		PreparedStatement pstmt= null;
		int count=0;
		if(pho_Rep_Stats == 1) {
			try {
				con=ds.getConnection();
				pstmt=con.prepareStatement(UPDATE_ON_STATE_STMT);
				
				pstmt.setString(1,mem_Id);
				pstmt.setString(2,photo_No);
				
				
				count=pstmt.executeUpdate();
				
			}catch(SQLException se) {
				throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
			}finally {
				if(pstmt != null) {
					try {
						pstmt.close();
					}catch(SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				
				if(con != null) {
					try {
					  con.close();
					}catch(Exception e) {
					  e.printStackTrace(System.err);
					}
				}
			}
			
		}else if(pho_Rep_Stats == 2) {		
			try {

				con=ds.getConnection();
				pstmt=con.prepareStatement(UPDATE_OFF_STATE_STMT);
				
				pstmt.setString(1,mem_Id);
				pstmt.setString(2,photo_No);
				
				count=pstmt.executeUpdate();
				
			}catch(SQLException se) {
				throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
			}finally {
				if(pstmt != null) {
					try {
						pstmt.close();
					}catch(SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				
				if(con != null) {
					try {
					  con.close();
					}catch(Exception e) {
					  e.printStackTrace(System.err);
					}
				}
			}
			
			
		}
		return count;
	}
	
}
