package com.igeek.utils;

import java.sql.Connection;

import java.sql.SQLException;


import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;



public class JDBCUtils {
	
private static ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
	
	
	public static Connection getConnection() throws SQLException{
		return comboPooledDataSource.getConnection();
	}
	
	public static DataSource getDataSource(){
		return comboPooledDataSource;
	}

	public static void startTransaction() throws SQLException {
		comboPooledDataSource.getConnection().setAutoCommit(false);
	}
	
	public static void startTransaction(Connection conn) throws SQLException {
		conn.setAutoCommit(false);
	}

	public static void commit() throws SQLException {
		comboPooledDataSource.getConnection().commit();
	}
	
	public static void commit(Connection conn) throws SQLException {
		conn.commit();
	}
	
	public static void rollback() throws SQLException {
		comboPooledDataSource.getConnection().rollback();
	}
	
	public static void rollback(Connection conn) throws SQLException {
		conn.rollback();
	}
	
	public static void close() throws SQLException {
		comboPooledDataSource.getConnection().close();
	}
	
	public static void close(Connection conn) throws SQLException {
		if(conn != null)
			conn.close();
	}
	public static void commitAndRelease(Connection conn) throws SQLException {
	if (conn!=null) {
		commit(conn);
		close(conn);
	}
	
	}
	
}
