package com.ispan.garylee7.dao.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ispan.garylee3.bean.Customer;
import com.ispan.garylee3.util.JDBCutils;
import com.ispan.garylee7.dao.CustomerDAOImpl;

public class CustomerDAOImplTest {
	
	private CustomerDAOImpl dao= new CustomerDAOImpl();
	
	@Test
	public void testInsert() {
		Connection conn =null;
		try {
			conn = JDBCutils.getConnection();
			Customer customer = new Customer(1,"garytest","garylee@.com",new Date());
			dao.insert(conn, customer);
			System.out.println("添加成功");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);			
		}
	}

	@Test
	public void testDeleteById() {
		Connection conn =null;
		try {
			conn = JDBCutils.getConnection();
			dao.deleteById(conn, 30);
			System.out.println("刪除成功");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);			
		}
	}

	@Test
	public void testUpdate() {
		Connection conn =null;
		try {
			conn = JDBCutils.getConnection();
			Customer customer = new Customer(19,"修改","123@com",new Date());
			dao.update(conn, customer);
			System.out.println("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);			
		}
	}

	@Test
	public void testGetCustomerById() {
		Connection conn =null;
		try {
			conn = JDBCutils.getConnection();
			Customer customer = dao.getCustomerById(conn, 29);
			System.out.println(customer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);			
		}
	}

	@Test
	public void testGetAll() {
		Connection conn =null;
		try {
			conn = JDBCutils.getConnection();
			List<Customer> list = dao.getAll(conn);
			list.forEach(System.out::println);
			System.out.println("");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);			
		}
	}

	@Test
	public void testGetCount() {
		Connection conn =null;
		try {
			conn = JDBCutils.getConnection();
			Long count = dao.getCount(conn);
			System.out.println("表中得紀錄數為"+count);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);			
		}
	}

	@Test
	public void testGetMaxBirth() {
		Connection conn =null;
		try {
			conn = JDBCutils.getConnection();
			Date maxBirth = dao.getMaxBirth(conn);
			System.out.println("最大的生日為："+maxBirth);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(conn, null);			
		}
	}

}
