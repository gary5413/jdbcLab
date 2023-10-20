package com.ispan.garylee8.connectionpool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
	
	
/**
 * 使用DBCP連線池
 */
	private static DataSource source;
	static {
		try {
			Properties properties = new Properties();
			FileInputStream fis = new FileInputStream(new File("src/dbcp.properties"));
			properties.load(fis);
			source = BasicDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection2() throws Exception{
//		Properties properties = new Properties();
//		FileInputStream fis = new FileInputStream(new File("src/dbcp.properties"));
//		properties.load(fis);
//		創建一個DBCP連線池
//		BasicDataSource source = BasicDataSourceFactory.createDataSource(properties);
		Connection conn = source.getConnection();
//		System.out.println(conn);
		return conn;
	}
	/*
	 * 使用hikariCP連線池
	 */
	private static HikariDataSource ds;
	static {
		HikariConfig config = new HikariConfig("src/hikariCP.properties");
		ds = new HikariDataSource(config);
	}
	public static Connection getConnection3() throws SQLException {
//		HikariConfig config = new HikariConfig("src/hikariCP.properties");
//		HikariDataSource ds = new HikariDataSource(config);
		Connection conn = ds.getConnection();
//		System.out.println(conn);
		return conn;
	}
	
	
	//關閉資源
	public static void closeResource(Connection conn,Statement pstmt) {
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
	//關閉資源2
	public static void closeResource(Connection conn,Statement pstmt,ResultSet rs) {
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
		}
	}
	
	/*
	 * 使用dbutils 資源關閉
	 */
	public static void closeResource1(Connection conn,Statement pstmt,ResultSet rs) {
//		try {
//			DbUtils.close(conn);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			DbUtils.close(pstmt);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			DbUtils.close(rs);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		DbUtils.closeQuietly(conn);
		DbUtils.closeQuietly(pstmt);
		DbUtils.closeQuietly(rs);
	}
}
