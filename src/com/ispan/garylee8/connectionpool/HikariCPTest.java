package com.ispan.garylee8.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPTest {

	@Test
	public void getConnection() throws SQLException {
//		方式一
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3306/mytestdb");
		config.setUsername("root");
		config.setPassword("5413gary");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		HikariDataSource ds = new HikariDataSource(config);
		Connection conn = ds.getConnection();
		System.out.println(conn);
	}
	
	@Test
	public void getConnection2() throws SQLException {
		HikariConfig config = new HikariConfig("src/hikariCP.properties");
		HikariDataSource ds = new HikariDataSource(config);
		Connection conn = ds.getConnection();
		System.out.println(conn);
	}
}
