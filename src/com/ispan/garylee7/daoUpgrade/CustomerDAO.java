package com.ispan.garylee7.daoUpgrade;

import java.util.*;
import java.sql.Connection;

import com.ispan.garylee3.bean.Customer;

/*
 * 此接口用於規範針對customers表的常用操作
 */
public interface CustomerDAO {
// 將customer 加入資料庫中
	void insert(Connection conn,Customer customer);
//刪除指定id一條資料
	void deleteById(Connection conn,int id);
//	修改資料的內容
	void update(Connection conn,Customer customer);
// 	返回一筆資料
	Customer getCustomerById(Connection conn,int id);
//	返回全部資料
	List<Customer> getAll(Connection conn);
//	返回資料中的條目數
	Long getCount(Connection conn);
//	返回資料表中最大生日
	Date getMaxBirth(Connection conn);
}
