package com.grp_com.model;

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

public class Grp_comDAO implements Grp_comDAO_interface {
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
			"Insert into GRP_COM (GRP_COM_ID,GRP_ID,MEM_ID,GRP_COM) VALUES (LPAD(to_char(GRP_COM_seq.NEXTVAL), 6, '0'),?,?,?)";
	private static final String UPDATE =
			"UPDATE GRP_COM SET GRP_ID=?,MEM_ID=?,GRP_COM=? WHERE GRP_COM_ID =? ";
	private static final String DELETE_GRP_COM =
			"DELETE FROM GRP_COM WHERE GRP_COM_ID = ?";
	private static final String GET_ONE_STMT = 
			"SELECT * FROM GRP_COM WHERE GRP_COM_ID = ?";
	private static final String GET_ALL = 
			"SELECT * FROM GRP_COM ORDER BY GRP_COM_ID";
	
	
	
	@Override
	public void insert(Grp_comVO grp_comVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, grp_comVO.getGrp_Id());
			pstmt.setString(2, grp_comVO.getMem_Id());
			pstmt.setString(3, grp_comVO.getGrp_Com());
			
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
	public void update(Grp_comVO grp_comVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, grp_comVO.getGrp_Id());
			pstmt.setString(2, grp_comVO.getMem_Id());
			pstmt.setString(3, grp_comVO.getGrp_Com());
			pstmt.setString(4, grp_comVO.getGrp_Com_Id());
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
	public void delete(String grp_Com_Id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();
			
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(DELETE_GRP_COM);

			pstmt.setString(1, grp_Com_Id);

			pstmt.executeUpdate();

			con.commit();
			con.setAutoCommit(true);
			
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
	public Grp_comVO findByPrimaryKey(String grp_Com_Id) {
		
		Grp_comVO grp_comVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, grp_Com_Id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				grp_comVO = new Grp_comVO();
				grp_comVO.setGrp_Com_Id(rs.getString("GRP_COM_ID"));
				grp_comVO.setGrp_Id(rs.getString("GRP_ID"));
				grp_comVO.setMem_Id(rs.getString("MEM_ID"));
				grp_comVO.setGrp_Com(rs.getString("GRP_COM"));
				
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
		return grp_comVO;
	}
		

	@Override
	public List<Grp_comVO> getAll() {
	List<Grp_comVO> list = new ArrayList<Grp_comVO>();
	Grp_comVO grp_comVO = null;
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	try {
		con = ds.getConnection();
		pstmt = con.prepareStatement(GET_ALL);
		
		rs = pstmt.executeQuery();

		while (rs.next()) {
			
			grp_comVO = new Grp_comVO();
			grp_comVO.setGrp_Com_Id(rs.getString("GRP_COM_ID"));
			grp_comVO.setGrp_Id(rs.getString("GRP_ID"));
			grp_comVO.setMem_Id(rs.getString("MEM_ID"));
			grp_comVO.setGrp_Com(rs.getString("GRP_COM"));
			list.add(grp_comVO);
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

}
