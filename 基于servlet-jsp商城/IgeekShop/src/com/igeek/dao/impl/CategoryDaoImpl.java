package com.igeek.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.igeek.dao.CategoryDao;
import com.igeek.domain.Category;
import com.igeek.utils.JDBCUtils;
import java.sql.Connection;


public class CategoryDaoImpl implements CategoryDao {
private QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
	@Override
	public void add(Category cate) {
	String sql="insert into category values(?,?)";
	try {
		qr.update(sql,cate.getCid(),cate.getCname());
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new RuntimeException("ÃÌº”∑÷¿‡ ß∞‹");
	}
		
	}
	@Override
	public List<Category> findAll() {
		String sql="select * from category";
		try {
			return qr.query(sql,new BeanListHandler<Category>(Category.class));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	@Override
	public Category findByid(String cid) {
		
		String sql="select * from category where cid = ?";
		try {
			return qr.query(sql, new BeanHandler<Category>(Category.class),cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	@Override
	public void update(Category category) {
		String sql="update category set cname=? where cid = ?";
try {
	qr.update(sql,category.getCname(),category.getCid());
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		
	}
	@Override
	public void del(Connection conn,String cid) {
		String sql="delete from category where cid =?";
		
		try {
			qr.update(conn,sql,cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
