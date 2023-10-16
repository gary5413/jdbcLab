package com.ispan.garylee3.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;

import com.ispan.garylee1.connection.ConnectionTest;


public class JDBCutils {
	/**
	 * @Description 獲得資料庫連接
	 * @author garylee
	 * @return
	 * @throws Exception
	 * 
	 */
	public static Connection getConnection() throws Exception {
		// 讀取jdbc.properties
//		InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
		pros.load(is);
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");

		// 加載驅動
		Class.forName(driverClass);

		// 獲取連接
		Connection conn = DriverManager.getConnection(url, user, password);
//		System.out.println("5" + conn);
		return conn;
	}
	
//	注意 statement 要選上一面點 不要選子接口 
	public static void closeResource(Connection conn,Statement pstmt) {
		//關閉資源
		try {
			if(pstmt !=null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn !=null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
/*
 * 	CustomerForQuery範例
 * 	重新修改 
 * 	多新增關閉ResultSet
 * 	注意 Java.sql的 
 * 不會出現第三方api
 * 
 * 知識點
 * 定義同名方法
 * 多重定義 Overloading
 * 定義參數個數或型別不同的同名方法
 */
	public static void closeResource(Connection conn,Statement pstmt,ResultSet rs) {
		//關閉資源2
		try {
			if(pstmt !=null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn !=null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(rs !=null)
				rs.close();
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}

}
