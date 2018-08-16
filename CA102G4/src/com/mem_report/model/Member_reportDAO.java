package com.mem_report.model;

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

public class Member_reportDAO implements Member_reportDAO_interface{
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
			"Insert into MEMBER_REPORT (MEM_ID_REPORT,MEM_ID_REPORTED,REPORT_TIME,REPORT_REASON,MEM_REP_STA) VALUES (?,?,SYSDATE,?,?)";
	private static final String GET_ALL_STMT =
			"SELECT * FROM MEMBER_REPORT ORDER BY MEM_ID_REPORT,MEM_ID_REPORTED";
	private static final String GET_ONE_STMT =
			"SELECT * FROM MEMBER_REPORT WHERE MEM_ID_REPORT =? AND MEM_ID_REPORTED = ?";
	private static final String DELETE_MEMBER_REPORT = 
			"DELETE FROM MEMBER_REPORT WHERE MEM_ID_REPORT= ? AND MEM_ID_REPORTED= ?";
	private static final String UPDATE = 
			"UPDATE MEMBER_REPORT SET REPORT_TIME= ?,REPORT_REASON= ?,MEM_REP_STA= ? WHERE MEM_ID_REPORT =? AND MEM_ID_REPORTED = ?";
	
	//更改會員審核狀態

	private static final String UPDATE_ON_STATE_STMT=
			"UPDATE MEMBER_REPORT SET MEM_REP_STA = 1 WHERE MEM_ID_REPORT=? AND MEM_ID_REPORTED=?";
	private static final String UPDATE_OFF_STATE_STMT=
			"UPDATE MEMBER_REPORT SET MEM_REP_STA = 2 WHERE MEM_ID_REPORT=? AND MEM_ID_REPORTED=?";
		
	@Override
	public void insert(Member_reportVO member_reportVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, member_reportVO.getMem_Id_report());
			pstmt.setString(2, member_reportVO.getMem_Id_reported());
			pstmt.setString(3, member_reportVO.getReport_Reason());
			pstmt.setInt(4, member_reportVO.getMem_Rep_Sta());
			pstmt.executeUpdate();
			
		}catch (SQLException se) {
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
	public void update(Member_reportVO member_reportVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			
			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE);
			
			

			pstmt.setTimestamp(1, member_reportVO.getReport_Time());
			pstmt.setString(2, member_reportVO.getReport_Reason());
			pstmt.setInt(3, member_reportVO.getMem_Rep_Sta());
			pstmt.setString(4, member_reportVO.getMem_Id_report());
			pstmt.setString(5, member_reportVO.getMem_Id_reported());
			
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
	public void delete(String mem_Id_Report, String mem_Id_Reported) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();


			pstmt = con.prepareStatement(DELETE_MEMBER_REPORT);

			pstmt.setString(1, mem_Id_Report);
			pstmt.setString(2, mem_Id_Reported);

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
	public Member_reportVO findByPrimaryKey(String mem_Id_Report, String mem_Id_Reported) {
		
		Member_reportVO member_reportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, mem_Id_Report);
			pstmt.setString(2, mem_Id_Reported);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				member_reportVO = new Member_reportVO();
				member_reportVO.setMem_Id_report(rs.getString("MEM_ID_REPORT"));
				member_reportVO.setMem_Id_reported(rs.getString("MEM_ID_REPORTED"));
				member_reportVO.setReport_Time(rs.getTimestamp("REPORT_TIME"));
				member_reportVO.setReport_Reason(rs.getString("REPORT_REASON"));
				member_reportVO.setMem_Rep_Sta(rs.getInt("MEM_REP_STA"));
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
		return member_reportVO;
	}

	@Override
	public List<Member_reportVO> getAll() {
		
		List<Member_reportVO> list = new ArrayList<Member_reportVO>();
		Member_reportVO member_reportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ALL_STMT);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				member_reportVO = new Member_reportVO();
				member_reportVO.setMem_Id_report(rs.getString("MEM_ID_REPORT"));
				member_reportVO.setMem_Id_reported(rs.getString("MEM_ID_REPORTED"));
				member_reportVO.setReport_Time(rs.getTimestamp("REPORT_TIME"));
				member_reportVO.setReport_Reason(rs.getString("REPORT_REASON"));
				member_reportVO.setMem_Rep_Sta(rs.getInt("MEM_REP_STA"));
				list.add(member_reportVO); // Store the row in the list
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
	public int update_review_State(String mem_Id_Report,String mem_Id_Reported, Integer mem_Rep_Sta) {
		Connection con = null ;
		PreparedStatement pstmt= null;
		int count=0;
		if(mem_Rep_Sta == 1) {
			try {
				con=ds.getConnection();
				pstmt=con.prepareStatement(UPDATE_ON_STATE_STMT);
				
				pstmt.setString(1,mem_Id_Report);
				pstmt.setString(2,mem_Id_Reported);
				
				
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
			
		}else if(mem_Rep_Sta == 2) {		
			try {

				con=ds.getConnection();
				pstmt=con.prepareStatement(UPDATE_OFF_STATE_STMT);
				
				pstmt.setString(1,mem_Id_Report);
				pstmt.setString(2,mem_Id_Reported);
				
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
