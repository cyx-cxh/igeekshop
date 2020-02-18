package com.igeek.dao;

import java.util.List;

import com.igeek.domain.Category;
import java.sql.Connection;

public interface CategoryDao {

	void add(Category cate);

	List<Category> findAll();

	Category findByid(String cid);

	void update(Category category);

	void del(Connection conn, String cid);



}
