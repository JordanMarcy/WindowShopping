package com.cs194.windowshopping;

public class ProductReview {
	String review, rating;
	
	ProductReview(String review, String rating) {
		this.review = review;
		this.rating = rating;
	}
	
	public String getText() {
		return this.review;
	}
	
	public String getRating() {
		return rating;
	}
}
