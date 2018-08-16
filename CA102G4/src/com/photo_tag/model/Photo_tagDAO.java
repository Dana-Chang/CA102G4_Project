package com.photo_tag.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Photo_tagDAO implements Photo_tagDAO_interface {
	
	
	private static final String INSERT_STMT =
			"Insert into PHOTO_TAG (PHOTO_TAG_NO,TAG_CONTENT) VALUES ('PTN'||LPAD(to_char(PHOTO_TAG_seq.NEXTVAL), 6, '0'),?)";
	private static final String UPDATE =
			"UPDATE PHOTO_TAG SET TAG_CONTENT=? WHERE PHOTO_TAG_NO =? ";
	private static final String DELETE_PHOTO_TAG =
			"DELETE FROM PHOTO_TAG WHERE PHOTO_TAG_NO = ?";
	private static final String GET_ONE = 
			"SELECT * FROM PHOTO_TAG WHERE PHOTO_TAG_NO = ?";
	private static final String GET_ALL = 
			"SELECT * FROM PHOTO_TAG ORDER BY PHOTO_TAG_NO";
	
	//找照片標籤內容(地點)
	private static final String get_Keyword = 
			"SELECT O.PHOTO_NO PHOTO_NO,P.PHOTO PHOTO FROM ( SELECT DISTINCT PW.PHOTO_NO FROM PHOTO_WALL PW,PHOTO_TAG_LIST PTL, PHOTO_TAG PT WHERE PW.PHOTO_NO = PTL.PHOTO_NO AND PTL.PHOTO_TAG_NO = PT.PHOTO_TAG_NO AND PT.TAG_CONTENT LIKE ? ) O, PHOTO_WALL P WHERE O.PHOTO_NO = P.PHOTO_NO"; 
			
//			"WITH GG AS" + 
//
//			"(SELECT L.PHOTO_TAG_NO, W.PHOTO_NO, W.PO_TIME, W.PHOTO, W.PHOTO_STA, W.PHOTO_CONTENT FROM PHOTO_TAG_LIST L, PHOTO_WALL W" +
//			"WHERE L.PHOTO_NO = W.PHOTO_NO)," +
//
//			"SS AS" +
//			"(SELECT O.PHOTO_NO, P.PHOTO FROM " +
//			"( SELECT DISTINCT PW.PHOTO_NO FROM PHOTO_WALL PW,PHOTO_TAG_LIST PTL, PHOTO_TAG PT " +
//			"WHERE PW.PHOTO_NO = PTL.PHOTO_NO AND PTL.PHOTO_TAG_NO = PT.PHOTO_TAG_NO AND PT.TAG_CONTENT LIKE ? ) O, " +
//			"PHOTO_WALL P " +
//			"WHERE O.PHOTO_NO = P.PHOTO_NO)" +
//
//			"SELECT A.*,PW.PHOTO FROM(" +
//			"SELECT DISTINCT G.PHOTO_NO,G.PO_TIME,G.PHOTO_STA,G.PHOTO_CONTENT FROM GG G, SS S" +
//			"WHERE G.PHOTO_NO = S.PHOTO_NO) A, PHOTO_WALL PW" +
//			"WHERE A.PHOTO_NO = PW.PHOTO_NO"; 
	
	private static DataSource ds = null;
	
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
		
	@Override
	public void insert(Photo_tagVO photo_tagVO) {
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				con = ds.getConnection();
				
				pstmt = con.prepareStatement(INSERT_STMT);
				
				pstmt.setString(1, photo_tagVO.getTag_Content());
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
	public void update(Photo_tagVO photo_tagVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, photo_tagVO.getTag_Content());
			pstmt.setString(2, photo_tagVO.getPhoto_Tag_No());

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
	public void delete(String photo_Tag_No) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = ds.getConnection();
						
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(DELETE_PHOTO_TAG);

			pstmt.setString(1, photo_Tag_No);

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
	public Photo_tagVO findByPrimaryKey(String photo_Tag_No) {
		Photo_tagVO photo_tagVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ONE);

			pstmt.setString(1,photo_Tag_No);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				photo_tagVO = new Photo_tagVO();
				photo_tagVO.setPhoto_Tag_No(rs.getString("PHOTO_TAG_NO"));
				photo_tagVO.setTag_Content(rs.getString("TAG_CONTENT"));
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
		return photo_tagVO;
	}
	@Override
	public List<Photo_tagVO> getAll() {
		
		List<Photo_tagVO> list = new ArrayList<Photo_tagVO>();
		Photo_tagVO photo_tagVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ALL);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				photo_tagVO = new Photo_tagVO();
				photo_tagVO.setPhoto_Tag_No(rs.getString("PHOTO_TAG_NO"));
				photo_tagVO.setTag_Content(rs.getString("TAG_CONTENT"));
			
				list.add(photo_tagVO);
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
	public List<Photo_tagVO> get_Keyword(String tag_Content) {
		List<Photo_tagVO> list = new ArrayList<Photo_tagVO>();
		Photo_tagVO photo_tagVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(get_Keyword);
			pstmt.setString(1, "%" + tag_Content + "%");
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				count++;
				photo_tagVO = new Photo_tagVO();
				photo_tagVO.setPhoto(rs.getBytes("PHOTO"));
				photo_tagVO.setPhoto_No(rs.getString("PHOTO_NO"));
				photo_tagVO.setTag_Content(tag_Content);
				
				String photoEncoded = Base64.getEncoder().encodeToString(rs.getBytes("Photo"));
				photo_tagVO.setEncoded(photoEncoded);
				
				list.add(photo_tagVO);
			}
			
			System.out.println("count = " + count);

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
