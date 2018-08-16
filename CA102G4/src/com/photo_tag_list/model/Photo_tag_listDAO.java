package com.photo_tag_list.model;

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

public class Photo_tag_listDAO implements Photo_tag_listDAO_interface {
	
	//用照片標籤編號找照片(編號)
	private static final String GET_PHOTO_NO =
			"SELECT * FROM PHOTO_TAG_LIST WHERE PHOTO_NO = ?";

	//以下還沒用到
	private static final String INSERT_STMT =
			"Insert into PHOTO_TAG_LIST (PHOTO_TAG_NO,PHOTO_NO) VALUES"
			+ " (?,?)";
	private static final String DELETE_PHOTO_TAG_LIST = 
			"DELETE FROM PHOTO_TAG_LIST WHERE PHOTO_TAG_NO= ?";
	
	private static final String GET_ONE_STMT =
			"SELECT * FROM PHOTO_TAG_LIST WHERE PHOTO_TAG_NO = ?";
	
	private static final String GET_ALL_STMT =
			"SELECT * FROM PHOTO_TAG_LIST ORDER BY PHOTO_TAG_NO";
	//
	
	
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	//用照片標籤編號找照片(編號)

	@Override
	public List<Photo_tag_listVO> getAll_Photo_No(String photo_No) {
		List<Photo_tag_listVO> list = new ArrayList<Photo_tag_listVO>();
		Photo_tag_listVO photo_tag_listVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

			try {
				con = ds.getConnection();

				pstmt = con.prepareStatement(GET_PHOTO_NO);
				
				pstmt.setString(1, photo_No);
				
				rs = pstmt.executeQuery();

				while (rs.next()) {
					photo_tag_listVO = new Photo_tag_listVO();
					photo_tag_listVO.setPhoto_Tag_No(rs.getString("PHOTO_TAG_NO"));
					photo_tag_listVO.setPhoto_No(rs.getString("PHOTO_NO"));

					list.add(photo_tag_listVO); // Store the row in the list
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
	public Photo_tag_listVO findByPrimaryKey(String photo_Tag_No,String photo_No) {
		Photo_tag_listVO photo_tag_listVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, photo_Tag_No);
			pstmt.setString(2, photo_No);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				photo_tag_listVO = new Photo_tag_listVO();
				photo_tag_listVO.setPhoto_Tag_No(rs.getString("PHOTO_TAG_NO"));
				photo_tag_listVO.setPhoto_No(rs.getString("PHOTO_NO"));
				
				System.out.println(photo_Tag_No+photo_No);

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
		return photo_tag_listVO;
	}
	
	
	@Override
	public void insert(Photo_tag_listVO photo_tag_listVO) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void delete(String photo_Tag_No) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Photo_tag_listVO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
