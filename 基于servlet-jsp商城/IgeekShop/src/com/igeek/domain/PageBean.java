package com.igeek.domain;

import java.util.ArrayList;
import java.util.List;

public class PageBean<T> {
	private Integer currentPage;//��ǰ��ҳ��
	private Integer rows;//ÿҳ��ʾ�ļ�¼��
	private Integer totalCount;//�ܼ�¼��
	private Integer totalPage;//��ҳ����
	private List<T> list;//ÿҳ������
	//����
	private List<Integer> pages = new ArrayList<Integer>();
	
	public void setPagination(int currentPage,int totalPage) {
		pages.add(currentPage);
		for (int i = 1; i <=3; i++) {
			//��ǰչʾ��ҳ
			if(currentPage - i > 0) {
				pages.add(0,currentPage-i);
			}
			//���չʾ��ҳ
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
