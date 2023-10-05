package com.ispan.garylee3.preparedstatement.crud;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import org.junit.Test;
import com.ispan.garylee3.util.JDBCutils;

/*
 * 使用PreparedStatement來代替Statement 實現對資料庫CRUD操作
 * 
 * 增刪改
 * 
 * 查
 * 會返回結果 
 * 
 */
public class PreparedStatementUpdateTest {
	@Test
	public void testCommonUpdate() {
//		String sql="DELETE FROM customers WHERE id = ?";
//		update(sql, 3);
		String sql="UPDATE `order` SET order_name = ? WHERE order_id = ?";
		update(sql, "DD","1");
//		You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 
//		抱錯 order 是關鍵字會抱錯 
//		要加上 ` `
	}
	
//	通用的增刪改操作
//	sql中？ 的個數與可變參數的長度一樣 使用
	public void update(String sql,Object ...args) {
		Connection conn =null;
		PreparedStatement pstmt =null;
		try {
			conn = JDBCutils.getConnection();
			pstmt = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
//			小心參數聲明 i從0開始 ?是1
				pstmt.setObject(i+1, args[i]);;
			}
			pstmt.execute();
		} catch  (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, pstmt);			
		}
	}

//	刪除customers表中的一條紀錄
	@Test
	public void testDelete() {
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			// 獲取資料庫連接
			conn = JDBCutils.getConnection();
			// 預編譯sql
			String sql = "DELETE FROM  customers WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			// 定義?
			pstmt.setInt(1, 23);
			// 執行
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
//		關閉資源
			JDBCutils.closeResource(conn, pstmt);
		}
	}

//	修改customers表中的一條紀錄
	@Test
	public void testUpdate() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			// 獲得資料庫連接
			conn = JDBCutils.getConnection();
			// 預編譯sql語句 返回preparedstatement
			String sql = "UPDATE customers SET NAME = ? WHERE id =?";
			pstmt = conn.prepareStatement(sql);
			// 填充站位服
			pstmt.setString(1, "testUpdate20");
			pstmt.setObject(2, 20);
			// 執行
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 資源關閉
			JDBCutils.closeResource(conn, pstmt);
		}
	}

	// 向customer表中添加一條紀錄
	@Test
	// public void testInsert() throws Exception {
	public void testInsert() {
		/*
		 * 把throws exception移除 匡選讀取連接執行的範圍 右鍵 > surround with > try/catch
		 */
		// 讀取jdbc.properties
//				InputStream is=ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
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
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("5" + conn);
			// 預編譯sql語句 返回PreparedStatment
			String sql = "INSERT INTO customers(name,email,birth) VALUES (?,?,?)";
			pstmt = conn.prepareStatement(sql);
			// 定義？
			pstmt.setString(1, "Test1");
			pstmt.setString(2, "ispan@mail.com");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = simpleDateFormat.parse("1000-01-01");
			pstmt.setDate(3, new Date(date.getTime())); // sql.date
			// 執行sql
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 關閉資源
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
