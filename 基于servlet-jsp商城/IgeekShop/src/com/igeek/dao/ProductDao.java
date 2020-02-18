package com.igeek.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.igeek.domain.Order;
import com.igeek.domain.Product;
import java.sql.Connection;


public interface ProductDao {

	List<Product> findAll();

	void release(Connection conn,String cid);

	Product findId(String pid);

	void update(Product p);

	int findTotalCount(Map<String, String[]> condition);

	List<Product> findUserByPage(int start, int rows, Map<String, String[]> condition);

	void delete(String pid);

	void add(Product product);
	public List<Product>findHotProducts()throws SQLException;
	public List<Product> findNewProducts()throws SQLException;

	void addOrders(Order order) ;
	

	 void addOderItems(Order order) ;

	void updateOrderInfo(Order order);
	
	
	

}
