package com.photo_wall.model;

import java.sql.*;

import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class Photo_wallDAO implements Photo_wallDAO_interface {
	
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
			"Insert into PHOTO_WALL (PHOTO_NO,MEM_ID,PO_TIME,PHOTO,PHOTO_STA) VALUES (?,?,?,?,?)";
	private static final String UPDATE = 
			"UPDATE PHOTO_WALL SET MEM_ID=?,PO_TIME=?,PHOTO=?,PHOTO_STA=? WHERE PHOTO_NO = ?";
	private static final String DELETE_PHOTO_WALL = 
			"DELETE FROM PHOTO_WALL WHERE PHOTO_NO = ?";
	private static final String GET_ONE =
			"SELECT * FROM PHOTO_WALL WHERE PHOTO_NO = ?";
	
	//取得照片牆所有資訊
	private static final String GET_ALL =
			"SELECT * FROM PHOTO_WALL ORDER BY PHOTO_NO";
	
	//更改照片牆的狀態
	private static final String UPDATE_ON_STATE_STMT=
		"UPDATE PHOTO_WALL SET PHOTO_STA = 1 WHERE MEM_ID=? AND PHOTO_NO=?";
	private static final String UPDATE_OFF_STATE_STMT=
		"UPDATE PHOTO_WALL SET PHOTO_STA = 2 WHERE MEM_ID=? AND PHOTO_NO=?";
	
	//***************為了讀取某位會員的照片***********************
	private static final String GET_ALL_BYMEMID=
		"SELECT * FROM PHOTO_WALL WHERE MEM_ID = ? AND PHOTO_STA = 1";
	//用照片
	
	//取得照片牆所有資訊
	@Override
	public List<Photo_wallVO> getAll() {
			List<Photo_wallVO> list = new ArrayList<Photo_wallVO>();
			Photo_wallVO photo_wallVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				con = ds.getConnection();

				pstmt = con.prepareStatement(GET_ALL);
				
				rs = pstmt.executeQuery();

				while (rs.next()) {
					photo_wallVO = new Photo_wallVO();
					photo_wallVO.setPhoto_No(rs.getString("PHOTO_NO"));
					photo_wallVO.setMem_Id(rs.getString("MEM_ID"));
					photo_wallVO.setPo_Time(rs.getTimestamp("PO_TIME"));
					photo_wallVO.setPhoto(rs.getBytes("PHOTO"));
					photo_wallVO.setPhoto_Sta(rs.getInt("PHOTO_STA"));
					photo_wallVO.setPhoto_Content(rs.getString("PHOTO_CONTENT"));
					String photoEncoded = Base64.getEncoder().encodeToString(rs.getBytes("PHOTO"));
					photo_wallVO.setEncoded(photoEncoded);
					
					list.add(photo_wallVO);

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

	
	//***************為了讀取某位會員的照片***********************
	public List<Photo_wallVO> getAll_ByMemID(String mem_Id){
		List<Photo_wallVO> list = new ArrayList<>();
		
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
		con = ds.getConnection();
		pstmt  = con.prepareStatement(GET_ALL_BYMEMID);
		pstmt.setString(1, mem_Id);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			
			Photo_wallVO pw = new Photo_wallVO();
			pw.setPhoto_No(rs.getString("PHOTO_NO"));
			pw.setMem_Id(rs.getString("MEM_ID"));
			pw.setPo_Time(rs.getTimestamp("PO_TIME"));
			pw.setPhoto(rs.getBytes("PHOTO"));
			pw.setPhoto_Sta(rs.getInt("PHOTO_STA"));
			list.add(pw);
		}
		
		}catch(SQLException se) {
			throw new RuntimeException ("資料庫發生錯誤"+se.getMessage());
		}finally{
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
	public void insert(Photo_wallVO photo_wallVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, photo_wallVO.getPhoto_No());
			pstmt.setString(2, photo_wallVO.getMem_Id());
			pstmt.setTimestamp(3, photo_wallVO.getPo_Time());
			pstmt.setBytes(4, photo_wallVO.getPhoto());
			pstmt.setInt(5, photo_wallVO.getPhoto_Sta());

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
	public void update(Photo_wallVO photo_wallVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, photo_wallVO.getMem_Id());
			pstmt.setTimestamp(2, photo_wallVO.getPo_Time());
			pstmt.setBytes(3, photo_wallVO.getPhoto());
			pstmt.setInt(4, photo_wallVO.getPhoto_Sta());
			pstmt.setString(5, photo_wallVO.getPhoto_No());

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
	public void delete(String photo_No) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(DELETE_PHOTO_WALL);

			pstmt.setString(1, photo_No);

			pstmt.executeUpdate();

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
	
	// 在詳細照片牆顯示會員內容
	@Override
	public Photo_wallVO findByPrimaryKey(String photo_No) {
		Photo_wallVO photo_wallVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ONE);

			pstmt.setString(1, photo_No);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				photo_wallVO = new Photo_wallVO();
				photo_wallVO.setPhoto_No(rs.getString("PHOTO_NO"));
				photo_wallVO.setMem_Id(rs.getString("MEM_ID"));
				photo_wallVO.setPo_Time(rs.getTimestamp("PO_TIME"));
				photo_wallVO.setPhoto(rs.getBytes("PHOTO"));
				photo_wallVO.setPhoto_Sta(rs.getInt("PHOTO_STA"));
				photo_wallVO.setPhoto_Content(rs.getString("PHOTO_CONTENT"));
				String photoEncoded = Base64.getEncoder().encodeToString(rs.getBytes("PHOTO"));
				photo_wallVO.setEncoded(photoEncoded);

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
		return photo_wallVO;
	}


	@Override
	public int update_State(String mem_Id, String photo_No, Integer photo_Sta) {
		Connection con = null ;
		PreparedStatement pstmt= null;
		int count=0;
		if(photo_Sta == 1) {
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
			
		}else if(photo_Sta == 2) {		
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
