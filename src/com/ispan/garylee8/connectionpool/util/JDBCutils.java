package com.ispan.garylee8.connectionpool.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCutils {
	/*
	 * 使用c3p0連線池
	 */
//	連線池只需要提供一個
	private static ComboPooledDataSource cpds = new ComboPooledDataSource("garylee");  
	public static Connection getConnection() throws Exception {
	Connection conn = cpds.getConnection();
	return conn;
}
//	下面方法還是會創造新的
//	public static Connection getConnection() throws Exception {
//		ComboPooledDataSource cpds = new ComboPooledDataSource("garylee");  
//		Connection conn = cpds.getConnection();
//		return conn;
//	}
	
	private static void close(Connection conn) throws SQLException {
		conn.close();
	}
}
