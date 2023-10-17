package com.ispan.garylee6.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ispan.garylee2.statement.crud.User;
import com.ispan.garylee3.util.JDBCutils;

/*
 * 1. 什麼是資料庫事務
 * 事務 一組邏輯操作單元 使資料從一種狀態轉到另一種狀態
 *  	> 一組邏輯操作單元 一個或多DML
 * 2. 事務處理的原則 要所有事務都完成 就commit
 * 		如果過程中有出現異常 就rollback
 * 
 * 3. 資料一但commit 就不可rollback
 * 
 * 4. 哪些操作會導致資料自動提交
 * 		DDL 操作一但執行 都會自動提交
 * 			我們可以通過 set autocommit =false 方式取消 DML
 * 		DML 默認情況下 一但執行 就會自動提交
 * 			我們可以通過 set autocommit =false 方式取消 DML
 * 		默認在關閉連接時,會自動提交
 * 
 *  事務的ACID
 *  	
		1. 原子性(Atomicity) 原子性是指事务是一个不可分割的工作单位，事务中的操作要么都发生，要么都不发 生。
		2. 一致性(Consistency) 事务必须使数据库从一个一致性状态变换到另外一个一致性状态。
		3. 隔离性(Isolation) 事务的隔离性是指一个事务的执行不能被其他事务干扰，即一个事务内部的操作及使用的
  			数据对并发的其他事务是隔离的，并发执行的各个事务之间不能互相干扰。
		4. 持久性(Durability) 持久性是指一个事务一旦被提交，它对数据库中数据的改变就是永久性的，接下来的其
  			他操作和数据库故障不应该对其有任何影响。
  			
  	 交易的幾個問題
  	 	髒讀
  	 		 T1讀取已被T2更新但未commit資料 之後若T2rollback T1內容就是臨時且無效的
  	 	不可重複讀
  	 		T1 T2讀取同一個資料 然後T2更新資料後 T1在讀取直就不同了
  	 	幻讀
  	 		T1 T2讀取同一個資料 然後T2插入新的資料後 如果T1在讀取同一資料就會多出幾行
 */
public class TransactionTest {
	
//*************************以下考慮交易*************************************************
	
//	@Test
//	public void testTransactionSelect() throws Exception {
//		Connection conn = JDBCutils.getConnection();
//		System.out.println(conn.getTransactionIsolation());
//		
////		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//		conn.setAutoCommit(false);
//		
//		String sql="SELECT user user,password password,balance FROM user_table WHERE user =?";
//		User user = getInstance(conn, User.class, sql, "CC");
//		System.out.println(user.toString());
//	}
	
//	@Test
//	public void testTransactionUpdate() throws Exception {
//		Connection conn = JDBCutils.getConnection();
//		
//		conn.setAutoCommit(false);
//		
//		String sql="UPDATE user_table SET balance =? WHERE user =?";
//		update2(conn,sql, 5000,"CC");
//		Thread.sleep(15000);
//		System.out.println("修改結束");
//		
//	}
	
//	通用查詢 用於返回一條紀錄 考慮上交易
	public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object...args ) {
		PreparedStatement pstmt =null;
		ResultSet rs =null;
		try {
			conn = JDBCutils.getConnection();
			pstmt = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
				pstmt.setObject(i+1, args[i]);
			}
			rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			if(rs.next()) {
				T t=clazz.newInstance(); 
				for(int i=0;i<columnCount;i++) {
					Object columValue = rs.getObject(i+1);
					String columnLabel = metaData.getColumnLabel(i+1);
					java.lang.reflect.Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(null, pstmt, rs);			
		}
		return null;
	}
	
	
	@Test
	public void testUpdateWithTX()  {
		Connection conn=null;
		try {
			conn = JDBCutils.getConnection();
			System.out.println(conn.getAutoCommit());
//			設置取消自動提交功能
			conn.setAutoCommit(false);
			String sql1="UPDATE user_table SET balance = balance - 100 WHERE user = ?";
			update2(conn,sql1,"AA");
			
//		模擬異常
//			System.out.println(10/0);
			
			String sql2="UPDATE user_table SET balance = balance + 100 WHERE user = ?";
			update2(conn,sql2,"BB");
			
//			提交
			conn.commit();
			System.out.println("轉帳成功");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
				System.out.println("轉帳失敗");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			try {
				/*
				 * 若此时 Connection 没有被关闭，还可能被重复使用，
				 * 则需要恢复其自动提交状态 setAutoCommit(true)。
				 * 尤其是在使用数据库连接池技术时，执行close()方法前，建议恢复自动提交状态。
				 */
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JDBCutils.closeResource(conn, null);
		}
	}
	
	
//	通用增刪改 v2.0 考慮交易功能
	/*
	 * 1.外面傳連接進來
	 * 2.也不用創建連接
	 * 3.不要關掉連接
	 */
	public int update2(Connection conn,String sql,Object ...args) {
	PreparedStatement pstmt =null;
	try {
		pstmt = conn.prepareStatement(sql);
		for(int i=0;i<args.length;i++) {
			pstmt.setObject(i+1, args[i]);;
		}
		return pstmt.executeUpdate();
	} catch  (Exception e) {
		e.printStackTrace();
	}finally {
		JDBCutils.closeResource(null, pstmt);			
	}
	return 0;
}
	
//**********************以下未考慮交易************************************************
	/*
	 * 針對user_table來說
	 * AA用戶給BB用戶轉帳100
	 * 
	 * UPDATE user_table SET balance = balance - 100 WHERE user='AA'
	 * UPDATE user_table SET balance = balance + 100 WHERE user='BB'
	 * 
	 */
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

}

	
	
	
	


