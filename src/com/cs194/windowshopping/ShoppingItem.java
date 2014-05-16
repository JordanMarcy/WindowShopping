package com.cs194.windowshopping;

public class ShoppingItem {
	private String name;
	private String price;
	private String url;
	private int iconID;
	
	public ShoppingItem(String name, String price, String url) {
		super();
		this.name = name;
		this.price = price;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getPrice() {
		return price;
	}

	public String getUrl() {
		return url;
	}	

}
