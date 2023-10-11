package com.ispan.garylee4.exer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import org.junit.Test;

import com.ispan.garylee3.util.JDBCutils;
import com.mysql.cj.x.protobuf.MysqlxCrud.Update;

public class Exer2Test {
/*
 *  examstudent insert功能
 *  Type
 *  IDCard
 *  ExamCard
 *  StudentName
 *  Location
 *  Grade
 */
		@Test
		public void testInsert() {
			Scanner scanner = new Scanner(System.in);
			System.out.println("輸入年級");
			int type = scanner.nextInt();
			System.out.println("身份字號");
			String idCard= scanner.next();
			System.out.println("考證號碼");
			String examCard= scanner.next();
			System.out.println("學生姓名");
			String studentName= scanner.next();
			System.out.println("所在城市");
			String location= scanner.next();
			System.out.println("考試成績");
			String grade= scanner.next();
			String sql ="INSERT INTO examstudent(type,IDCard,examCard,studentName,Location,Grade)value(?,?,?,?,?,?)";
			int insertCount= update(sql,type,idCard,examCard,studentName,location,grade);
			if(insertCount>0) {
				System.out.println("加入成功");
			}else {
				System.out.println("加入失敗");
			}
		}
		
//		通用更新
		public int update(String sql,Object... args )  {
			Connection connection =null;
			PreparedStatement prepareStatement=null;
			try {
				connection = JDBCutils.getConnection();
				prepareStatement = connection.prepareStatement(sql);
				for(int i=0;i<args.length;i++) {
					prepareStatement.setObject(i+1, args[i]);
				}

				return prepareStatement.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				JDBCutils.closeResource(connection, prepareStatement);
			}
			return 0;
		}
}
