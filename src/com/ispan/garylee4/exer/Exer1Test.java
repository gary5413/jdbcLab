package com.ispan.garylee4.exer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import org.junit.Test;

import com.ispan.garylee3.util.JDBCutils;

/**
 * 
 * @author garylee
 *
 */
public class Exer1Test {

	@Test
	public void testInsert() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("請輸入姓名");
		String name = scanner.next();
		System.out.println("請輸入email");
		String email= scanner.next();
		System.out.println("請輸入生日");
		String birth =scanner.next();
		String sql ="INSERT INTO customers(name,email,birth) VALUES (?,?,?)";
		int insertCount = update(sql, name,email,birth);
		if(insertCount >0) {
			System.out.println("新增成功");
		}else {
			System.out.println("新增失敗");
		}
	}
	
//	public void update(String sql,Object... args )  {
	public int update(String sql,Object... args )  {
		Connection connection =null;
		PreparedStatement prepareStatement=null;
		try {
			connection = JDBCutils.getConnection();
			prepareStatement = connection.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
				prepareStatement.setObject(i+1, args[i]);
			}
			/**
			 *  execute() 也是回傳boolean值
			 *  如果執行的查詢操作 有返回結果 則此方法返回true
			 *  如果執行增刪改 沒有返回結果則此方法返回false
			 */
//			prepareStatement.execute();
			/*
			 * 方式二 回傳int
			 */
			return prepareStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(connection, prepareStatement);
		}
		return 0;
	}
}
