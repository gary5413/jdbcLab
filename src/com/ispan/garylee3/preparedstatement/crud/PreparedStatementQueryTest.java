package com.ispan.garylee3.preparedstatement.crud;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ispan.garylee3.bean.Customer;
import com.ispan.garylee3.bean.Order;
import com.ispan.garylee3.util.JDBCutils;

/**
 * 
 * 使用PreparedStatement實現針對不同表的通用查詢操作
 * @author garylee
 *	
 */
public class PreparedStatementQueryTest {	
	
	@Test
	public void testGetForList() {
		String sql="SELECT id,name,email FROM customers WHERE id < ?";
		List<Customer> list = getForList(Customer.class, sql, 4);
//		System.out.println(list);
		list.forEach(System.out::println);
	}
	
	
	public <T> List<T> getForList(Class<T> clazz,String sql,Object...args){
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
//			創建集合對象
			ArrayList<T> list = new ArrayList<T>();
//			多條紀錄
			while(rs.next()) {
//		這邊複製過來會有問題 改成泛型寫法
				T t=clazz.newInstance(); 
//				
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
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, pstmt, rs);			
		}
		return null;
	}
	
	
	@Test
	public void testGetInstantce() {
		String sql="SELECT id,name,email FROM customers WHERE id= ?";
		Customer customer = getInstance(Customer.class, sql, 1);
		System.out.println(customer);
		
		String sql1="SELECT order_id orderId, order_name orderName FROM `order` WHERE order_id =?";
		Order order = getInstance(Order.class, sql1, 2);
		System.out.println(order);
		
	}
	/**
	 * 針對不同表通用的查詢操作 返回表中單一紀錄
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
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
	

