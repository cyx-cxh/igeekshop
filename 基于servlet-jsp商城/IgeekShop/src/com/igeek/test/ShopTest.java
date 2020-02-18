package com.igeek.test;

import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;


import com.igeek.domain.Category;
import com.igeek.domain.Product;
import com.igeek.domain.User;
import com.igeek.service.CategoryService;
import com.igeek.service.ProductService;
import com.igeek.service.UserService;
import com.igeek.service.impl.CategoryServiceImpl;
import com.igeek.service.impl.ProductServiceImpl;
import com.igeek.service.impl.UserServiceImpl;
import com.igeek.utils.JDBCUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class ShopTest {
	private QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
//	@Test
//public void text01() {
//	UserService service=new UserServiceImpl();
//	User user=new User();
//	user.setEmail("1121616623@qq.com");
//	user.setPassword("1234");
//	user.setNickname("ºÆ¸ç");
//	User login = service.login(user);
//	System.out.println(login);
//	
//	
//}
	@Test
	public void text02() {
		CategoryService service=new CategoryServiceImpl();
		
		List<Category> list = service.findAll();
		for (Category category : list) {
			System.out.println(category);
		}
		
	}
	@Test
	public void text03() {
		
		CategoryService service=new CategoryServiceImpl();
		
		 Category category = service.findByid("1");
	
			System.out.println(category);
		
		
	}
	@Test
	public void text04() throws SQLException {
		String sql = "select count(*)from product  ";
		
		int intValue = ((Long)qr.query(sql, new ScalarHandler())).intValue();
		
		System.out.println(intValue);
	}
	@Test
	public void text05() throws SQLException {
		 QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
		User user=new User();
		
		String sql="INSERT INTO `users` (`uid`, `username`, `password`, `name`, `email`, `telephone`, `birthday`, `sex`, `state`, `code`) VALUES(?,?,?,?,?,?,?,?,?,?)";
		try {
			int row=qr.update(sql,"3","2","3",
					"3","4","4",null,
					"2",1,"2");
		
			System.out.println(row);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("×¢²áÊ§°Ü");
		}
		
	}
	@Test
	public void text06() throws SQLException {
		UserService userService=new UserServiceImpl();
		boolean checkUsername = userService.checkUsername("11111");
		System.out.println(checkUsername);
		
		
		
	}
}
