package com.igeek.domain;

import java.util.ArrayList;
import java.util.List;

public class PageBean<T> {
	private Integer currentPage;//当前的页数
	private Integer rows;//每页显示的记录数
	private Integer totalCount;//总记录数
	private Integer totalPage;//总页码数
	private List<T> list;//每页的数据
	//新增
	private List<Integer> pages = new ArrayList<Integer>();
	
	public void setPagination(int currentPage,int totalPage) {
		pages.add(currentPage);
		for (int i = 1; i <=3; i++) {
			//向前展示三页
			if(currentPage - i > 0) {
				pages.add(0,currentPage-i);
			}
			//向后展示三页
			if(currentPage + i <= totalPage) {
				pages.add(currentPage+i);
			}
		}
	}
	
	public List<Integer> getPages() {
		return pages;
	}
	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}

}
