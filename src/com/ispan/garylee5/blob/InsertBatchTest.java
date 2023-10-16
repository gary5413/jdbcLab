package com.ispan.garylee5.blob;
/*
 * 使用preparedstatement來實作批次處理 更有效率
 * 			DBServer会对预编译语句提供性能优化。因为预编译语句有可能被重复调用，所以语句在被DBServer的 编译器编译后的执行代码被缓存下来，那么下次调用时只要是相同的预编译语句就不需要编译，只要将参 数直接传入编译过的语句执行代码中就会得到执行。 在statement语句中,即使是相同操作但因为数据内容不一样,所以整个语句本身不能匹配,没有缓存语句的意 义.事实是没有数据库会对普通语句编译后的执行代码缓存。这样每执行一次都要对传入的语句编译一次。 (语法检查，语义检查，翻译成二进制命令，缓存)
 * update delete本身就具有批次處理的效果
 * 此時的批次處理主要指的是批次新增 
 * 
 * CREATE TABLE goods1(
	id int primary auto_increment,
	name varchar(25)
	);
 * 
 * 方式一 使用statement
 * 	Connection conn = JDBCUtils.getConnection();
 * 	Statement st = conn.createStatement();
 * 	for(int i= 1;i<=2000;i++){
 * 		String sql = "INSERT INTO goods1(name) VALUES ('name_"+i+"')";
 * 		st.excute(sql);
 * }
 */

import static org.hamcrest.CoreMatchers.nullValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

import com.ispan.garylee3.util.JDBCutils;

public class InsertBatchTest {
	
//	批次處理方式二 使用preparedstatment
	@Test
	public void testInsert() {
		Connection conn=null;
		PreparedStatement ps =null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCutils.getConnection();
			String sql="INSERT INTO goods1(name) VALUES(?)";
			ps = conn.prepareStatement(sql);
			for(int i=1;i<=10000;i++) {
				ps.setObject(1, "name_"+i);
				ps.execute();
			}
			Long end = System.currentTimeMillis();
			System.out.println("所花費時間:"+(end-start)); //10000:6770ms
		}  catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps);
		}
	}

/*
 * 	批次處理方式三 使用addBatch() executeBatch() clearBatch()
 * 
 * mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
* 		?rewriteBatchedStatements=true 写在配置文件的url后面
 */
	@Test
	public void testInsert2() {
		Connection conn=null;
		PreparedStatement ps =null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCutils.getConnection();
			String sql="INSERT INTO goods1(name) VALUES(?)";
			ps = conn.prepareStatement(sql);
			for(int i=1;i<=10000;i++) {
				ps.setObject(1, "name_"+i);
//				1. 屯sql
				ps.addBatch();
				if(i % 500 == 0) {
//					2.執行batch
					ps.executeBatch();
//					3.清空batch
					ps.clearBatch();
				}
			}
			Long end = System.currentTimeMillis();
			System.out.println("所花費時間:"+(end-start)); //10000:887ms
		}  catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps);
		}
	}
	
	/*
	 *  批次處理方式四
	 *  使用交易機制？
	 *  
	 */
	@Test
	public void testInsert3() {
		Connection conn=null;
		PreparedStatement ps =null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCutils.getConnection();
//			設置不允許自動提交資料
			conn.setAutoCommit(false);
			String sql="INSERT INTO goods1(name) VALUES(?)";
			ps = conn.prepareStatement(sql);
			for(int i=1;i<=10000;i++) {
				ps.setObject(1, "name_"+i);
//				1. 屯sql
				ps.addBatch();
				if(i % 500 == 0) {
//					2.執行batch
					ps.executeBatch();
//					3.清空batch
					ps.clearBatch();
				}
			}
//			同意交易
			conn.commit();
			Long end = System.currentTimeMillis();
			System.out.println("所花費時間:"+(end-start)); //10000:869ms
		}  catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, ps);
		}
	}
}
