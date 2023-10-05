package com.ispan.garylee1.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

public class ConnectionTest {
	

	//方式ㄧ
	@Test
	public void testConnection1() throws SQLException {
		
		Driver driver=new com.mysql.jdbc.Driver();
		// url 用於辨識哪個資料庫
		/*
		 * jdbc:mysql://localhost:3306/db
		 * jdbc:mysql協議
		 * localhost:ip位置
		 * 3306:mysql默認
		 * db:DB name
		 */
		String url="jdbc:mysql://localhost:3306/mytestdb";
		
		Properties info=new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "5413gary");
		
		Connection conn=driver.connect(url, info);
		System.out.println("1"+conn);
		
	};
	
	//方式二 
	//對方式一的迭代 再如下程序中部出現第三方api 使得程序具有更好的移植性
	//竟量避免出現第三方api 維護難度增加
	@Test
	public void testConnecton2() throws Exception {
		//1.獲取Driver實現類對象 使用反射
		Class clazz=Class.forName("com.mysql.jdbc.Driver");
		Driver driver=(Driver)clazz.newInstance();
		//2.提供連線相關資料庫url
		String url="jdbc:mysql://localhost:3306/mytestdb";
		//3.註冊相關資料庫帳號密碼
		Properties info=new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "5413gary");
		//4.獲取連接
		Connection conn=driver.connect(url,info);
		System.out.println("2"+conn);
		
	}
	
	//方式三
	/*
	 *  使用DriverManager替換二
	 */
	@Test
	public void testConnection3() throws Exception {
		//1.獲取Driver實現類對象 使用反射
		Class clazz=Class.forName("com.mysql.jdbc.Driver");
		Driver driver=(Driver)clazz.newInstance();
		
		//2.提供另外三個連接基本訊息
		String url="jdbc:mysql://localhost:3306/mytestdb";
		String user="root";
		String password="5413gary";
		//註冊驅動
		DriverManager.registerDriver(driver);
		//獲取連接
		DriverManager.getConnection(url,user,password);
		Connection conn=DriverManager.getConnection(url,user,password);
		System.out.println("3"+conn);
	}
	
	//方式四
	@Test
	public void testConnection4() throws Exception {
		
		//提供另外三個連接基本訊息
		String url="jdbc:mysql://localhost:3306/mytestdb";
		String user="root";
		String password="5413gary";
		
		//加載Driver
		/*
		 * 相較於程式三可以省略操作
		 * 在註冊時mysql Driver實現類
		 */
		Class clazz=Class.forName("com.mysql.jdbc.Driver"); //可以省略但不建議 
//		Class clazz=Class.forName("com.mysql.jdbc.Driver");
//		Driver driver=(Driver)clazz.newInstance();
		
		//註冊驅動
//		DriverManager.registerDriver(driver);
		
		//獲取連接
		DriverManager.getConnection(url,user,password);
		Connection conn=DriverManager.getConnection(url,user,password);
		System.out.println("4"+conn);
	}
	
	//方式五
	/*
	 * 將資料庫連線資料 放在文件中 通過讀取文件來獲取
	 * 耦合度降低 此方式更改資料庫資訊較為容易維護
	 */
	@Test
	public void getConnetion5() throws Exception {
		//讀取jdbc.properties
		InputStream is=ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros=new Properties();
		pros.load(is);
		String user =pros.getProperty("user");
		String password =pros.getProperty("password");
		String url =pros.getProperty("url");
		String driverClass =pros.getProperty("driverClass");
		
		//加載驅動
		Class.forName(driverClass);
		
		//獲取連接
		Connection conn=DriverManager.getConnection(url,user,password);
		System.out.println("5"+conn);
		
	}
	
}
