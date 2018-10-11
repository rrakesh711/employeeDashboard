package com.abcd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Department
 */
@WebServlet("/Department")
public class Department extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=hrdatabase";
	static final String USER = "hrdb";
	static final String PASS = "1234";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Department() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		List <DepartmentModel> DepartmentList = new ArrayList<DepartmentModel>();
		Connection dbConnection = null;
		Statement stm = null;
		dbConnection = getDBConnection();
		try {
			String dept= "select * from dbo.get_Department_Details();";
			stm = dbConnection.createStatement();
			ResultSet dept_rs=stm.executeQuery(dept);
			while(dept_rs.next())
	        {
				DepartmentModel obj = new DepartmentModel();
	        	obj.setDeptid(Integer.parseInt(dept_rs.getString("deptid")));
	        	obj.setDeptname(dept_rs.getString("deptname"));
	        	DepartmentList.add(obj);
	        }
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally {
			request.setAttribute("displayList", DepartmentList);
			try {
				RequestDispatcher rd = request.getRequestDispatcher("InsDept.jsp");
				       rd.forward(request, response);	
				       }
				catch(Exception e) {
				e.printStackTrace();
				}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);

		Connection dbConnection = null;
		Statement stmt = null;
		
		
		
        
		dbConnection = getDBConnection();
		try {
    
        String deptname=request.getParameter("deptname").toString();
        PreparedStatement psmt = dbConnection.prepareStatement("select deptname from department where deptname=(?)");
        
        System.out.println("query");
        
        psmt.setString(1,deptname);
        ResultSet rs=psmt.executeQuery();
        
        System.out.println("query execute");
        
        if(rs.next()){
        	response.sendRedirect("Error.jsp");
        }else {
        PreparedStatement pstmt = dbConnection.prepareStatement("{call [dbo].[usp_department_insert](?, ?)}");
        pstmt.setString(1,null);
        
        System.out.println("2");
        pstmt.setString(2,(request.getParameter("deptname").toString()));
        
        System.out.println("3");
        
        pstmt.execute();
        //firstname=request.getParameter("firstname");
        System.out.println("4");
        
        pstmt.close();
        
		}
        rs.close();
        
		}
		catch(SQLException e) {
		System.out.println("sql Exception:"+e.getMessage());
	}
		doGet(request, response);
	}
	
	
	/*
	 * db connection seperate method
	 */
	private static Connection getDBConnection() {

		Connection dbConnection = null;

		try {

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		} catch (ClassNotFoundException e) {

			System.out.println(e.getMessage());

		}

		try {

			dbConnection = DriverManager.getConnection(
					 DB_URL, USER,PASS);
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		return dbConnection;

	}

}
