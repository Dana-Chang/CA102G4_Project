package com.ad.model;

import java.io.*;
import java.sql.*;
import java.util.*;

public class AdJDBCDAO implements AdDAO_interface{
	private static final String DRIVER="oracle.jdbc.driver.OracleDriver";
	private static final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	private static final String USER="CA102G4";
	private static final String PASSWORD="12345678";
	
	private static final String ADDAD_STMT=
			"INSERT INTO AD(AD_ID,AD_TITLE,AD_TEXT,AD_LINK,AD_PIC,AD_PREADDTIME,AD_PREOFFTIME,AD_STAT,CLICKCOUNT)VALUES('AD'||LPAD(TO_CHAR(AD_SEQ.NEXTVAL),6,'0'),?,?,?,?,?,?,0,0)";
	private static final String UPDATEAD_STMT=
			"UPDATE AD SET AD_TITLE=?,AD_TEXT=?,AD_LINK=?,AD_PIC=?,AD_PREADDTIME=?,AD_PREOFFTIME=? WHERE AD_ID=?";
	private static final String DELAD_STMT=
			"DELETE FROM AD WHERE AD_ID=?";
	/***更動廣告為馬上上架***/
	private static final String UPDATE_ADONSTAT_STMT=
			"UPDATE AD SET AD_STAT=1,AD_PREADDTIME=SYSDATE,AD_ACTADDTIME=SYSDATE,AD_PREOFFTIME=?,AD_ACTOFFTIME=? WHERE AD_ID=?";
	/***更動廣告為下架***/
	private static final String UPDATE_ADOFFSTAT_STMT=
			"UPDATE AD SET AD_STAT=0,AD_PREOFFTIME=SYSDATE,AD_ACTOFFTIME=SYSDATE WHERE AD_ID=?";
	private static final String UPDATE_CLICK_STMT=
			"UPDATE AD SET CLICKCOUNT=CLICKCOUNT+1 WHERE AD_ID=?";
	private static final String FINDNEW_STMT=
			"SELECT * FROM AD WHERE AD_STAT=1 ORDER BY AD_ACTADDTIME DESC";
	private static final String FINDHOT_STMT=
			"SELECT * FROM AD WHERE AD_STAT=1 ORDER BY CLICKCOUNT DESC";
	private static final String FINDALL_STMT=
			"SELECT * FROM AD ORDER BY AD_PREADDTIME DESC";;
	private static final String FINDONE_BYID=
			"SELECT * FROM AD WHERE AD_ID=?";
	//新增廣告
	@Override
	public int addAD(AdVO ad) {
		int count=0;
		Connection con = null ;
		PreparedStatement pstmt= null;
		
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt=con.prepareStatement(ADDAD_STMT);
			
			pstmt.setString(1,ad.getAd_Title());
			pstmt.setString(2,ad.getAd_Text());
			pstmt.setString(3,ad.getAd_Link());
			pstmt.setBytes(4,ad.getAd_Pic());
			pstmt.setTimestamp(5, ad.getAd_PreAddTime());
			pstmt.setTimestamp(6, ad.getAd_PreOffTime());
			
			count=pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("資料庫無法載入驅動"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt=con.prepareStatement(UPDATEAD_STMT);
			
			pstmt.setString(1,ad.getAd_Title());
			pstmt.setString(2,ad.getAd_Text());
			pstmt.setString(3,ad.getAd_Link());
			pstmt.setBytes(4,ad.getAd_Pic());
			pstmt.setTimestamp(5, ad.getAd_PreAddTime());
			pstmt.setTimestamp(6, ad.getAd_PreOffTime());
			pstmt.setString(7,ad.getAd_ID());
			
			count=pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("資料庫無法載入驅動"+ce.getMessage());
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
				Class.forName(DRIVER);
				con=DriverManager.getConnection(URL, USER, PASSWORD);
				pstmt=con.prepareStatement(UPDATE_ADONSTAT_STMT);
				
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
				
				count=pstmt.executeUpdate();
				
			}catch(ClassNotFoundException ce) {
				throw new RuntimeException("資料庫無法載入驅動"+ce.getMessage());
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
				Class.forName(DRIVER);
				con=DriverManager.getConnection(URL, USER, PASSWORD);
				pstmt=con.prepareStatement(UPDATE_ADOFFSTAT_STMT);
				
				pstmt.setString(1,id);
				
				count=pstmt.executeUpdate();
				
			}catch(ClassNotFoundException ce) {
				throw new RuntimeException("資料庫無法載入驅動"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt=con.prepareStatement(UPDATE_CLICK_STMT);
			
			pstmt.setString(1,id);	
			
			count=pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("資料庫無法載入驅動"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
			pstmt=con.prepareStatement(DELAD_STMT);
			
			pstmt.setString(1,id);	
			
			count=pstmt.executeUpdate();
			
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("資料庫無法載入驅動"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL,USER,PASSWORD);
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
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("資料庫無法載入驅動"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL,USER,PASSWORD);
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
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException("資料庫無法載入驅動"+ce.getMessage());
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
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
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
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException ("資料庫無法載入驅動"+ce.getMessage());
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

	//查詢某支廣告
	@Override
	public AdVO findByIdAD(String id) {
		Connection con = null ;
		PreparedStatement pstmt= null;
		ResultSet rs= null;
		AdVO ad = null;
		
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL, USER, PASSWORD);
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
				ad.setAd_ActAddTime(rs.getTimestamp("AD_ACTADDTIME"));
				ad.setAd_ActOffTime(rs.getTimestamp("AD_ACTOFFTIME"));
				ad.setAd_Stat(rs.getInt("AD_STAT"));
				ad.setClickCount(rs.getInt("CLICKCOUNT"));
				ad.setAd_PreAddTime(rs.getTimestamp("AD_PREADDTIME"));
				ad.setAd_ActOffTime(rs.getTimestamp("AD_PREOFFTIME"));
			}
		}catch(ClassNotFoundException ce) {
			throw new RuntimeException ("資料庫無法載入驅動"+ce.getMessage());
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
		
		return ad;
	}

	
	//JDBC測試時，把本地圖片存入資料庫
	public static byte[] getPicByteArray(String path) throws IOException {
		File file = new File(path);
		FileInputStream fis=new FileInputStream(file);
		ByteArrayOutputStream baso = new ByteArrayOutputStream();
		byte[] buffer =new byte[fis.available()];
		int i ; 
		while((i = fis.read(buffer)) != -1) {
			baso.write(buffer,0,i);
		}
		baso.close();
		fis.close();
		
		return baso.toByteArray() ; 
	}

	//我是Main方法
	public static void main(String args[]){
		AdJDBCDAO dao = new AdJDBCDAO();
		
		try {
//			新增廣告
			AdVO ad = new AdVO();
			ad.setAd_Title("探索蒙古國．體驗新鮮事");
			ad.setAd_Text("探索草漠之境，前行藍天之國，入住蒙古包度假村、品嚐鮮美石頭烤羊、騎馬慢賞草原美景，盡情體驗無法複製的蒙古文化。");
			ad.setAd_Link("https://www.everfuntravel.com/promo/area/asia/mongolia/");
			byte[] pic = getPicByteArray("WebContent/front_end/images/ad08.jpg");
			ad.setAd_Pic(pic);
			int count = dao.addAD(ad);
			System.out.println("新增共"+count+"筆");

		
			//修改廣告資訊
//			AdVO ad1 = new AdVO();
//			ad1.setAd_ID("AD000003");
//			ad1.setAd_Title("修改後，探索蒙古國．體驗新鮮事");
//			ad1.setAd_Text("修改後，探索草漠之境，前行藍天之國，入住蒙古包度假村、品嚐鮮美石頭烤羊、騎馬慢賞草原美景，盡情體驗無法複製的蒙古文化。");
//			ad1.setAd_Link("https://www.everfuntravel.com/promo/area/asia/mongolia/");
//			byte[] pic1 = getPicByteArray("WebContent/front_end/images/ad07.jpg");
//			ad1.setAd_Pic(pic1);
//			int count1 = dao.updateAD(ad1);
//			System.out.println("更新共"+count1+"筆");
			
//			//修改廣告狀態(上架/下架也會更新實際上下架時間)
//			
//			
//			//修改廣告點擊率
//			int count2 =dao.updateAD_Click("AD000001");
//			System.out.println("更新廣告點擊率，共"+count2+"筆");
//			
//			//刪除廣告
////			int count_del =dao.delectAD("AD000002");
////			System.out.println("刪除廣告，共"+count_del+"筆");
//			
//			//查詢已上架熱門廣告
//			List<AdVO> hotlist=dao.findHotAD();
//			for(AdVO i:hotlist) {
//				System.out.print(i.getAd_ID()+",");
//				System.out.print(i.getAd_Title()+",");
//				System.out.print(i.getAd_Text()+",");
//				System.out.print(i.getAd_Link()+",");
//				System.out.print(i.getAd_Pic()+",");
//				System.out.print(i.getClickCount());
//			}
//			System.out.println();
//			//查詢已上架的最新廣告
//			List<AdVO> newlist = dao.findNewAD();
//			for(AdVO i : newlist) {
//				System.out.print(i.getAd_ID());
//				System.out.print(i.getAd_Title());
//				System.out.print(i.getAd_Text());
//				System.out.print(i.getAd_Link());
//				System.out.print(i.getAd_Pic());
//				System.out.print(i.getAd_ActAddTime());
//			}
//			System.out.println();
//			//查詢已上架或未上架廣告
//			List<AdVO> onAD = dao.findOnOffAD(0);
//			for(AdVO i : onAD) {
//				System.out.print(i.getAd_ID());
//				System.out.print(i.getAd_Title());
//				System.out.print(i.getAd_Text());
//				System.out.print(i.getAd_Link());
//				System.out.print(i.getAd_Pic());
//				System.out.print(i.getAd_Stat());
//			}
//			System.out.println();
//			List<AdVO> offAD = dao.findOnOffAD(1);
//			for(AdVO i : offAD) {
//				System.out.print(i.getAd_ID());
//				System.out.print(i.getAd_Title());
//				System.out.print(i.getAd_Text());
//				System.out.print(i.getAd_Link());
//				System.out.print(i.getAd_Pic());
//				System.out.print(i.getAd_Stat());
//			}
//			
//			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	






}
