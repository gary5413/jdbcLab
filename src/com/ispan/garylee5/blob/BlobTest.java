package com.ispan.garylee5.blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.ispan.garylee3.bean.Customer;
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
			ps.setObject(1, "Gary1");
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
	
	//查詢customer Blob類型
	@Test
	public void testQuery() {
		Connection conn =null;
		PreparedStatement ps =null;
		InputStream is =null;
		FileOutputStream fos =null;
		ResultSet rs=null;
		try {
			conn = JDBCutils.getConnection();
			String sql="SELECT id,name,email,birth,photo FROM customers WHERE id =?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, 28);
			rs = ps.executeQuery();
			if(rs.next()) {
				//方式一
//			int id = rs.getInt(1);
//			String name = rs.getString(2);
//			String email = rs.getString(3);
//			Date birth = rs.getDate(4);
				//方式二
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date birth = rs.getDate("birth");
				Customer customer = new Customer(id,name,email,birth);
				System.out.println(customer.toString());
//			將blob類型下載下來
				Blob photo = rs.getBlob("photo");
				is = photo.getBinaryStream();
				fos = new FileOutputStream("resource/test.jpg");
				byte[] buffer =new byte[1024];
				int len;
				while((len =is.read(buffer)) != -1) {
					fos.write(buffer,0,len);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(is !=null) {
					is.close();					
				}
			} catch (IOException e) {
			}
			try {
				if(is !=null) {
					fos.close();
				}
			} catch (IOException e) {
			}
			JDBCutils.closeResource(conn, ps,rs);			
		}
	}
	
}
