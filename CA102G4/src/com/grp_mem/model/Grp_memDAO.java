package com.grp_mem.model;

import java.io.IOException;
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


public class Grp_memDAO implements Grp_memDAO_interface {
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
			"Insert into GRP_MEM (GRP_ID,MEM_ID,GRP_LEADER,GRP_CHECKIN) VALUES (?,?,?,0)";
	private static final String UPDATE = 
			"UPDATE GRP_MEM SET GRP_LEADER= ? WHERE GRP_ID =? AND MEM_ID = ?";
	private static final String DELETE_GRP_MEM = 
			"DELETE FROM GRP_MEM WHERE GRP_ID =? AND MEM_ID = ?";
	private static final String GET_ONE = 
			"SELECT * FROM GRP_MEM WHERE GRP_ID = ? AND MEM_ID=?";
	private static final String GET_ALL = 
			"SELECT * FROM GRP_MEM ORDER BY GRP_ID,MEM_ID";
	
	//取得參加揪團會員的資料
	private static final String GET_JOIN_MEMBER = 
			"SELECT MEM_PHOTO,MEM_NAME,MEM_PHONE FROM MEMBER JOIN GRP_MEM ON GRP_MEM.MEM_ID = MEMBER.MEM_ID WHERE GRP_ID = ?";
	
	//揪團主更改參加者狀態
	private static final String UPDATE_ON_STATE_STMT=
		"UPDATE GRP_MEM SET GRP_LEADER= ? WHERE GRP_ID =? AND MEM_ID = ?";
	private static final String UPDATE_OFF_STATE_STMT=
		"UPDATE GRP_MEM SET GRP_LEADER= ? WHERE GRP_ID =? AND MEM_ID = ?";	
	
	//取得參加者(GRP_LEADER=1)的會員資料
	private static final String GET_CHECK_MEMBER = 
		"SELECT * FROM GRP_MEM WHERE GRP_ID = ? AND GRP_LEADER = 1 ";
	
	
	
	@Override
	public void insert(Grp_memVO grp_memVO) {
		System.out.println("資料庫新增");
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();

			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, grp_memVO.getGrp_Id());
			
			System.out.println("資料庫新增GRP_ID="+ grp_memVO.getGrp_Id());

			pstmt.setString(2, grp_memVO.getMem_Id());
			
			System.out.println("資料庫新增MEM_ID="+ grp_memVO.getMem_Id());

			pstmt.setString(3, grp_memVO.getGrp_Leader());
			
			System.out.println("資料庫新增LEADER="+ grp_memVO.getGrp_Leader());

			pstmt.executeUpdate();
			System.out.println("資料庫新增成功");

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
	public void update(Grp_memVO grp_memVO) {
		System.out.println("DAO的大師兄咧");

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			
			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, grp_memVO.getGrp_Leader());
			
			System.out.println("DAO的大師兄getGrp_Id"+grp_memVO.getGrp_Leader());
			
			pstmt.setString(2, grp_memVO.getGrp_Id());
			
			System.out.println("DAO的大師兄getGrp_Id"+grp_memVO.getGrp_Id());

			pstmt.setString(3, grp_memVO.getMem_Id());
			
			System.out.println("DAO的大師兄getGrp_Id"+grp_memVO.getMem_Id());



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
	public void delete(String grp_Id, String mem_Id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();

			pstmt = con.prepareStatement(DELETE_GRP_MEM);

			pstmt.setString(1, grp_Id);
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
	public Grp_memVO findByPrimaryKey(String grp_Id, String mem_Id) {

		Grp_memVO grp_memVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ONE);

			pstmt.setString(1, grp_Id);
			pstmt.setString(2, mem_Id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				grp_memVO = new Grp_memVO();
				grp_memVO.setGrp_Id(rs.getString("GRP_ID"));
				grp_memVO.setMem_Id(rs.getString("MEM_ID"));
				grp_memVO.setGrp_Leader(rs.getString("GRP_LEADER"));	
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
		return grp_memVO;
	}
		

	@Override
	public List<Grp_memVO> getAll() {
		List<Grp_memVO> list = new ArrayList<Grp_memVO>();
		Grp_memVO grp_memVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				grp_memVO = new Grp_memVO();
				grp_memVO.setGrp_Id(rs.getString("GRP_ID"));
				grp_memVO.setMem_Id(rs.getString("MEM_ID"));
				grp_memVO.setGrp_Leader(rs.getString("GRP_LEADER"));
				list.add(grp_memVO);
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
	public List<Grp_memVO> getAll_join_mem(String grp_Id) {
		List<Grp_memVO> list = new ArrayList<Grp_memVO>();
		Grp_memVO grp_memVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(GET_JOIN_MEMBER);
			
			pstmt.setString(1, grp_Id);
			
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				grp_memVO = new Grp_memVO();
				grp_memVO.setGrp_Id(rs.getString("GRP_ID"));
				grp_memVO.setMem_Id(rs.getString("MEM_ID"));
				grp_memVO.setGrp_Leader(rs.getString("GRP_LEADER"));
				list.add(grp_memVO);
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
	
	//更改參加者的狀態(接受或拒絕)
	@Override
	public int update_State(String grp_Id, String mem_Id,String grp_Leader) {
		Connection con = null ;
		PreparedStatement pstmt= null;
		int count=0;
		if(grp_Leader.equals("0")) {
			try {
				con=ds.getConnection();
				pstmt=con.prepareStatement(UPDATE_ON_STATE_STMT);
				
				pstmt.setString(1,grp_Id);
				pstmt.setString(2,mem_Id);

				
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
			
		}else if(grp_Leader.equals("1")) {		
			try {

				con=ds.getConnection();
				pstmt=con.prepareStatement(UPDATE_OFF_STATE_STMT);
				
				pstmt.setString(1,grp_Id);
				pstmt.setString(2,mem_Id);
				
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

	@Override
	public List<Grp_memVO> getAll_check_mem(String grp_Id,String grp_Leader) {
			List<Grp_memVO> list = new ArrayList<Grp_memVO>();
			Grp_memVO grp_memVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				con = ds.getConnection();
				
				pstmt = con.prepareStatement(GET_CHECK_MEMBER);
				
				pstmt.setString(1, grp_Id);
				//因為指令已經寫死 grp_Leader = 1  所以這邊註解
//				pstmt.setString(2, grp_Leader);
				
				
				rs = pstmt.executeQuery();

				while (rs.next()) {
					
					grp_memVO = new Grp_memVO();
					grp_memVO.setGrp_Id(rs.getString("GRP_ID"));
					grp_memVO.setMem_Id(rs.getString("MEM_ID"));
					grp_memVO.setGrp_Leader(rs.getString("GRP_LEADER"));
					list.add(grp_memVO);
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
