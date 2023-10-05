package com.ispan.garylee2.statement.crud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

import org.junit.Test;

import com.ispan.garylee3.util.JDBCutils;

/**
 * 使用preparedstatedment 替換statment
 * @author garylee
 * 1.解決statment拼串 解決sql injection問題
 * 2.可以操作blob數據等 檔案 圖片 statemnt做不到
 * 3.可以更有效率的使用批次執行
 */
public class PreparedStatementTest {
	
	@Test
	public void testLogin() {
		Scanner scan = new Scanner(System.in);
		System.out.print("帳號：");
		String userName = scan.nextLine();
		System.out.print("密碼：");
		String password = scan.nextLine(); 
		// SELECT user,password FROM user_table WHERE USER = '1' or ' AND PASSWORD = '
		// ='1' or '1' = '1';
//		String sql = "SELECT user,password FROM user_table WHERE USER = '" + userName + "' AND PASSWORD = '" + password
//				+ "'";
		/**
		 * 占位符起到作用
		 * 預編譯sql語句 
		 * pstmt = conn.prepareStatement(sql); 已經使用過了
		 * 不會改變語句的關係
		 */
		String sql="SELECT user,password FROM user_table WHERE user = ? and password =?";
		User user = getInstance(User.class,sql,userName,password);
		if (user != null) {
			System.out.println("登陆成功!");
		} else {
			System.out.println("用户名或密码错误！");
		}
	}
	
	public <T> T getInstance(Class<T> clazz,String sql,Object...args ) {
		Connection conn =null;
		PreparedStatement pstmt =null;
		ResultSet rs =null;
		try {
			conn = JDBCutils.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
				pstmt.setObject(i+1, args[i]);
			}
			rs = pstmt.executeQuery();
			//獲取結果集元數據 
			ResultSetMetaData metaData = rs.getMetaData();
			//通過ResultSetMetaData 獲取結果集列數
			int columnCount = metaData.getColumnCount();
			if(rs.next()) {
//		這邊複製過來會有問題 改成泛型寫法
				
				T t=clazz.newInstance(); 
				
				for(int i=0;i<columnCount;i++) {
					Object columValue = rs.getObject(i+1);
					//獲取每個列的列名
					//使用getColumnLabel() 獲取別名
					String columnLabel = metaData.getColumnLabel(i+1);
					
//				給clazz物件指定屬性 賦值為value  
//				如何操作 通過反射
					java.lang.reflect.Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, pstmt, rs);			
		}
		return null;
	}
}
