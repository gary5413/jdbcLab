package com.ispan.garylee8.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;


/*
 * 使用C3P0报错：java.lang.NoClassDefFoundError: com/mchange/v2/ser/Indirector
 * 原因：
c3p0数据库连接池的辅助包，没有这个包系统启动的时候会报classnotfoundexception，这是c3p0-0.9.2版本后分离出来的包，0.9.1的时候还是一个包就搞定的
解决办法：
加上mchange-commons-java-0.2.3.4.jar包
 */
public class C3P0Test {
	//方式一
	@Test
	public void testGetConnection() throws Exception {
//		取得c3p0連線池
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" ); //loads the jdbc driver            
		cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/mytestdb" );
		cpds.setUser("root");                                  
		cpds.setPassword("5413gary"); 
//		設定初始連接池連線數量
		cpds.setInitialPoolSize(10);
		Connection conn = cpds.getConnection();
//		System.out.println(conn);
		
	}
//	方式二
	@Test
	public void testGetConnecttion2() throws Exception {
		ComboPooledDataSource cpds = new ComboPooledDataSource("garylee");  
		Connection conn = cpds.getConnection();
		System.out.println(conn);
	      
	}
	      
}
