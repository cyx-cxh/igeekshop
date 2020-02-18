package com.igeek.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.igeek.dao.ProductDao;
import com.igeek.dao.impl.ProductDaoImpl;
import com.igeek.domain.Order;
import com.igeek.domain.Page;
import com.igeek.domain.PageBean;
import com.igeek.domain.Product;
import com.igeek.domain.User;
import com.igeek.service.ProductService;
import com.igeek.utils.JDBCUtils;

public class ProductServiceImpl implements ProductService {
private ProductDao productDao=new ProductDaoImpl();
	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return productDao.findAll();
	}
	@Override
	public Product findById(String pid) {
		// TODO Auto-generated method stub
		return productDao.findId(pid);
	}
	@Override
	public void update(Product p) {
		productDao.update(p);
		
	}
	@Override
	public PageBean<Product> findUserByPage(String _currentPage, String _rows, Map<String, String[]> condition) {
		int currentPage = Integer.parseInt(_currentPage); 
		int rows = Integer.parseInt(_rows);
		if(currentPage <= 0) {
			currentPage = 1;
		}
		//1.创建PageBean对象
		PageBean<Product> pb = new PageBean<Product>();
		//2.设置参数
		pb.setCurrentPage(currentPage);
		pb.setRows(rows);
		//3.调用dao查询总记录数
		int totalCount = productDao.findTotalCount(condition);
		pb.setTotalCount(totalCount);
		//4.计算总页码
		int totalPage =  (totalCount % rows) == 0 ? (totalCount/rows) : ((totalCount/rows)+1);
		pb.setTotalPage(totalPage);
		//5.每页的数据
		int start = (currentPage-1) * rows;
		List<Product> list = productDao.findUserByPage(start,rows,condition);
		pb.setList(list);
		pb.setPagination(currentPage, totalPage);
		return pb;
	}
	@Override
	public void delete(String pid) {
		productDao.delete(pid);
		
	}
	@Override
	public void add(Product product) {
		productDao.add(product);
		
	}
	@Override
	public List<Product> findHotProducts() throws SQLException {
		
		List<Product> hotProductList =productDao.findHotProducts();
		return hotProductList;
	}
	@Override
	public List<Product> findNewProducts() throws SQLException {
		 
		 List<Product> newProductList=productDao.findNewProducts();
		return newProductList;
	}
	@Override
	public void submitOrders(Order order) {
		Connection conn=null;
		try {
			 conn = JDBCUtils.getConnection();
			JDBCUtils.startTransaction(conn);
			productDao.addOrders(order);
			productDao.addOderItems(order);
		} catch (SQLException e) {
		try {
			JDBCUtils.rollback();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		e.printStackTrace();
		}
		finally {
			try {
				JDBCUtils.commitAndRelease(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void updateOrderInfo(Order order) {
		productDao.updateOrderInfo(order);
		
	}


}
