package com.ispan.garylee5.blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

import com.ispan.garylee3.util.JDBCutils;

public class BlobTest {
	
	/*
	 * 向customer表中插入Blob類型
	 */
	@Test
	public void testInsert() {
		Connection conn=null;
		PreparedStatement ps=null;
		try {
			conn = JDBCutils.getConnection();
			String sql="INSERT INTO customers(name,email,birth,photo)values(?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, "Gary");
			ps.setObject(2, "garylee@gmail.com");
			ps.setObject(3, "1990-04-27");
			FileInputStream is = new FileInputStream(new File("resource/test.png"));
			ps.setObject(4, is);
			ps.execute();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps);
			
		}
	}
	
}
