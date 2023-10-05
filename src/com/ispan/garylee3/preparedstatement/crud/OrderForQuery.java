package com.ispan.garylee3.preparedstatement.crud;
/**
 * 
 * @author garylee
 * 針對order表單 查詢操作
 */

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Test;

import com.ispan.garylee3.bean.Order;
import com.ispan.garylee3.util.JDBCutils;

public class OrderForQuery {
	/**
	 * 		此通用方法會有一個小bug會報錯 因為屬性名跟表的列名不一樣
	 * 		解決方式 
	 * 		sql 起別名
	 * 		ResultSetMetaData 不使用此方法取列名getColumnName()
	 * 		改此方法取別名getColumnLabel()
	 * 		如果sql中沒有給字串取別名 getColumnLabel()就是取列名
	 */
	@Test
	public void testOrderForQuery() {
//		String sql="SELECT order_id,order_name,order_date FROM `order` WHERE order_id =? ";
		String sql="SELECT order_id orderId,order_name orderName,order_date orderDate FROM `order` WHERE order_id =? ";
		Order order = orderForQuery(sql,1 );
		System.out.println(order);
	}
	
	/**
	 * 通用查詢操作
	 * @return
	 * @throws Exception 
	 */
	public Order orderForQuery(String sql,Object ...args)  {
		Connection conn =null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			conn = JDBCutils.getConnection();
			pstmt = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
				pstmt.setObject(i+1, args[i]);
			}
			// 執行獲取結果集
			rs = pstmt.executeQuery();
//		獲取結果集的元數據
			ResultSetMetaData metaData = rs.getMetaData();
//		獲取列數
			int columnCount = metaData.getColumnCount();
			if(rs.next()) {
				Order order = new Order();
				for(int i=0;i<columnCount;i++) {
//			獲取每個列的列值 通過resultset
					Object columValue = rs.getObject(i+1);
//			獲取每個列的列名 通過resultsetmetadate
//					String columnName = metaData.getColumnName(i+1); --不推薦使用
//					跳到CustomForQuery類 改用此方法 不取別名也沒關係
//			改使用 獲取列的別名方法getColumnLabe()
//			sql語句沒有取別名 此方法會獲取列名
				String columnLabel = metaData.getColumnLabel(i+1);
//			通過反射 將物件指定columnName的屬性賦值給columValue
//					Field field = order.getClass().getDeclaredField(columnName);
					Field field = order.getClass().getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(order, columValue);
				}
				return order;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, pstmt,rs);			
		}
		return null;
	}
	
	/**
	 * 查詢order表的基本型
	 */
	@Test
	public void testQuery1()  {
		Connection conn =null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			conn = JDBCutils.getConnection();
			String sql="SELECT order_id,order_name,order_date FROM `order` WHERE order_id =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setObject(1, 1);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int id=(int) rs.getObject(1);
				String name = (String) rs.getObject(2);
				Date date = (Date) rs.getObject(3);
				Order order = new Order(id,name,date);
				System.out.println(order);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, pstmt, rs);
		}
	}

}
