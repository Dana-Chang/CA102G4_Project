package com.trip.controller;

import java.io.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import com.attEdit.model.AttractionsEditService;
import com.attractions.model.*;

public class GatPicture extends HttpServlet {
	Connection con;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		res.setContentType("image/gif");
		ServletOutputStream out = res.getOutputStream();
		
		String reqStr = req.getQueryString().substring(0, req.getQueryString().indexOf("="));
		if ("mem_id".equals(reqStr)) {
			
			try {
				String reqPara = req.getParameter("mem_id").trim();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM MEMBER WHERE MEM_ID = '" + reqPara + "'");
				if (rs.next()) {
					BufferedInputStream in = new BufferedInputStream(rs.getBinaryStream("MEM_PHOTO"));
					byte[] buf = new byte[4 * 1024]; // 4K buffer
					int len;
					while ((len = in.read(buf)) != -1) {
						out.write(buf, 0, len);
					}
					in.close();
				} else {
					InputStream in = getServletContext().getResourceAsStream("/front_end/images/trip/NoData/no.png");
					byte[] buf = new byte[in.available()];
					in.read(buf);
					out.write(buf);
					in.close();
				}
				rs.close();
				stmt.close();
			} catch (Exception e) {
				InputStream in = getServletContext().getResourceAsStream("/front_end/images/trip/NoData/null.png");
				byte[] buf = new byte[in.available()];
				in.read(buf);
				out.write(buf);
				in.close();
			}
		}
		
		if("att_no".equals(reqStr)) {
			try {
				String reqPara = req.getParameter("att_no").trim();
				AttractionsService attSvc = new AttractionsService();
				BufferedOutputStream bf = new BufferedOutputStream(out,8192);
				bf.write(attSvc.getPicture(reqPara));
				
//				out.write(attSvc.getPicture(reqPara)); //不透過BufferedOutputStream也可直接output
				
//				Statement stmt = con.createStatement();
//				ResultSet rs = stmt.executeQuery("SELECT * FROM ATTRACTIONS WHERE ATT_NO = '" + reqPara + "'");
//				if (rs.next()) {
//					BufferedInputStream in = new BufferedInputStream(rs.getBinaryStream("ATT_PICTURE"));
//					byte[] buf = new byte[4 * 1024]; // 4K buffer
//					int len;
//					while ((len = in.read(buf)) != -1) {
//						out.write(buf, 0, len);
//					}
//					in.close();
//				} else {
//					InputStream in = getServletContext().getResourceAsStream("/front-end/images/trip/NoData/no.png");
//					byte[] buf = new byte[in.available()];
//					in.read(buf);
//					out.write(buf);
//					in.close();
//				}
//				rs.close();
//				stmt.close();
			} catch (Exception e) {
				InputStream in = getServletContext().getResourceAsStream("/front_end/images/all/8.jpg");
				byte[] buf = new byte[in.available()];
				in.read(buf);
				out.write(buf);
				in.close();
			}
		}
		
		if("attEdit_no".equals(reqStr)) {
			try {
				String reqPara = req.getParameter("attEdit_no").trim();
				AttractionsEditService attEditSvc = new AttractionsEditService();
				BufferedOutputStream bf = new BufferedOutputStream(out,8192);
				bf.write(attEditSvc.getPicture(reqPara));
				
			} catch (Exception e) {
				InputStream in = getServletContext().getResourceAsStream("/front_end/images/all/8.jpg");
				byte[] buf = new byte[in.available()];
				in.read(buf);
				out.write(buf);
				in.close();
			}
		}
		
		if("trip_no".equals(reqStr)) {
			try {
				String reqPara = req.getParameter("trip_no").trim();
				Statement stmt = con.createStatement();
				String sql = "select ATTRACTIONS.ATT_PICTURE"+ 
						" from ATTRACTIONS INNER JOIN"+
					    "(select ATTRACTIONS_TRIP.ATT_NO"+
					    " from ATTRACTIONS_TRIP INNER JOIN"+
					    "(select TRIP.TRIP_NO, TRIP_DAYS.TRIPDAY_NO"+
					    " from TRIP INNER JOIN TRIP_DAYS on(TRIP.TRIP_NO = TRIP_DAYS.TRIP_NO)"+
					    " where TRIP.TRIP_NO ='"+reqPara+"' AND ROWNUM=1) FDLocation"+
					    " on(ATTRACTIONS_TRIP.TRIPDAY_NO = FDLocation.TRIPDAY_NO)"+
					    " where ROWNUM=1) FDATT"+
					    " on(ATTRACTIONS.ATT_NO = FDATT.ATT_NO)"+
					 " where RowNum = 1";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					BufferedInputStream in = new BufferedInputStream(rs.getBinaryStream(1));
					byte[] buf = new byte[4 * 1024]; // 4K buffer
					int len;
					while ((len = in.read(buf)) != -1) {
						out.write(buf, 0, len);
					}
					in.close();
				} else {
					InputStream in = getServletContext().getResourceAsStream("/front_end/images/all/8.jpg");
					byte[] buf = new byte[in.available()];
					in.read(buf);
					out.write(buf);
					in.close();
				}
				rs.close();
				stmt.close();
			} catch (Exception e) {
				InputStream in = getServletContext().getResourceAsStream("/front_end/images/all/8.jpg");
				byte[] buf = new byte[in.available()];
				in.read(buf);
				out.write(buf);
				in.close();
			}
		}
	}

	public void init() throws ServletException {
		Context ctx;
		try {
			ctx = new javax.naming.InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/CA102G4");
			if (ds != null) {
				con = ds.getConnection();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

}
