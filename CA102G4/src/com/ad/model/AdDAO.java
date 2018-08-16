package com.ad.model;
/*使用JNDI*/
import java.io.*;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AdDAO implements AdDAO_interface{
	
	private static DataSource ds=null;
	
	static {
		try {
			Context ctx = new javax.naming.InitialContext();
			ds=(DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
		}catch(NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	//新增廣告，會設定上下架時間使用的
	private static final String ADDAD_STMT=
			"INSERT INTO AD(AD_ID,AD_TITLE,AD_TEXT,AD_LINK,AD_PIC,AD_PREADDTIME,AD_PREOFFTIME,AD_STAT,CLICKCOUNT)VALUES('AD'||LPAD(TO_CHAR(AD_SEQ.NEXTVAL),6,'0'),?,?,?,?,?,?,0,0)";
	private static final String UPDATEAD_STMT=
			"UPDATE AD SET AD_TITLE=?,AD_TEXT=?,AD_LINK=?,AD_PIC=?,AD_PREADDTIME=?,AD_PREOFFTIME=? WHERE AD_ID=?";
	private static final String DELAD_STMT=
			"DELETE FROM AD WHERE AD_ID=?";
	/***更動廣告為馬上上架***/
	private static final String UPDATE_ADONSTAT_STMT=
			"UPDATE AD SET AD_STAT=1,AD_PREADDTIME=SYSDATE,AD_ACTADDTIME=SYSDATE,AD_PREOFFTIME=?,AD_ACTOFFTIME=? WHERE AD_ID=?";
	/***更動廣告為馬上下架***/
	private static final String UPDATE_ADOFFSTAT_STMT=
			"UPDATE AD SET AD_STAT=0,AD_PREOFFTIME=SYSDATE,AD_ACTOFFTIME=SYSDATE WHERE AD_ID=?";
	
	private static final String UPDATE_CLICK_STMT=
			"UPDATE AD SET CLICKCOUNT=CLICKCOUNT+1 WHERE AD_ID=?";
	private static final String FINDNEW_STMT=
			"SELECT * FROM AD WHERE AD_STAT=1 ORDER BY AD_ACTADDTIME DESC";
	private static final String FINDHOT_STMT=
			"SELECT * FROM AD WHERE AD_STAT=1 ORDER BY CLICKCOUNT DESC";
	private static final String FINDALL_STMT=
			"SELECT * FROM AD ORDER BY AD_PREADDTIME DESC";
	private static final String FINDONE_BYID=
			"SELECT * FROM AD WHERE AD_ID=?";
	
	//新增廣告
	@Override
	public int addAD(AdVO ad) {
		int count=0;
		Connection con = null ;
		PreparedStatement pstmt= null;
		
		try {
			
			con=ds.getConnection();
			pstmt=con.prepareStatement(ADDAD_STMT);

			pstmt.setString(1,ad.getAd_Title());
			pstmt.setString(2,ad.getAd_Text());
			pstmt.setString(3,ad.getAd_Link());
			pstmt.setBytes(4,ad.getAd_Pic());
			pstmt.setTimestamp(5, ad.getAd_PreAddTime());
			pstmt.setTimestamp(6, ad.getAd_PreOffTime());

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
		
		return count;
	}

	//修改廣告資訊(必須在下架狀態才能修改)
	@Override
	public int updateAD(AdVO ad) {
		int count=0;
		Connection con = null ;
		PreparedStatement pstmt= null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(UPDATEAD_STMT);
			
			pstmt.setString(1,ad.getAd_Title());
			pstmt.setString(2,ad.getAd_Text());
			pstmt.setString(3,ad.getAd_Link());
			pstmt.setBytes(4,ad.getAd_Pic());
			pstmt.setTimestamp(5, ad.getAd_PreAddTime());
			pstmt.setTimestamp(6, ad.getAd_PreOffTime());
			pstmt.setString(7,ad.getAd_ID());
			
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
		
		return count;
	}

	
	//修改廣告狀態(上架/下架也會更新實際上下架時間)
	@Override
	public int updateAD(String id,Integer stat,AdVO advo) {
		int count=0;
		Connection con = null ;
		PreparedStatement pstmt= null;
		
		if(stat == 1) {
			try {
				con=ds.getConnection();
				pstmt=con.prepareStatement(UPDATE_ADONSTAT_STMT);
				
				
				if(advo.getAd_PreOffTime() !=null) {
					//假設上架時間大於原有預計下架時間時，把預計下架時間跟實際下架時間清空
					if(System.currentTimeMillis() >= advo.getAd_PreOffTime().getTime() ) {
						pstmt.setTimestamp(1,null);
						pstmt.setTimestamp(2,null);
						pstmt.setString(3,id);
					}else {
						pstmt.setTimestamp(1,advo.getAd_PreOffTime());
						pstmt.setTimestamp(2,null);
						pstmt.setString(3,id);
					}
				}else {
					pstmt.setTimestamp(1,advo.getAd_PreOffTime());
					pstmt.setTimestamp(2,null);
					pstmt.setString(3,id);
					
				}



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
			
		}else if(stat == 0) {		
			try {
				con=ds.getConnection();
				pstmt=con.prepareStatement(UPDATE_ADOFFSTAT_STMT);
				
				pstmt.setString(1,id);
				
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
	
	//修改廣告點擊率
	@Override
	public int updateAD_Click(String id) {
		int count=0;
		Connection con = null ;
		PreparedStatement pstmt= null;
		
		try {
			
			con=ds.getConnection();
			pstmt=con.prepareStatement(UPDATE_CLICK_STMT);
			
			pstmt.setString(1,id);	
			
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
		
		return count;
	}
	
	//刪除廣告
	@Override
	public int delectAD(String id) {
		int count=0;
		Connection con = null ;
		PreparedStatement pstmt= null;
		
		try {
			
			con=ds.getConnection();
			pstmt=con.prepareStatement(DELAD_STMT);
			
			pstmt.setString(1,id);	
			
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
		
		return count;
	}

	//查詢已上架熱門廣告
	@Override
	public List<AdVO> findHotAD() {
		Connection con = null ;
		PreparedStatement pstmt= null ;
		ResultSet rs=null;
		List<AdVO> hlist= new ArrayList<>();
		AdVO ad = null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDHOT_STMT);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				ad=new AdVO();
				ad.setAd_ID(rs.getString("AD_ID"));
				ad.setAd_Title(rs.getString("AD_TITLE"));
				ad.setAd_Text(rs.getString("AD_TEXT"));
				ad.setAd_Link(rs.getString("AD_LINK"));
				ad.setAd_Pic(rs.getBytes("AD_PIC"));
				hlist.add(ad);
			}
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				}catch(SQLException e){
					e.printStackTrace(System.err);
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				}catch(SQLException e){
					e.printStackTrace(System.err);
				}
			}
			if(con!=null) {
				try {
					con.close();
				}catch(SQLException e){
					e.printStackTrace(System.err);
				}
			}
			
		}
		return hlist;
	}

	//查詢已上架的最新廣告
	@Override
	public List<AdVO> findNewAD() {
		Connection con = null ;
		PreparedStatement pstmt= null ;
		ResultSet rs=null;
		List<AdVO> nlist= new ArrayList<>();
		AdVO ad = null;
		
		try {
			
			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDNEW_STMT);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				ad=new AdVO();
				ad.setAd_ID(rs.getString("AD_ID"));
				ad.setAd_Title(rs.getString("AD_TITLE"));
				ad.setAd_Text(rs.getString("AD_TEXT"));
				ad.setAd_Link(rs.getString("AD_LINK"));
				ad.setAd_Pic(rs.getBytes("AD_PIC"));
				ad.setAd_ActAddTime(rs.getTimestamp("AD_ACTADDTIME"));
				nlist.add(ad);
			}
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				}catch(SQLException e){
					e.printStackTrace(System.err);
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				}catch(SQLException e){
					e.printStackTrace(System.err);
				}
			}
			if(con!=null) {
				try {
					con.close();
				}catch(SQLException e){
					e.printStackTrace(System.err);
				}
			}
			
		}
		return nlist;
	}

	//查詢所有廣告
	@Override
	public List<AdVO> findAllAD() {
		Connection con = null ;
		PreparedStatement pstmt= null;
		ResultSet rs= null;
		List<AdVO> adlist= new ArrayList<>();
		AdVO ad = null;
		
		try {
			
			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDALL_STMT);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				ad=new AdVO();
				ad.setAd_ID(rs.getString("AD_ID"));
				ad.setAd_Title(rs.getString("AD_TITLE"));
				ad.setAd_Text(rs.getString("AD_TEXT"));
				ad.setAd_Link(rs.getString("AD_LINK"));
				ad.setAd_Pic(rs.getBytes("AD_PIC"));
				ad.setAd_PreAddTime(rs.getTimestamp("AD_PREADDTIME"));
				ad.setAd_PreOffTime(rs.getTimestamp("AD_PREOFFTIME"));
				ad.setAd_ActAddTime(rs.getTimestamp("AD_ACTADDTIME"));
				ad.setAd_ActOffTime(rs.getTimestamp("AD_ACTOFFTIME"));
				ad.setAd_Stat(rs.getInt("AD_STAT"));
				ad.setClickCount(rs.getInt("CLICKCOUNT"));
				adlist.add(ad);
			}
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();				
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return adlist;
	}

	//查詢某一項廣告
	@Override
	public AdVO findByIdAD(String id) {
		Connection con = null ;
		PreparedStatement pstmt= null ;
		ResultSet rs=null;
		AdVO ad = null;
		
		try {

			con=ds.getConnection();
			pstmt=con.prepareStatement(FINDONE_BYID);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				ad=new AdVO();
				ad.setAd_ID(rs.getString("AD_ID"));
				ad.setAd_Title(rs.getString("AD_TITLE"));
				ad.setAd_Text(rs.getString("AD_TEXT"));
				ad.setAd_Link(rs.getString("AD_LINK"));
				ad.setAd_Pic(rs.getBytes("AD_PIC"));
				ad.setAd_PreAddTime(rs.getTimestamp("AD_PREADDTIME"));
				ad.setAd_PreOffTime(rs.getTimestamp("AD_PREOFFTIME"));
				ad.setAd_ActAddTime(rs.getTimestamp("AD_ACTADDTIME"));
				ad.setAd_ActOffTime(rs.getTimestamp("AD_ACTOFFTIME"));
				ad.setAd_Stat(rs.getInt("AD_STAT"));
				ad.setClickCount(rs.getInt("CLICKCOUNT"));
			}
		}catch(SQLException se) {
			throw new RuntimeException("資料庫發生錯誤"+se.getMessage());
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				}catch(SQLException e){
					e.printStackTrace(System.err);
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				}catch(SQLException e){
					e.printStackTrace(System.err);
				}
			}
			if(con!=null) {
				try {
					con.close();
				}catch(SQLException e){
					e.printStackTrace(System.err);
				}
			}
			
		}
		return ad;
	}
	
}
