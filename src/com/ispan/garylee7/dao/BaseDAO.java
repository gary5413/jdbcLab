package com.ispan.garylee7.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ispan.garylee3.util.JDBCutils;

/*
 * DAO : data access object
 * 封裝針對資料表的通用操作
 * 
 */
//
public abstract class BaseDAO {
//	通用增刪改操作 考慮交易
	public int update(Connection conn,String sql,Object ...args) {
		PreparedStatement pstmt =null;
		try {
			pstmt = conn.prepareStatement(sql);
			for(int i=0;i<args.length;i++) {
				pstmt.setObject(i+1, args[i]);;
			}
			return pstmt.executeUpdate();
		} catch  (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(null, pstmt);			
		}
		return 0;
	}
//	通用查詢返回單一資料 考慮交易
	public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object...args ) {
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
			JDBCutils.closeResource(null, pstmt, rs);			
		}
		return null;
	}
	
//	通用查詢返回多個資料 考慮交易
	public <T> List<T> getForList(Connection conn,Class<T> clazz,String sql,Object...args){
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
			ArrayList<T> list = new ArrayList<T>();
			while(rs.next()) {
				T t=clazz.newInstance(); 
				for(int i=0;i<columnCount;i++) {
					Object columValue = rs.getObject(i+1);
					String columnLabel = metaData.getColumnLabel(i+1);
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
			JDBCutils.closeResource(null, pstmt, rs);			
		}
		return null;
	}
//	用於查詢特殊的查詢
	public <E> E getValue(Connection conn,String sql,Object...args)  {
		PreparedStatement ps=null;
		ResultSet rs =null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i = 0;i<args.length;i++) {
				ps.setObject(i+1, args[i]);
			}
			rs = ps.executeQuery();
			if(rs.next()) {
				return(E) rs.getObject(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCutils.closeResource(null, ps, rs);
		}
		return null;
	}
	
}
