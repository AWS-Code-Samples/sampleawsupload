package com.aws.dao;

import java.sql.DriverManager;

public class DbConnection {

	public java.sql.Connection connection() {
		java.sql.Connection conn = null;
		try {
			String JDBC_DRIVER = "com.mysql.jdbc.Driver";
			String DB_URL = "jdbc:mysql://localhost/aws_tables";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, "root", "");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

}
