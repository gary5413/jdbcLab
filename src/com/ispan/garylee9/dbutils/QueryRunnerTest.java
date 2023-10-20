package com.ispan.garylee9.dbutils;

import static org.hamcrest.CoreMatchers.nullValue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import com.ispan.garylee3.bean.Customer;
import com.ispan.garylee8.connectionpool.util.JDBCutils;

/*
 * 是Apache組織提供的一個開源JDBC工具 封裝了針對數據庫增刪改查操作
 */
public class QueryRunnerTest {
// 	測試插入
	@Test
	public void testInsert()  {
		Connection conn=null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnection3();
			String sql="INSERT INTO customers(name,email,birth) VALUES (?,?,?)";
			int insertCount = runner.update(conn, sql, "dbutils","db@gmail.com",new Date());
			System.out.println("新增:"+insertCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}
//	測試搜尋 返回一條紀錄  BeanHandler
	@Test
	public void testQuery1(){
		Connection conn=null;
		try {
			QueryRunner runner = new QueryRunner();
			 conn = JDBCutils.getConnection3();
			String sql="SELECT id,name,email,birth FROM customers WHERE id =?";
			BeanHandler<Customer> handler=new BeanHandler<>(Customer.class);
			Customer customer = runner.query(conn, sql, handler , 29);
			System.out.println(customer.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);
		}
	}
//	測試搜尋 返回多條紀錄 BeanListHandler
	@Test
	public void testQuery2() {
		Connection conn =null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnection3();
			String sql="SELECT id,name,email,birth FROM customers WHERE id < ?";
			BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
			List<Customer> list = runner.query(conn, sql, handler , 29);
			list.forEach(System.out::println);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);			
		}
	}
//	測試搜尋 MapHandler
	@Test
	public void testQuery3() {
		Connection conn =null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnection3();
			String sql="SELECT id,name,email,birth FROM customers WHERE id = ?";
			MapHandler handler = new MapHandler();
			Map<String, Object> map = runner.query(conn, sql, handler, 29);
			System.out.println(map);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);			
		}
	}
	
//	MapListHandler
	@Test
	public void testQuery4() {
		Connection conn =null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnection3();
			String sql="SELECT id,name,email,birth FROM customers WHERE id < ?";
			MapListHandler handler = new MapListHandler();
			List<Map<String, Object>> list = runner.query(conn, sql, handler, 29);
			list.forEach(System.out::println);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);			
		}
	}
	
//	ScalarHandler 用於查詢特殊值
	@Test
	public void testQuery5() {
		Connection conn =null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnection3();
			String sql="SELECT count(*) FROM customers";
			ScalarHandler<Object> handler = new ScalarHandler<>();
			Long count = (Long) runner.query(conn,sql,handler);
			System.out.println(count);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);			
		}
	}
	
	@Test
	public void testQuery6() {
		Connection conn =null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnection3();
			String sql="SELECT MAX(birth) FROM customers";
			ScalarHandler<Object> handler = new ScalarHandler<>();
			Date count = (Date) runner.query(conn,sql,handler);
			System.out.println(count);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);			
		}
	}
//	自定義ResultSetHandler
	@Test
	public void testQuery7() {
		Connection conn =null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCutils.getConnection3();
			String sql="SELECT id,name,email,birth FROM customers WHERE id = ?";
			ResultSetHandler<Customer> handler =new ResultSetHandler<Customer>() {
				@Override
				public Customer handle(ResultSet resultSet) throws SQLException {
					return null;
				}
			};
			Customer customer = runner.query(conn,sql,handler,29);
			System.out.println(customer);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCutils.closeResource(conn, null);			
		}
	}
	
	
}
