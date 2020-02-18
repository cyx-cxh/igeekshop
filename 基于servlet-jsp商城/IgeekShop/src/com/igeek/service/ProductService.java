package com.igeek.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.igeek.domain.Order;
import com.igeek.domain.PageBean;
import com.igeek.domain.Product;


public interface ProductService {

	List<Product> findAll();

	Product findById(String pid);

	void update(Product p);

	

	PageBean<Product> findUserByPage(String currentPage, String rows, Map<String, String[]> condition);

	void delete(String pid);

	void add(Product product);
	public List<Product>findHotProducts()throws SQLException;
	public List<Product> findNewProducts()throws SQLException;

	void submitOrders(Order order);

	void updateOrderInfo(Order order);
}
