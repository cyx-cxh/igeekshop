package com.igeek.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.igeek.dao.UserDao;
import com.igeek.domain.User;
import com.igeek.utils.JDBCUtils;


public class UserDaoImpl implements UserDao {
private QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
	@Override
	public User login(User user) {
		String sql="select * from users where email = ? and password=?";
		try {
			return qr.query(sql, new BeanHandler<User>(User.class),user.getEmail(),user.getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("ÓÃ»§µÇÂ¼Òì³£");
		}
		
	}
	@Override
	public int regist(User user) throws SQLException {
		String sql="INSERT INTO `users` (`uid`, `username`, `password`, `name`, `email`, `telephone`, `birthday`, `sex`, `state`, `code`) VALUES(?,?,?,?,?,?,?,?,?,?)";
		System.out.println(user);
			int row=qr.update(sql,user.getUid(),user.getUsername(),user.getPassword(),
					user.getName(),user.getEmail(),user.getTelephone(),user.getBirthday(),
					user.getSex(),user.getState(),user.getCode());
		
			return row;
	
		
	}
	@Override
	public void active(String activeCode) {
	String sql="update users set state=1 where code=?";
		try {
			qr.update(sql,activeCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public Long checkUsername(String username) {
		String sql="select count(*) from users where username=?";
		Long count=0L;
		try {
			 count = (Long)qr.query(sql, new ScalarHandler(),username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

}
