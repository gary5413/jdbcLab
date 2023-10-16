package com.ispan.garylee6.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import com.ispan.garylee3.util.JDBCutils;

/*
 * 針對user_table來說
 * AA用戶給BB用戶轉帳100
 * 
 * UPDATE user_table SET balance = balance - 100 WHERE user='AA'
 * UPDATE user_table SET balance = balance + 100 WHERE user='BB'
 * 
 */
public class TransactionTest {
	@Test
	public void testUpdate1() {
		String sql1="UPDATE user_table SET balance = balance - 100 WHERE user = ?";
		update(sql1,"AA");
//		模擬異常
		System.out.println(10/0);
//		發現只有AA-100 BB沒有+100
		
		String sql2="UPDATE user_table SET balance = balance + 100 WHERE user = ?";
		update(sql2,"BB");
		System.out.println("轉帳成功");
	}
//	通用增刪改 v1.0
/*
 * 	java.lang.Exception: No tests found matching....
 * 	此方法不能加@Test註釋
 */
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
//		@Test
//		public int update(String sql,Object ...args) {
//		Connection conn =null;
//		PreparedStatement pstmt =null;
//		try {
//			conn = JDBCutils.getConnection();
//			pstmt = conn.prepareStatement(sql);
//			for(int i=0;i<args.length;i++) {
//				pstmt.setObject(i+1, args[i]);;
//			}
//			return pstmt.executeUpdate();
//		} catch  (Exception e) {
//			e.printStackTrace();
//		}finally {
//			JDBCutils.closeResource(conn, pstmt);			
//		}
//		return 0;
//	}
}

	
	
	
	


