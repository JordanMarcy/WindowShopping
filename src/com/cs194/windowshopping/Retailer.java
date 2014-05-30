package com.cs194.windowshopping;

import java.io.Serializable;

public class Retailer implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String retailer, price;
	
	public Retailer(String retailer, String price) {
		this.retailer = retailer;
		this.price = price;
	}

	public String getRetailer() {
		return retailer;
	}

	public String getPrice() {
		return price;
	}
}
