package com.cs194.windowshopping;

import java.io.IOException;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//import com.ECS.client.jax.AWSECommerceService;
//import com.ECS.client.jax.AWSECommerceServicePortType;
//import com.ECS.client.jax.Item;
//import com.ECS.client.jax.ItemLookup;
//import com.ECS.client.jax.ItemLookupRequest;
//import com.ECS.client.jax.ItemLookupResponse;


/**
 * Contains product details for an query result from <code>ProductSearch</code>.
 * @author Stephen
 * @see	   ProductSearch
 */
public class ProductSearchHit {
	
	String price, rating, name, brand, upc;
	ArrayList<Retailer> retailers;
	
	Bitmap picture = null;
	
	/**
	 * 
	 * @param price
	 * @param rating
	 * @param name
	 * @param brand
	 * @param retailer
	 */
	ProductSearchHit(String price, String rating, String name, String brand,
			ArrayList<Retailer> retailers, String upc) {
		this.price = price;
		this.rating = rating;
		this.name = name;
		this.brand = brand;
		this.retailers = retailers;
		this.upc = upc;
	}
	
	/**
	 * Returns the price of the product.
	 * @return price (including dollar sign)
	 */
	public String getPrice() {
		return price;
	}
	
	/**
	 * Returns the rating of a product.
	 * @return	rating in the range [1,5]
	 */
	public String getRating() {
		return rating;
	}
	
	/**
	 * REturns the name of the product
	 * @return product name
	 */
	public String getProductName() {
		return name;
	}
	
	/**
	 * Returns the name of the brand of the product
	 * @return brand's name
	 */
	public String getBrandName() {
		return brand;
	}
	
	/**
	 * Returns the name of the retailer selling the product
	 * @return retailer's name
	 */
	public ArrayList<Retailer> getRetailer() {
		return retailers;
	}
	
	public Retailer getRetailerByIndex(int i) {
		return retailers.get(i);
	}
	
	
	/**
	 * Returns an image of the product.
	 * @return a bitmap or null if no image is available
	 */
	public Bitmap getImage() {
		if(picture == null) {
			HttpClient client = HttpClients.createDefault();
			HttpGet getRequest = new HttpGet("http://content.hwigroup.net/images/products/xl/155820/3/apple_macbook_pro_retina_mc976na.jpg");
			HttpResponse response = null;
			try {
				response = client.execute(getRequest);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpEntity entity = response.getEntity();
			if(entity != null) {
				try {
					picture = BitmapFactory.decodeStream(entity.getContent());
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return picture;
	}
	
//	private String getImageURL() {
//		AWSECommerceService service = new AWSECommerceService();
//		AWSECommerceServicePortType port = service.getAWSECommerceServicePort();
//		
//		ItemLookupRequest request = new ItemLookupRequest();
//		request.setIdType("UPC");
//		request.getItemId().add(upc);
//		request.getResponseGroup().add("Images");
//		
//		ItemLookup lookup = new ItemLookup();
//		lookup.getRequest().add(request);
//		
//		ItemLookupResponse response = port.itemLookup(lookup);
//		Item item = response.getItems().get(0).getItem().get(0);
//		return item.getSmallImage().getURL();
//	}
	
}

