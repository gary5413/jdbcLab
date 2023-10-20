package com.ispan.garylee8.connectionpool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

public class DbcpTest {
//		方式一
		@Test
		public void testGetConnection() throws SQLException {
//			創建DBCP連線池 
			BasicDataSource source=new BasicDataSource();
//			基本設定
			source.setDriverClassName("com.mysql.cj.jdbc.Driver");
			source.setUrl("jdbc:mysql://localhost:3306/mytestdb");
			source.setUsername("root");
			source.setPassword("5413gary");
//			設定其他連線池設定
			source.setInitialSize(10);
			source.setMaxTotal(10);
			
			Connection conn = source.getConnection();
			System.out.println(conn);
		}
	
//		方式二
		@Test
		public void testGetConnection2() throws  Exception {
			Properties properties = new Properties();
//			加載方式一
//			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
//			加載方式二
			FileInputStream fis = new FileInputStream(new File("src/dbcp.properties"));
			properties.load(fis);
			BasicDataSource source = BasicDataSourceFactory.createDataSource(properties);
			Connection conn = source.getConnection();
			System.out.println(conn);
			
		}
		
}
