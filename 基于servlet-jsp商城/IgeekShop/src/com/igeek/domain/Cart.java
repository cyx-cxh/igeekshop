package com.igeek.domain;

import java.util.HashMap;
import java.util.Map;

public class Cart {
private Map<String, CartItem> cartItems=new HashMap<String, CartItem>();

private double total;

public Map<String, CartItem> getCartItems() {
	return cartItems;
}

public void setCartItems(Map<String, CartItem> cartItems) {
	this.cartItems = cartItems;
}

public double getTotal() {
	return total;
}

public void setTotal(double total) {
	this.total = total;
}

@Override
public String toString() {
	return "Cart [cartItems=" + cartItems + ", total=" + total + "]";
}

}
