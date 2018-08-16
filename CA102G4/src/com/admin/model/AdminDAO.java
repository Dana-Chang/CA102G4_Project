package com.admin.model;

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

import com.mem.model.MemberVO;

public class AdminDAO implements AdminDAO_interface {
	
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_ADMIN =
			"Insert into ADMIN (ADMIN_ID,ADMIN_ACCOUNT,ADMIN_PASSWORD,ADMIN_NAME,ADMIN_MAIL,ADMIN_PHONE) VALUES ('ADMIN'||LPAD(to_char(ADMIN_seq.NEXTVAL), 3, '0'),?,?,?,?,?)";
	private static final String update_Admin =
			"UPDATE ADMIN SET ADMIN_PASSWORD=?,ADMIN_NAME=?,ADMIN_MAIL=?,ADMIN_PHONE=? WHERE ADMIN_ID =? ";
	private static final String DELETE_ADMIN =
			"DELETE FROM ADMIN WHERE ADMIN_ID = ?";
	private static final String GET_ONE = 
			"SELECT * FROM ADMIN WHERE ADMIN_ID = ?";
	private static final String GET_ALL = 
			"SELECT * FROM ADMIN ORDER BY ADMIN_ID";
	private static final String login_Admin = 
			"SELECT * FROM ADMIN WHERE ADMIN_ACCOUNT= ? AND ADMIN_PASSWORD = ?";
	private static final String getAll_Keyword = 
			"SELECT * FROM ADMIN WHERE UPPER(ADMIN_NAME) LIKE UPPER(?) OR UPPER(ADMIN_NAME) LIKE UPPER(?) ORDER BY ADMIN_ID ";
	

	

	@Override
	public void insert_Admin(AdminVO adminVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_ADMIN);
			
			pstmt.setString(1, adminVO.getAdmin_Account());
			pstmt.setString(2, adminVO.getAdmin_Password());
			pstmt.setString(3, adminVO.getAdmin_Name());
			pstmt.setString(4, adminVO.getAdmin_Mail());
			pstmt.setString(5, adminVO.getAdmin_Phone());
			pstmt.executeUpdate();
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public List<AdminVO> getAll_keyword(String admin_Name) {
		
		List<AdminVO> list = new ArrayList<AdminVO>();
		AdminVO adminVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(getAll_Keyword);
			pstmt.setString(1, "%" + admin_Name + "%");
			pstmt.setString(2, "%" + admin_Name + "%");

			rs = pstmt.executeQuery();

			while (rs.next()) {
				
			adminVO = new AdminVO();
			adminVO.setAdmin_Id(rs.getString("ADMIN_ID"));
			adminVO.setAdmin_Account(rs.getString("ADMIN_ACCOUNT"));
			adminVO.setAdmin_Password(rs.getString("ADMIN_PASSWORD"));
			adminVO.setAdmin_Name(rs.getString("ADMIN_NAME"));
			adminVO.setAdmin_Mail(rs.getString("ADMIN_MAIL"));
			adminVO.setAdmin_Phone(rs.getString("ADMIN_PHONE"));		
			list.add(adminVO);
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
		return list;
	}

	@Override
	public void update_Admin(AdminVO adminVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(update_Admin);
			
			pstmt.setString(1, adminVO.getAdmin_Password());
			pstmt.setString(2, adminVO.getAdmin_Name());
			pstmt.setString(3, adminVO.getAdmin_Mail());
			pstmt.setString(4, adminVO.getAdmin_Phone());
			pstmt.setString(5, adminVO.getAdmin_Id());
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
	public void delete_Admin(String admin_Id) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();
			
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(DELETE_ADMIN);

			pstmt.setString(1, admin_Id);

			pstmt.executeUpdate();

			con.commit();
			con.setAutoCommit(true);
			
			// Handle any SQL errors

		} catch (SQLException se) {
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
	public AdminVO findByPrimaryKey(String admin_Id) {
		AdminVO adminVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE);

			pstmt.setString(1, admin_Id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				adminVO = new AdminVO();
				adminVO.setAdmin_Id(rs.getString("ADMIN_ID"));
				adminVO.setAdmin_Account(rs.getString("ADMIN_ACCOUNT"));
				adminVO.setAdmin_Password(rs.getString("ADMIN_PASSWORD"));
				adminVO.setAdmin_Name(rs.getString("ADMIN_NAME"));
				adminVO.setAdmin_Mail(rs.getString("ADMIN_MAIL"));
				adminVO.setAdmin_Phone(rs.getString("ADMIN_PHONE"));		
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
		return adminVO;
	}

	@Override
	public List<AdminVO> getAll() {
		List<AdminVO> list = new ArrayList<AdminVO>();
		AdminVO adminVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
			adminVO = new AdminVO();
			adminVO.setAdmin_Id(rs.getString("ADMIN_ID"));
			adminVO.setAdmin_Account(rs.getString("ADMIN_ACCOUNT"));
			adminVO.setAdmin_Password(rs.getString("ADMIN_PASSWORD"));
			adminVO.setAdmin_Name(rs.getString("ADMIN_NAME"));
			adminVO.setAdmin_Mail(rs.getString("ADMIN_MAIL"));
			adminVO.setAdmin_Phone(rs.getString("ADMIN_PHONE"));		
			list.add(adminVO);
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
		return list;
	}

	@Override
	public AdminVO login_Admin(String admin_Account, String admin_Password) {
		
		AdminVO adminVO_login = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(login_Admin);
			pstmt.setString(1, admin_Account);
			pstmt.setString(2, admin_Password);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				adminVO_login = new AdminVO();
				adminVO_login.setAdmin_Id(rs.getString("ADMIN_ID"));
				adminVO_login.setAdmin_Account(rs.getString("ADMIN_ACCOUNT"));
				adminVO_login.setAdmin_Password(rs.getString("ADMIN_PASSWORD"));
				adminVO_login.setAdmin_Name(rs.getString("ADMIN_NAME"));
				adminVO_login.setAdmin_Mail(rs.getString("ADMIN_MAIL"));
				adminVO_login.setAdmin_Phone(rs.getString("ADMIN_PHONE"));	
				pstmt.executeUpdate();

			}		
			// Handle any driver errors
		} 
		catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} 
		finally {
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
		return adminVO_login;
		}

	


}
