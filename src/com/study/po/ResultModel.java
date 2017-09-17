package com.study.po;

import java.util.List;

public class ResultModel {

	private List<Products> productList;
	private long total;//商品总数
	private int currentPage;//当前页
	private int totalPage;//总页数
	public List<Products> getProductList() {
		return productList;
	}
	public void setProductList(List<Products> productList) {
		this.productList = productList;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}