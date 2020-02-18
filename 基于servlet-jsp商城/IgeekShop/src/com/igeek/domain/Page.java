package com.igeek.domain;

import java.util.Arrays;
import java.util.List;

public class Page {
private int total;
private int rows;
private int end;
private int page;
private List<Product>data;
private int[]bar;
public int getTotal() {
	return total;
}

public int getRows() {
	return rows;
}

public void setRows(int rows) {
	this.rows = rows;
}

public int getEnd() {
	return end;
}

public void setEnd(int end) {
	this.end = end;
}

public int getPage() {
	return page;
}

public void setPage(int page) {
	this.page = page;
}

public List<Product> getData() {
	return data;
}

public void setData(List<Product> data) {
	this.data = data;
}

public void setTotal(int total) {
	this.total = total;
}

public int[] getBar() {
	//确定好开头和结尾
	int start;
	int stop;
	//少于10页
	if (end<10) {
		start=1;
		stop=end;
	}else {
		start=page-5;
		stop=page+4;
		if (page+4>=end) {
			start=end-9;
			stop=end;
		}
		if (page-5<=0) {
			start=1;
			stop=10;
		}
	}
	bar=new int[stop-start+1];
	int index=0;
	for (int i = start; i < stop; i++) {
		bar[index++]=i;
		
	}
	return bar;
}
public void setBar(int[] bar) {
	this.bar = bar;
}

@Override
public String toString() {
	return "Page [total=" + total + ", rows=" + rows + ", end=" + end + ", page=" + page + ", data=" + data + ", bar="
			+ Arrays.toString(bar) + "]";
}


}
