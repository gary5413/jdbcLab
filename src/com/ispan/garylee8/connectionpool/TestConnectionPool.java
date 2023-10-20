package com.ispan.garylee8.connectionpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Test;

import com.ispan.garylee8.connectionpool.util.JDBCutils;



public class TestConnectionPool {
	
	@Test
	public void getCustomerById()  {
		try {
			Connection conn = JDBCutils.getConnection3();
			String sql="SELECT id,name,email,birth FROM customers WHERE id= ? ";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, 29);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println("name:"+rs.getString("name"));
				System.out.println("email:"+rs.getString("email"));
				System.out.println("birth:"+rs.getDate("birth"));
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
