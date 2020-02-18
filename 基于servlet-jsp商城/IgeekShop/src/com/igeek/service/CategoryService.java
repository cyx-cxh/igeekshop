package com.igeek.service;

import java.util.List;

import com.igeek.domain.Category;
import com.igeek.domain.Product;

public interface CategoryService {

	void add(Category cate);

	List<Category> findAll();

	Category findByid(String cid);

	void update(Category category);

	void delete(String cid);

	List<Category> findCategoryList();

	

	

}
