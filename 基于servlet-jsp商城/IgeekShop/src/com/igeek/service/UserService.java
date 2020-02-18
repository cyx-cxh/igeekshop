package com.igeek.service;

import com.igeek.domain.User;

public interface UserService {

	public User login(User user) ;

	public boolean regist(User user);

	public void active(String activeCode);

	public boolean checkUsername(String username);

}
