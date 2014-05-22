package com.cs194.windowshopping;

//import com.ECS.client.jax.AWSECommerceService;
//import com.ECS.client.jax.AWSECommerceServicePortType;
//import com.ECS.client.jax.Item;
//import com.ECS.client.jax.ItemLookup;
//import com.ECS.client.jax.ItemLookupRequest;
//import com.ECS.client.jax.ItemLookupResponse;


//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;

/**
 * Contains product details for an query result from <code>ProductSearch</code>.
 * @author Stephen
 * @see	   ProductSearch
 */
public class ProductSearchHit {
	
	String price, rating, name, brand, retailer, upc;
//	Bitmap picture = null;
	
	/**
	 * 
	 * @param price
	 * @param rating
	 * @param name
	 * @param brand
	 * @param retailer
	 */
	ProductSearchHit(String price, String rating, String name, String brand,
			String retailer, String upc) {
		this.price = price;
		this.rating = rating;
		this.name = name;
		this.brand = brand;
		this.retailer = retailer;
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
	public String getRetailerName() {
		return retailer;
	}
	
	
	/**
	 * Returns an image of the product.
	 * @return a bitmap or null if no image is available
	 */
//	public Bitmap getImage() {
//		if(picture == null) {
//			HttpClient client = HttpClients.createDefault();
//			HttpGet getRequest = new HttpGet("http://content.hwigroup.net/images/products/xl/155820/3/apple_macbook_pro_retina_mc976na.jpg");
//			HttpResponse response = client.execute(getRequest);
//			HttpEntity entity = response.getEntity();
//			if(entity != null) {
//				picture = BitmapFactory.decodeStream(entity.getContent());
//			}
//		}
//		return picture;
//	}
	
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
