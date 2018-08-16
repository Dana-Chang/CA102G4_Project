package com.question.controller;

import java.io.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class QuestionGetPicture extends HttpServlet {
	
	Connection con;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		res.setContentType("image/gif");
		ServletOutputStream out = res.getOutputStream();
		
		try {
			Statement stmt = con.createStatement();
					
			String mem_id =req.getParameter("mem_id").trim();
			ResultSet rs = stmt.executeQuery(
				"SELECT * FROM MEMBER WHERE mem_id='"+mem_id+"'");
					
				if (rs.next()) {
					BufferedInputStream in = new BufferedInputStream(rs.getBinaryStream("MEM_PHOTO"));
					byte[] buf = new byte[4 * 1024]; // 4K buffer
					int len;
					while ((len = in.read(buf)) != -1) {
						out.write(buf, 0, len);
					}
					in.close();
				} else {
					InputStream in = getServletContext().getResourceAsStream("/front_end/images/all/t4.jpg");
					byte[] buf = new byte[in.available()];
					in.read(buf);
					out.write(buf);
					in.close();
				}
				rs.close();
				stmt.close();
			} catch (Exception e) {
				InputStream in = getServletContext().getResourceAsStream("/front_end/images/all/t4.jpg");
				byte[] buf = new byte[in.available()];
				in.read(buf);
				out.write(buf);
				in.close();
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
