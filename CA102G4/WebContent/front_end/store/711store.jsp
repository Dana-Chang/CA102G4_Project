<%@page import="java.sql.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
if(request.getParameter("store_711_dist_id")!=null) 
{
    int id=Integer.parseInt(request.getParameter("store_711_dist_id")); //get store_711_dist_id from select_page.jsp page with function state_change() through ajax and store in id variable.
        
    try
    {
    	Class.forName("oracle.jdbc.driver.OracleDriver"); //load driver
        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","CA102G4","12345678"); //create connection
            
        PreparedStatement pstmt=null ; //create statement
                
        pstmt=con.prepareStatement("select * from STORE_711 where STORE_711_DIST_ID =? "); //sql select query
        pstmt.setInt(1,id);
        ResultSet rs=pstmt.executeQuery(); //execute query and set in resultset object rs.
        %>         
            <option selected="selected">--選擇711地址--</option>
        <%    
        while(rs.next())
        {
        %> 
            <option value="<%=rs.getInt("STORE_711_ID")%>">
                <%=rs.getString("STORE_711_NAME")%>  <%=rs.getString("STORE_711_ADDR")%>
            </option>
        <%
        }
  
  con.close(); //close connection 
    }
    catch(Exception e)
    {
        out.println(e);
    }
}
%>