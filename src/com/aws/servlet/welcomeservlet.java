package com.aws.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.aws.dao.DbConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class welcomeservlet
 */
public class welcomeservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public welcomeservlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("uname");
		String password = request.getParameter("pass");
		String tenantname = request.getParameter("tname");
		DbConnection dbconnect=new DbConnection();
		Connection conn=dbconnect.connection();
		PreparedStatement stmt = null;
		ServletContext context = request.getSession().getServletContext();
		//getting tenant id for tenant name 
		try {
			String tentidquery = "select tenantid from s3tenants where tenantname=?";
			stmt = conn.prepareStatement(tentidquery);
			stmt.setString(1, tenantname);
			ResultSet rows = stmt.executeQuery();
			String tenantid =null;
			while(rows.next()){
			 tenantid = rows.getString("tenantid");
			}
		
			// login
			String loginquery = "SELECT username, password, tenantid,userid from s3users";
			ResultSet rs = stmt.executeQuery(loginquery);
			PrintWriter pw = response.getWriter();
			while(rs.next()){
			if (username.equals(rs.getString(1))
					&& password.equals(rs.getString(2))
					&& tenantid.equals(rs.getString(3))) {
				
				context.setAttribute("user", username);
				context.setAttribute("tent", tenantname);
				context.setAttribute("userid",rs.getString(4));
				RequestDispatcher rd = request
						.getRequestDispatcher("/upload.jsp");
				rd.forward(request, response);
			
			} else {
				pw.print("Sorry wrong credentials!");
				RequestDispatcher rd = request
						.getRequestDispatcher("/login.jsp");
				rd.include(request, response);
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
