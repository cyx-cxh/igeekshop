package com.igeek.service.impl;


import com.igeek.dao.UserDao;
import com.igeek.dao.impl.UserDaoImpl;
import com.igeek.domain.User;
import com.igeek.service.UserService;

public class UserServiceImpl implements UserService {
private UserDao userdao=new UserDaoImpl();
	@Override
	public User login(User user) {
		// TODO Auto-generated method stub
		return userdao.login(user);
	}
	@Override
	public boolean regist(User user) {
	int row=0;
	try {
		row=userdao.regist(user);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
		return row>0?true:false;
	}
	@Override
	public void active(String activeCode) {
		userdao.active(activeCode);
		
	}
	@Override
	public boolean checkUsername(String username) {
		Long count=0L;
		try {
			count=userdao.checkUsername(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count>0?true:false;
	}

}
