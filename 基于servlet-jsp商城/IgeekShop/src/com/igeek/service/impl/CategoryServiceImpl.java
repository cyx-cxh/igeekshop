package com.igeek.service.impl;



import java.sql.SQLException;
import java.util.List;

import com.igeek.dao.CategoryDao;
import com.igeek.dao.ProductDao;
import com.igeek.dao.impl.CategoryDaoImpl;
import com.igeek.dao.impl.ProductDaoImpl;
import com.igeek.domain.Category;

import com.igeek.service.CategoryService;
import com.igeek.utils.JDBCUtils;
import java.sql.Connection;

public class CategoryServiceImpl implements CategoryService {
private CategoryDao categoryDao=new CategoryDaoImpl();
	@Override
	public void add(Category cate) {
		categoryDao.add(cate);
		
	}
	@Override
	public List<Category> findAll() {
		return categoryDao.findAll();
	}
	@Override
	public Category findByid(String cid) {
		
		return categoryDao.findByid(cid);
	}
	@Override
	public void update(Category category) {
		categoryDao.update(category);
		
	}
	private ProductDao productDao=new ProductDaoImpl();
	@Override
	public void delete(String cid) {
		Connection conn=null;
		try {
			conn= (Connection)JDBCUtils.getConnection();
			JDBCUtils.startTransaction(conn);
			productDao.release(conn,cid);
			categoryDao.del(conn,cid);
			JDBCUtils.commit(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				JDBCUtils.rollback(conn);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			try {
				JDBCUtils.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		
	}
	@Override
	public List<Category> findCategoryList() {
		List<Category> list = categoryDao.findAll();
		return list;
	}
	


}
