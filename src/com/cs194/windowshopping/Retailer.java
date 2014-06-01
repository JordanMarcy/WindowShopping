package com.cs194.windowshopping;

import java.io.Serializable;

public class Retailer implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String retailer, price, website;
	
	public Retailer(String retailer, String price, String website) {
		this.retailer = retailer;
		this.price = price;
		this.website = website;
	}

	public String getRetailer() {
		return retailer;
	}

	public String getPrice() {
		return price;
	}
	
	public String getWebsite() {
		return website;
	}
}
