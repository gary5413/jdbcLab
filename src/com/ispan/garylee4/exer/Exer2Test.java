package com.ispan.garylee4.exer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

import org.junit.Test;

import com.ispan.garylee3.bean.Student;
import com.ispan.garylee3.util.JDBCutils;
import com.mysql.cj.x.protobuf.MysqlxCrud.Update;

public class Exer2Test {
		/*
		 * 
		 *  功能1 examstudent insert
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
		
/*
 * 		功能2 查詢功能
 */
		@Test
		public void queryWithIDCardOrExamCard() {
			System.out.println("請選擇您要輸入的類型");
			System.out.println("a.准考證號碼");
			System.out.println("b.身分證字號");
			Scanner scanner = new Scanner(System.in);
			String selection = scanner.next();
			if("a".equalsIgnoreCase(selection)) {
				System.out.println("請輸入准考證號碼");
				String examCard =scanner.next();
				String sql="SELECT FlowID flowID,Type type,IDCard iDCard,ExamCard examCard,StudentName name,Location location,Grade grade "
						+ "FROM examstudent WHERE examCard = ? ";
				Student student = getInstance(Student.class,sql,examCard);
				if(student !=null) {
					System.out.println(student.toString());
				}else {
					System.out.println("准考證號碼輸入有誤");
				}
				
			}else if("b".equalsIgnoreCase(selection)) {
				System.out.println("請輸入身份證字號碼");
				String idCard =scanner.next();
				String sql="SELECT FlowID flowID,Type type,IDCard iDCard,ExamCard examCard,StudentName name,Location location,Grade grade "
						+ "FROM examstudent WHERE iDCard = ? ";
				Student student = getInstance(Student.class,sql,idCard);
				if(student !=null) {
					System.out.println(student.toString());
				}else {
					System.out.println("輸入身份字號有誤");
				}
				
			}else {
				System.out.println("您輸入有誤");
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
				JDBCutils.closeResource(conn, pstmt, rs);			
			}
			return null;
		}
		/*
		 * 功能3 刪除功能
		 */
		@Test
		public void testDeleteByExamCard() {
			System.out.println("請輸入學生准考證號碼");
			Scanner scanner = new Scanner(System.in);
			String examCard = scanner.next();
			String sql="SELECT FlowID flowID,Type type,IDCard iDCard,ExamCard examCard,StudentName name,Location location,Grade grade "
					+ "FROM examstudent WHERE examCard = ? ";
			Student student = getInstance(Student.class, sql, examCard);
			if(student ==null) {
				System.out.println("輸入錯誤，請重新輸入");
			}else {
				String sql1="DELETE FROM examstudent WHERE examCard=?";
				int deleteCount =update(sql1, examCard);
				if(deleteCount >0) {
					System.out.println("刪除成功");
				}
			}
		}
		@Test
		public void testDeleteByExamCard1() {
			System.out.println("請輸入學生准考證號碼");
			Scanner scanner = new Scanner(System.in);
			String examCard = scanner.next();
			String sql1="DELETE FROM examstudent WHERE examCard=?";
			int deleteCount =update(sql1, examCard);
			if(deleteCount >0) {
				System.out.println("刪除成功");
			}else {
				System.out.println("查無此人，請重新輸入");
			}
		}
}
