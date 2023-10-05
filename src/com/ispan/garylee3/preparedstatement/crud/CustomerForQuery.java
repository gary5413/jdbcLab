package com.ispan.garylee3.preparedstatement.crud;


import java.sql.Connection;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.ispan.garylee3.bean.Customer;
import com.ispan.garylee3.util.JDBCutils;


/**
 * 
 * @author garylee
 * 真對Customers表查詢操作
 */
public class CustomerForQuery {
	
	@Test
	public void testQueryForCustomers() {
		String sql1="SELECT id,name,email FROM customers WHERE id =?";
		Customer customer1 = queryForCustomers(sql1, 3);
//		會出現date=null
		System.out.println(customer1);
		String sql2="SELECT id,name,email,birth FROM customers WHERE id =?";
		Customer customer2 = queryForCustomers(sql2, 3);
		System.out.println(customer2);
		String sql3="SELECT name,email,birth FROM customers WHERE name =?";
		Customer customer3 = queryForCustomers(sql3, "Gary");
//		會出去 id=0
		System.out.println(customer3);
		
	}
	
	/**
	 * 針對customers表通用查詢操作
	 * @throws Exception 
	 */
	public Customer queryForCustomers(String sql,Object ...args) {
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
//		獲取結果集元數據 
			ResultSetMetaData metaData = rs.getMetaData();
//		通過ResultSetMetaData 獲取結果集列數
			int columnCount = metaData.getColumnCount();
			if(rs.next()) {
//			建議寫這邊 
				Customer customer = new Customer();
				for(int i=0;i<columnCount;i++) {
					Object columValue = rs.getObject(i+1);
//				思考 需要把拿出來得值 放到物件裡
//				獲取每個列的列名
//					String columnName = metaData.getColumnName(i+1);
//					使用getColumnLabel() 獲取別名
					String columnLabel = metaData.getColumnLabel(i+1);
					
//				給customer物件指定屬性 賦值為value 
//				如何操作 通過反射
//					java.lang.reflect.Field field = Customer.class.getDeclaredField(columnName);
					java.lang.reflect.Field field = Customer.class.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(customer, columValue);
				}
				return customer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, pstmt, rs);			
		}
		return null;
	}
	
	@Test
	public void testQuery1(){
//		匡選的時候要留意不要把 關閉框住 
		Connection conn =null;
		PreparedStatement pstmt =null;
		ResultSet resultSet =null;
		try {
			conn = JDBCutils.getConnection();
			String sql="SELECT id,name,email,birth FROM customers WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setObject(1, 1);
			// 執行 並返回結果集
			resultSet = pstmt.executeQuery();
//		處理結果集
//		next()判斷結果集下一條是否有數據 
//		如果有返回true 並指向下一個 如果返回false 指向不移動
			if(resultSet.next()) {
//			獲取當前數據各個值
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				Date birth = resultSet.getDate(4);
//			方式一
//			System.out.println("id="+id+",name="+name+"email="+email+"birth="+birth);
//			方式二
//			Object[] date=new Object[] {id,name,email,birth};
//			方式三
				Customer customer = new Customer(id,name,email,birth);
				System.out.println(customer);
//			關閉資源 resultset要關閉
//			重新修改JDBCutils close方法
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, pstmt, resultSet);
		}
	}
}
