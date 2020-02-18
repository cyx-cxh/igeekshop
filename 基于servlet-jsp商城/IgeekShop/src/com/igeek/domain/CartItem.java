package com.igeek.domain;

public class CartItem {
private Product product;
private int buyNum;
private double subTotal;

public CartItem(Product product, int buyNum, double subTotal) {
	super();
	this.product = product;
	this.buyNum = buyNum;
	this.subTotal = subTotal;
}
public Product getProduct() {
	return product;
}
public void setProduct(Product product) {
	this.product = product;
}
public int getBuyNum() {
	return buyNum;
}
public void setBuyNum(int buyNum) {
	this.buyNum = buyNum;
}
public double getSubTotal() {
	return subTotal;
}
public void setSubTotal(double subTotal) {
	this.subTotal = subTotal;
}
@Override
public String toString() {
	return "CartItem [product=" + product + ", buyNum=" + buyNum + ", subTotal=" + subTotal + "]";
}

}
