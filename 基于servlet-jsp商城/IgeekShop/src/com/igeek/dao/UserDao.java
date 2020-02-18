package com.igeek.dao;

import java.sql.SQLException;

import com.igeek.domain.User;

public interface UserDao {

	User login(User user);

	int regist(User user) throws SQLException;

	void active(String activeCode);

	Long checkUsername(String username);

}
