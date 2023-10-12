package com.ispan.garylee1.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.junit.Test;

public class ConnectionTestMssql {
//	@Test
//	public void getConnetion5() throws Exception {
//		//讀取jdbc.properties
//		InputStream is=ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
//		Properties pros=new Properties();
//		pros.load(is);
//		String user =pros.getProperty("user");
//		String password =pros.getProperty("password");
//		String url =pros.getProperty("url");
//		String driverClass =pros.getProperty("driverClass");
//		
//		//加載驅動
//		Class.forName(driverClass);
//		
//		//獲取連接
//		Connection conn=DriverManager.getConnection(url,user,password);
//		System.out.println("5"+conn);
//	}
}
